package org.unibl.etf.forumback.services.impl;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.exceptions.AccountBlockedException;
import org.unibl.etf.forumback.models.dto.JwtUserDTO;
import org.unibl.etf.forumback.models.dto.UserDTO;
import org.unibl.etf.forumback.models.dto.UserDetailsDTO;
import org.unibl.etf.forumback.models.entities.UserEntity;
import org.unibl.etf.forumback.models.enums.Role;
import org.unibl.etf.forumback.repositories.UserRepository;
import org.unibl.etf.forumback.services.JwtService;
import org.unibl.etf.forumback.services.OAuth2Service;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OAuth2ServiceImpl implements OAuth2Service {


    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${redirectUri}")
    private String redirectUri;

    @Value("${accessTokenUri}")
    private String accessTokenUri;
    @Value("${userDetailsUri}")
    private String userDetailsUri;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public OAuth2ServiceImpl(UserRepository userRepository, JwtService jwtService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO oAuth2signIn(String code) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(accessTokenUri)
                .addParameter("client_id", clientId)
                .addParameter("client_secret", clientSecret)
                .addParameter("code", code)
                .addParameter("redirect_uri", redirectUri)
                .build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setEntity(new StringEntity("", ContentType.APPLICATION_FORM_URLENCODED));

        HttpResponse response = client.execute(postRequest);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        // Extract the access token from the response body
        String accessToken = extractAccessToken(responseBody);
        // Use the access token to fetch user details
        String userDetails = getUserDetails(accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UserDetailsDTO userDetailsDTO = objectMapper.readValue(userDetails, UserDetailsDTO.class);
        UserEntity userEntity = null;
        if(userRepository.existsByMail(userDetailsDTO.getEmail())){
            userEntity = userRepository.findByMail(userDetailsDTO.getEmail()).get();
            if(userEntity.getBlocked()){
                throw new AccountBlockedException();
            }
        }else{
                userEntity = new UserEntity();
                userEntity.setId(null);
                userEntity.setUsername(userDetailsDTO.getLogin());
                if (userDetailsDTO.getName() != null) {
                    String params[] = userDetailsDTO.getName().split(" ");
                    userEntity.setName(params[0]);
                    userEntity.setSurname(params[1]);
                } else {
                    userEntity.setName(userDetailsDTO.getLogin());
                    userEntity.setSurname(userDetailsDTO.getLogin());
                }
                userEntity.setPassword(null);
                userEntity.setMail(userDetailsDTO.getEmail());
                userEntity.setVerified(true);
                userEntity.setBlocked(false);
                userEntity.setRole(Role.CLIENT);
                userEntity.setCode(null);
                userEntity =  userRepository.saveAndFlush(userEntity);
                entityManager.refresh(userEntity);
        }
        JwtUserDTO jwtUserDTO = modelMapper.map(userEntity,JwtUserDTO.class);
        var token = jwtService.generateToken(jwtUserDTO);
        UserDTO loginResponse = new UserDTO();
        loginResponse.setToken(token);
        List<String> permissions = userEntity.getPermissions().stream().map(el->el.getName()).collect(Collectors.toList());
        loginResponse.setPermissions(permissions);
        return loginResponse;
    }

    private String extractAccessToken(String responseBody) {
        String prefix = "access_token=";
        String suffix = "&scope";
        int startIndex = responseBody.indexOf(prefix);
        int endIndex = responseBody.indexOf(suffix);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return responseBody.substring(startIndex + prefix.length(), endIndex);
        }
        return null; // Return null if the access token cannot be extracted
    }

    private String getUserDetails(String accessToken) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(userDetailsUri)
                .build();

        // Create an HTTP GET request
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(uri);
        getRequest.setHeader("Authorization", "Bearer " + accessToken);

        // Send the request and get the response
        HttpResponse response = client.execute(getRequest);

        // Get the response body as a string
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);


        // Return the response body to the caller
        return responseBody;
    }

}
