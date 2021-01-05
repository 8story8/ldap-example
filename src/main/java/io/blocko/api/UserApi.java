package io.blocko.api;

import io.blocko.response.ResultForm;
import io.blocko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ResultForm> register(){
    return ResponseEntity.ok(new ResultForm());
  }

  @PostMapping
  public ResponseEntity<ResultForm> update(){
    return ResponseEntity.ok(new ResultForm());
  }

  @PostMapping
  public ResponseEntity<ResultForm> delete(){
    return ResponseEntity.ok(new ResultForm());
  }

  @PostMapping
  public ResponseEntity<ResultForm> findByEmail(){
    return ResponseEntity.ok(new ResultForm());
  }

  @PostMapping
  public ResponseEntity<ResultForm> findAll(){
    return ResponseEntity.ok(new ResultForm());
  }
}
