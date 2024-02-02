package org.unibl.etf.forumback.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleDTO {

    @NotBlank
    @Pattern(regexp = "^(Admin|Moderator|Client)$", message = "Invalid role")
    private String role;
}
