package ism.dakar.edumanage.security.api.models;

import lombok.*;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TokenReponseDto {
    private String accessToken;
    private Date expiresIn;
    private Object user;
}
