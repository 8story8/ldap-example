package io.blocko.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GroupUpdate {

  private String group;

  private String toBeUpdatedGroup;
}
