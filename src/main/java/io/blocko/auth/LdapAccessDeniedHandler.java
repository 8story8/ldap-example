package io.blocko.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.blocko.response.ErrorForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class LdapAccessDeniedHandler implements AccessDeniedHandler {

  private static final String ERR_MSG = "권한이 없습니다.";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ObjectMapper mapper = new ObjectMapper();
    ErrorForm errorForm = new ErrorForm(ERR_MSG);
    String errorResponse = mapper.writeValueAsString(errorForm);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write(errorResponse);
  }
}
