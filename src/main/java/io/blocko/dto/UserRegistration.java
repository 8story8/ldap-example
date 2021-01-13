package io.blocko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRegistration {

  private String group;

  private String email;

  private String name;

  private String password;

}
