package ism.dakar.edumanage.security.services.impls;

import ism.dakar.edumanage.security.api.models.TokenDto;
import ism.dakar.edumanage.security.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${app.jwt.key}")
    private String jwtKey;

    @Value("${app.jwt.time}")
    private long jwtTime;

    @Override
    public TokenDto getToken() {
        TokenDto token = new TokenDto();
        token.setKey(jwtKey);
        token.setTime(jwtTime);
        return token;
    }
}
