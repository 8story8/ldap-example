package io.blocko.exception;

public class GroupAlreadyExistsException extends RuntimeException {

  private static final String ERR_MSG = "이미 존재하는 그룹입니다.";

  public GroupAlreadyExistsException() {
    super(ERR_MSG);
  }
}
