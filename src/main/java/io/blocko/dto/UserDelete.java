package io.blocko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDelete {

  private String group;

  private String email;
}
