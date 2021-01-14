package io.blocko.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginForm {

  private String email;

  private String password;

  @Builder
  public LoginForm(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
