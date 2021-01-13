package io.blocko.exception;

public class GroupInvalidDeleteException extends RuntimeException {

  private static final String ERR_MSG = "사용자가 존재하여 그룹을 삭제할 수 없습니다.";

  public GroupInvalidDeleteException() {
    super(ERR_MSG);
  }
}
