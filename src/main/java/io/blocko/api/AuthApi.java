package io.blocko.api;

import io.blocko.auth.LdapUser;
import io.blocko.auth.LdapTokenUtil;
import io.blocko.dto.LoginForm;
import io.blocko.response.ResultForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApi {

  private final AuthenticationManager authenticationManager;

  private final LdapTokenUtil ldapTokenUtil;

  @PostMapping("/login")
  public ResponseEntity<ResultForm> login(@RequestBody LoginForm loginForm) {
    String email = loginForm.getEmail();
    String password = loginForm.getPassword();
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    LdapUser user = (LdapUser) authentication.getPrincipal();
    String token = ldapTokenUtil.create(user.getEmail());
    return ResponseEntity.ok(new ResultForm(token));
  }
}
