package io.blocko.controller;

import io.blocko.response.ResultForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @RequestMapping("/login")
  public ResponseEntity<ResultForm> login(){

    return ResponseEntity.ok(new ResultForm("token"));
  }
}
