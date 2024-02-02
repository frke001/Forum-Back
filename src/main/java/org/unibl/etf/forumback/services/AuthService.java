package org.unibl.etf.forumback.services;

import org.unibl.etf.forumback.models.dto.*;

public interface AuthService {

    void register(UserRegisterDTO request);
    boolean checkDetails(DetailsRequestDTO detailsRequestDTO);

    Long login(LoginDTO request);

    UserDTO checkCode(Long id, CodeDTO request);
}
