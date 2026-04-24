package ism.dakar.edumanage.security.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.services.interfaces.AppUserService;
import ism.dakar.edumanage.security.services.interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    private final TokenService tokenService;

    private final AppUserService appService;

    public String createToken(AppUserDto user) {
        Date now = new Date();

        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        String token= Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+ tokenService.getToken().getTime()))
                .signWith(getSignKey(), algorithm)
                .compact();

        return token;
    }

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenService.getToken().getKey()));
    }

    public Authentication validateToken(String token) {
        final String username = extractUsername(token);
        AppUserDto user = appService.getByLogin(username);
        if (user!=null && user.isActif() && !isTokenExpired(token)) {
            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }else{
            return null;
        }
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        }
        catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return extractAllClaim(token).getExpiration();
    }

    public String  extractUsername(String token){
        return extractAllClaim(token).getSubject();
    }

    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
