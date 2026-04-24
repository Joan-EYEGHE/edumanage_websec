package ism.dakar.edumanage.security.api.models;

import lombok.Data;

@Data
public class TokenDto {
    private String key;
    private long time;
}