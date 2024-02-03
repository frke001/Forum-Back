package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.validators.SQLInjectionMatch;
import org.unibl.etf.forumback.validators.XSSMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDTO {

    @NotBlank
    @Size(min = 4, max = 4)
    @XSSMatch
    @SQLInjectionMatch
    private String code;


}
