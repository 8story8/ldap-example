package io.blocko.exception;

public class UserAlreadyExistsException extends RuntimeException {

  private static final String ERR_MSG = "이미 존재하는 사용자입니다.";

  public UserAlreadyExistsException() {
    super(ERR_MSG);
  }
}
