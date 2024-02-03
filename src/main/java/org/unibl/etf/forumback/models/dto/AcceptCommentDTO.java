package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.validators.SQLInjectionMatch;
import org.unibl.etf.forumback.validators.XSSMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptCommentDTO {

    @NotBlank
    @Size(max = 1000)
    @XSSMatch
    @SQLInjectionMatch
    private String text;
}
