package io.blocko.api;

import io.blocko.dto.UserInfo;
import io.blocko.exception.UnauthorizedUserException;
import io.blocko.response.ResultForm;
import io.blocko.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {

  private final UserService userService;

  @GetMapping("/{email}")
  public ResponseEntity<ResultForm> findByEmail(@PathVariable("email") String email) {
    validateFindByEmail(email);
    UserInfo userInfo = userService.findByEmail(email);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }

  private void validateFindByEmail(String email){
    boolean isUser = false;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    for(GrantedAuthority authority : authentication.getAuthorities()){
      if(authority.getAuthority().equals("ROLE_USER")){
        if(authentication.getName().equals(email)){
          isUser = true;
          break;
        }
      }
    }

    if(isUser){
      throw new UnauthorizedUserException();
    }
  }

  @GetMapping
  public ResponseEntity<ResultForm> findAll(){
    List<UserInfo> userInfoList = userService.findAll();
    return ResponseEntity.ok(new ResultForm(userInfoList));
  }
}
