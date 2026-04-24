package ism.dakar.edumanage.security.configs;

import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.exceptions.AccountLockedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider userAuthenticationProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        try{
            if (header != null) {
                String[] authElements = header.split(" ");

                if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                    try {
                        var auth= userAuthenticationProvider.validateToken(authElements[1]);
                        SecurityContextHolder.getContext().setAuthentication(auth);

                        AppUserDto user = (AppUserDto) auth.getPrincipal();
                        if (!user.isActif())
                            throw new AccountLockedException("Votre compte est bloqué");

                        org.slf4j.MDC.put("user", user.getEmail());

                    } catch (RuntimeException e) {
                        SecurityContextHolder.clearContext();
                        throw e;
                    }
                }

            }
        } catch (RuntimeException ex){
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
