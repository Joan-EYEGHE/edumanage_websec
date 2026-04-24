package ism.dakar.edumanage.security.api.models;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}