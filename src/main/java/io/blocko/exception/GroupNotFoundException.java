package io.blocko.exception;

public class GroupNotFoundException extends RuntimeException {

  private static final String ERR_MSG = "존재하지 않는 그룹입니다.";

  public GroupNotFoundException() {
    super(ERR_MSG);
  }
}
