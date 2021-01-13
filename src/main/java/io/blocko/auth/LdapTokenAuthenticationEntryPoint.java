package io.blocko.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.blocko.response.ErrorForm;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class LdapTokenAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final String ERR_MSG="토큰을 입력해주세요.";

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    String errorMsg = (String) request.getAttribute("ERR_MSG");
    ObjectMapper mapper = new ObjectMapper();
    ErrorForm errorForm = new ErrorForm(errorMsg == null ? ERR_MSG:  errorMsg);
    String errorResponse = mapper.writeValueAsString(errorForm);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(errorResponse);
  }
}
