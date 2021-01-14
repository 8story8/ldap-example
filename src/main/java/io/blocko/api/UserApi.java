package io.blocko.api;

import io.blocko.dto.UserDelete;
import io.blocko.dto.UserInfo;
import io.blocko.dto.UserRegistration;
import io.blocko.dto.UserUpdate;
import io.blocko.exception.UnauthorizedUserException;
import io.blocko.response.ResultForm;
import io.blocko.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {

  private final UserService userService;

  /**
   * 사용자 조회.
   * @param email
   * @return
   */
  @GetMapping("/{email}")
  @ApiOperation(value = "사용자 조회", notes = "ADMIN, USER\nADMIN은 모든 사용자 조회 가능, 사용자는 자신만 조회 가능\n사용자 Email이 존재하지 않으면 사용자 없음 오류 발생")
  public ResponseEntity<ResultForm> findByEmail(@PathVariable("email") String email) {
    validateFindByEmail(email);
    UserInfo userInfo = userService.findByEmail(email);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }

  /**
   * 사용자 조회 유효성 검사.
   * 관리자는 모든 관리자, 사용자 조회 가능, 사용자는 자신만 조회 가능.
   * @param email
   */
  private void validateFindByEmail(String email) {
    boolean isUser = false;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_USER")) {
        if (authentication.getName().equals(email)) {
          isUser = true;
          break;
        }
      }
    }

    if (isUser) {
      throw new UnauthorizedUserException();
    }
  }

  /**
   * 사용자 목록 조회.
   * @return
   */
  @GetMapping
  @ApiOperation(value = "사용자 목록 조회", notes = "ADMIN")
  public ResponseEntity<ResultForm> findAll() {
    List<UserInfo> userInfoList = userService.findAll();
    return ResponseEntity.ok(new ResultForm(userInfoList));
  }

  /**
   * 사용자 등록.
   *
   * @param userRegistration
   * @return
   */
  @PostMapping
  @ApiOperation(value = "사용자 등록", notes = "ALL\n그룹이 존재하지 않으면 그룹 없음 오류 발생\n사용자가 존재하면 사용자 중복 오류 발생")
  public ResponseEntity<ResultForm> register(@RequestBody UserRegistration userRegistration) {
    UserInfo userInfo = userService.register(userRegistration);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }

  /**
   * 사용자 수정.
   *
   * @param userUpdate
   * @return
   */
  @PutMapping
  @ApiOperation(value = "사용자 수정", notes = "ADMIN, USER\n자신만 수정 가능\n그룹, 사용자 Email이 존재하지 않으면 사용자 없음 오류 발생")
  public ResponseEntity<ResultForm> update(@RequestBody UserUpdate userUpdate) {
    validateUpdateAndDelete(userUpdate.getEmail());
    UserInfo userInfo = userService.update(userUpdate);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }

  /**
   * 사용자 삭제.
   *
   * @param userDelete
   * @return
   */
  @DeleteMapping
  @ApiOperation(value = "사용자 삭제", notes = "ADMIN, USER\n자신만 삭제 가능\n그룹, 사용자 Email이 존재하지 않으면 사용자 없음 오류 발생")
  public ResponseEntity<ResultForm> delete(@RequestBody UserDelete userDelete) {
    validateUpdateAndDelete(userDelete.getEmail());
    UserInfo userInfo = userService.delete(userDelete);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }

  /**
   * 사용자 수정, 삭제 유효성 검사.
   * 사용자는 자신만 수정, 삭제 가능.
   * @param email
   */
  private void validateUpdateAndDelete(String email) {
    boolean isMe = false;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (authentication.getName().equals(email)) {
        isMe = true;
        break;
      }
    }
    if (!isMe) {
      throw new UnauthorizedUserException();
    }
  }
}
