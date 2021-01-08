package io.blocko.exception;

public class UnauthenticatedUserException extends RuntimeException {

  private static final String ERR_MSG = "인증할 수 없는 사용자입니다.";

  public UnauthenticatedUserException() {
    super(ERR_MSG);
  }
}
