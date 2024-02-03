package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.validators.SQLInjectionMatch;
import org.unibl.etf.forumback.validators.XSSMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDTO {

    // zasticeni na nivou JSON parsera
    @NotNull
    private Long userSenderId;
    @NotBlank
    @Size(max = 1000)
    @XSSMatch
    @SQLInjectionMatch
    private String text;

}
