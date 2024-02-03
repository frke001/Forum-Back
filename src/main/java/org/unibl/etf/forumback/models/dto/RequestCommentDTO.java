package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDTO {

    @NotNull
    private Long userSenderId;
    @NotBlank
    @Size(max = 1000)
    private String text;

}
