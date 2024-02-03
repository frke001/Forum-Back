package org.unibl.etf.forumback.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentDTO {

    private Long id;
    private String text;
    private String creationDate;
    private Boolean approved;
    private Long userSenderId;
    private String userSenderUsername;
    private String userSenderName;
    private String userSenderSurname;
}
