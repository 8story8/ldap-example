package io.blocko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdate {

  private String group;

  private String email;

  private String toBeUpdatedPassword;
  private String toBeUpdatedName;
}
