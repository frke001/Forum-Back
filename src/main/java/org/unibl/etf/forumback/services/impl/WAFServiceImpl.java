package org.unibl.etf.forumback.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.unibl.etf.forumback.exceptions.BadRequestException;
import org.unibl.etf.forumback.services.BlacklistService;
import org.unibl.etf.forumback.services.SIEMService;
import org.unibl.etf.forumback.services.WAFService;

import java.util.List;
@Service
public class WAFServiceImpl implements WAFService {

    private final SIEMService siemService;
    private final HttpServletRequest request;

    private final BlacklistService blacklistService;

    public WAFServiceImpl(SIEMService siemService, HttpServletRequest request, BlacklistService blacklistService) {
        this.siemService = siemService;
        this.request = request;
        this.blacklistService = blacklistService;
    }

    @Override
    public void checkValidity(BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            StringBuilder errorBuilder = new StringBuilder("Address: " + request.getRemoteAddr() + ".Validation failed: ");

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorBuilder.append(fieldError.getDefaultMessage()).append(", ");
            }
            errorBuilder.setLength(errorBuilder.length() - 2);
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null || authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                blacklistService.addToBlacklist(jwt);
            }
            siemService.log(errorBuilder.toString());
            throw new BadRequestException();

        }

    }
}
