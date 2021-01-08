package io.blocko.exception;

public class UnauthorizedUserException extends RuntimeException {

  private static final String ERR_MSG = "권한이 없습니다.";

  public UnauthorizedUserException() {
    super(ERR_MSG);
  }
}
