package io.blocko.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class LdapTokenUtil {

  private static final String HEADER_KEY = "Authorization";
  private static final String HEADER_VALUE = "Bearer ";

  private static final String ERR_SIG_MSG="올바르지 않은 토큰 서명입니다.";
  private static final String ERR_MAL_MSG="올바르지 않은 토큰 형식입니다.";
  private static final String ERR_EXP_MSG="만료된 토큰입니다.";
  private static final String ERR_UNS_MSG="지원하지 않는 토큰입니다.";
  private static final String ERR_ILL_MSG="올바르지 않는 변수를 가지고 있는 토큰입니다.";

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.validity_time}")
  private long validityTime;

  /**
   * 토큰 추출.
   *
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
   *
   * @param token
   * @return
   */
  public boolean validate(HttpServletRequest request, String token) {
    boolean isValid = false;
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      isValid = true;
    } catch (SignatureException e) {
      request.setAttribute("ERR_MSG", ERR_SIG_MSG);
    } catch (MalformedJwtException e) {
      request.setAttribute("ERR_MSG", ERR_MAL_MSG);
    } catch (ExpiredJwtException e) {
      request.setAttribute("ERR_MSG", ERR_EXP_MSG);
    } catch (UnsupportedJwtException e) {
      request.setAttribute("ERR_MSG", ERR_UNS_MSG);
    } catch (IllegalArgumentException e) {
      request.setAttribute("ERR_MSG", ERR_ILL_MSG);
    }

    return isValid;
  }

  /**
   * 토큰 생성.
   *
   * @return
   */
  public String create(String email) {
    long currentTime = System.currentTimeMillis();
    long expiredTime = currentTime + validityTime;
    return Jwts.builder()
        .setId(email)
        .setIssuedAt(new Date(currentTime))
        .setExpiration(new Date(expiredTime))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  /**
   * 토큰으로부터 이메일 추출.
   *
   * @param token
   * @return
   */
  public String extractEmail(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
  }
}
