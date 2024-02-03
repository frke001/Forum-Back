package org.unibl.etf.forumback.services;


import org.unibl.etf.forumback.models.dto.UserDTO;

import java.io.IOException;
import java.net.URISyntaxException;

public interface OAuth2Service {

    UserDTO oAuth2signIn(String code) throws URISyntaxException, IOException;
}
