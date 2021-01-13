package io.blocko.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserInfo {

  private String email;

  private String name;

  private String group;

  @Builder
  public UserInfo(String email, String name, String group) {
    this.email = email;
    this.name = name;
    this.group = group;
  }
}
