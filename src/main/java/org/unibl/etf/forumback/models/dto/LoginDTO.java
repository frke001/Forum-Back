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
public class LoginDTO {

    @NotBlank
    @Size(max = 50)
    @XSSMatch
    @SQLInjectionMatch
    private String username;
    @NotBlank
    @Size(max = 50)
    @XSSMatch
    @SQLInjectionMatch
    private String password;
}
