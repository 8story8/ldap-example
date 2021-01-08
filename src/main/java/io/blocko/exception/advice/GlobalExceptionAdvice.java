package io.blocko.exception.advice;

import io.blocko.exception.UnauthenticatedUserException;
import io.blocko.exception.UnauthorizedUserException;
import io.blocko.exception.UserNotFoundException;
import io.blocko.response.ErrorForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    return new ErrorForm(INTERNAL_ERR_MSG);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleUserNotFoundException(UserNotFoundException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(UnauthenticatedUserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleUnauthenticatedUserException(UnauthenticatedUserException e) {
    return new ErrorForm(e.getMessage());
  }

  @ExceptionHandler(UnauthorizedUserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleUnauthorizedUserException(UnauthorizedUserException e) {
    return new ErrorForm(e.getMessage());
  }
}
