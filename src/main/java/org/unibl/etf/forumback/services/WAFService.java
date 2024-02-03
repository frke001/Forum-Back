package org.unibl.etf.forumback.services;

import org.springframework.validation.BindingResult;


public interface WAFService {


    void checkValidity(BindingResult bindingResult);
}
