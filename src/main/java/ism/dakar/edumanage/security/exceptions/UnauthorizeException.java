package ism.dakar.edumanage.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import ism.dakar.edumanage.security.api.enums.StatusRequete;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UnauthorizeException implements AuthenticationEntryPoint {
   @Override
   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
           throws ServletException, IOException {

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      Map<String, Object> data = new HashMap<>();
      data.put("status", StatusRequete.UNAUTHORIZED);
      data.put("message", "Veuillez vous authentifier.");

      new ObjectMapper().writeValue(response.getWriter(), data);
   }
}
