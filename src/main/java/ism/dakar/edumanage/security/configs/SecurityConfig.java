package ism.dakar.edumanage.security.configs;

import ism.dakar.edumanage.security.exceptions.AccesDeniedException;
import ism.dakar.edumanage.security.exceptions.UnauthorizeException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final UnauthorizeException userAuthenticationEntryPoint;
    private final AccesDeniedException accesDeniedException;
    private final UserAuthenticationProvider userAuthenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .exceptionHandling(customizer -> customizer.accessDeniedHandler(accesDeniedException))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html/**",
                                "/v1/api-docs/**",
                                "/users/login",
                                "/users/create"
                        ).permitAll()
//                        .anyRequest().permitAll())
                        .anyRequest().authenticated())
        ;
        return http.build();
    }
}
