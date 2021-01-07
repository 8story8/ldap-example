package io.blocko.api;

import io.blocko.dto.LoginForm;
import io.blocko.response.ResultForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApi {

  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<ResultForm> login(@RequestBody LoginForm loginForm) {
    String email = loginForm.getEmail();
    String password = loginForm.getPassword();
    System.out.println("1111");
    System.out.println(email);
    System.out.println(password);
    System.out.println("2222");
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    return ResponseEntity.ok(new ResultForm("token"));
  }
}
