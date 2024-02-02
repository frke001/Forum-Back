package org.unibl.etf.forumback.services;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendEmail(String code,String subject,String to);
}
