package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.validators.SQLInjectionMatch;
import org.unibl.etf.forumback.validators.XSSMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank
    @Size(max = 50)
    @XSSMatch
    @SQLInjectionMatch
    private String name;
    @NotBlank
    @Size(max = 50)
    @XSSMatch
    @SQLInjectionMatch
    private String surname;
    @NotBlank
    @Size(max = 50)
    @Email
    @XSSMatch
    @SQLInjectionMatch
    private String mail;
    @NotBlank
    @Size(max = 50)
    @XSSMatch
    @SQLInjectionMatch
    private String username;
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$", message = "Password must contain at least one uppercase letter and one digit")
    @XSSMatch
    @SQLInjectionMatch
    private String password;

}
