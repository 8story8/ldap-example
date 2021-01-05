package io.blocko.api;

import io.blocko.dto.LoginForm;
import io.blocko.response.ResultForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApi {

  @RequestMapping("/login")
  public ResponseEntity<ResultForm> login(LoginForm loginForm){

    return ResponseEntity.ok(new ResultForm("token"));
  }
}