package io.blocko.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class TokenUtil {

  private static final String HEADER_KEY = "Authorization";
  private static final String HEADER_VALUE = "Bearer ";

  @Value("${jwt.secret}")
  private String secret;

  /**
   * 토큰 추출.
   * @param request
   * @return
   */
  public String parse(HttpServletRequest request) {
    String tokenHeader = request.getHeader(HEADER_KEY);
    String token = null;
    if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(HEADER_VALUE)) {
      token = tokenHeader.substring(7, tokenHeader.length());
    }
    return token;
  }

  /**
   * 토큰 유효성 검사.
   * @param token
   * @return
   */
  public boolean validate(String token) {
    boolean isValid = false;
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      isValid = true;
    } catch (SignatureException e) {
      log.error("invalid jwt signature : {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("invalid jwt token : {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("jwt token is expired : {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("jwt token is unsupported : {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("jwt claims string is empty : {}", e.getMessage());
    }

    return isValid;
  }

  /**
   * 토큰으로부터 이메일 추출.
   * @param token
   * @return
   */
  public String extractEmail(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }
}
