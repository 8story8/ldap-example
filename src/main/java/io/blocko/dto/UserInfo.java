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

  private List<String> groups;

  @Builder
  public UserInfo(String email, String name, List<String> groups) {
    this.email = email;
    this.name = name;
    this.groups = groups;
  }
}
