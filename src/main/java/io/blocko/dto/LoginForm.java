package io.blocko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginForm {

  private String email;

  private String password;
}
