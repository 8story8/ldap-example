package io.blocko.exception.advice;

import io.blocko.exception.TokenValidationException;
import io.blocko.response.ErrorForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  private static final String INTERNAL_ERR_MSG = "서버 오류입니다.";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorForm handleException(Exception e){
    e.printStackTrace();
    log.error("exception msg : " + e.getMessage());
    return new ErrorForm(INTERNAL_ERR_MSG);
  }

  @ExceptionHandler(TokenValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorForm handleTokenValidationException(TokenValidationException e){
    log.error("token validation exception msg : " + e.getMessage());
    return new ErrorForm(e.getMessage());
  }
}
