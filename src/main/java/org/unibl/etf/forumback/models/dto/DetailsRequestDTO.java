package org.unibl.etf.forumback.models.dto;

import lombok.Data;
import org.unibl.etf.forumback.validators.SQLInjectionMatch;
import org.unibl.etf.forumback.validators.XSSMatch;

@Data
public class DetailsRequestDTO {

    private String mail;
    private String username;
}
