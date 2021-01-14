package io.blocko.exception.advice;

import io.blocko.exception.GroupAlreadyExistsException;
import io.blocko.exception.GroupInvalidDeleteException;
import io.blocko.exception.GroupNotFoundException;
import io.blocko.exception.UserAlreadyExistsException;
import io.blocko.exception.UserNotFoundException;
import io.blocko.response.ErrorForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  private static final String INTERNAL_ERR_MSG = "server error";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleException(Exception e) {
    log.info(e.getMessage());
    return new ErrorForm(INTERNAL_ERR_MSG);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleAuthenticationException(AuthenticationException e) {
    log.info(e.getMessage());
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleUserNotFoundException(UserNotFoundException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(GroupNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleGroupNotFoundException(GroupNotFoundException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(GroupAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleGroupAlreadyExistsException(GroupAlreadyExistsException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(GroupInvalidDeleteException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleGroupInvalidDeleteException(GroupInvalidDeleteException e) {
    return new ErrorForm(e.getMessage());
  }
}
