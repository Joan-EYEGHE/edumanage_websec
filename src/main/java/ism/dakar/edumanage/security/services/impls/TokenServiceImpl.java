package ism.dakar.edumanage.security.services.impls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import ism.dakar.edumanage.security.api.models.TokenDto;
import ism.dakar.edumanage.security.helpers.HelperService;
import ism.dakar.edumanage.security.services.interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService, CommandLineRunner {

    private static String TOKENPATH = "public/token/token.json";
    private static String PATH_DIRECTORY = "public/token";

    private static ObjectMapper mapper=new ObjectMapper();
    private final HelperService helperService;


    @Override
    public TokenDto getToken() {
        try {
            init();
            if (Files.exists(Path.of(TOKENPATH))){
                return mapper.readValue(
                        new File(TOKENPATH),
                        new TypeReference<TokenDto>(){}
                );
            }
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void init() {
        try {
            if (!Files.exists(Path.of(TOKENPATH))){
                helperService.createDirectoryIsNotExist(PATH_DIRECTORY);
                TokenDto token= new TokenDto();
                token.setTime(18000000);
                token.setKey(generateNewToken());
                mapper.writeValue(new File(TOKENPATH), token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateNewToken() {
//            generation du code
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//            recuperation du code
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }
}
