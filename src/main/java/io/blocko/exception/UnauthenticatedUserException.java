package io.blocko.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthenticatedUserException extends AuthenticationException {

  private static final String ERR_MSG = "인증할 수 없는 사용자입니다.";

  public UnauthenticatedUserException() {
    super(ERR_MSG);
  }
}
