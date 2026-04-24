package ism.dakar.edumanage.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import ism.dakar.edumanage.security.api.enums.StatusRequete;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class AccesDeniedException implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("status", StatusRequete.ACCESS_DENIED);
        data.put("error", "Accès Refusé");
        data.put("message", "Vous n'avez pas les droits nécessaires pour effectuer cette action.");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(data));
    }
}