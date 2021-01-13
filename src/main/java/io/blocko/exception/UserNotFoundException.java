package io.blocko.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends RuntimeException {

  private static final String ERR_MSG = "존재하지 않는 사용자입니다.";

  public UserNotFoundException() {
    super(ERR_MSG);
  }
}
