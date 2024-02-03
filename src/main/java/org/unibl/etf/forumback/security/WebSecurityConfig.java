package org.unibl.etf.forumback.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.unibl.etf.forumback.models.enums.Role;
import org.unibl.etf.forumback.services.JwtUserDetailsService;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(JwtUserDetailsService jwtUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                //.csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers(HttpMethod.GET,"/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/permissions").hasAuthority(Role.ADMIN.getRole())
                                .requestMatchers(HttpMethod.GET,"/api/v1/users").hasAuthority(Role.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").hasAuthority(Role.ADMIN.getRole())
                                .requestMatchers(HttpMethod.GET,"/api/v1/forum-categories/pending").hasAnyAuthority(Role.ADMIN.getRole(), Role.MODERATOR.getRole())
                                .requestMatchers(HttpMethod.GET,"/api/v1/forum-categories/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/forum-categories/*").hasAuthority("CREATE")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/forum-categories/comments/*").hasAuthority("UPDATE")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/forum-categories/comments/accept/*").hasAnyAuthority(Role.ADMIN.getRole(), Role.MODERATOR.getRole())
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/forum-categories/comments/forbid/*").hasAnyAuthority(Role.ADMIN.getRole(), Role.MODERATOR.getRole())
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/forum-categories/comments/*/*").hasAuthority("DELETE")
                                .anyRequest().denyAll()

                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jwtUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
