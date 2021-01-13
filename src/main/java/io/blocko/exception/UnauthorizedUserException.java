package io.blocko.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedUserException extends AuthenticationException {

  private static final String ERR_MSG = "권한이 없습니다.";

  public UnauthorizedUserException() {
    super(ERR_MSG);
  }
}
