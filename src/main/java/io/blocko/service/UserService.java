package io.blocko.service;

import io.blocko.auth.LdapService;
import io.blocko.auth.LdapUser;
import io.blocko.dto.UserInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final LdapService ldapService;

  /**
   * 사용자 조회.
   *
   * @param email
   * @return
   */
  public UserInfo findByEmail(String email) {
    LdapUser ldapUser = ldapService.findByEmail(email).orElse(null);
    return UserInfo.builder()
        .email(ldapUser.getEmail())
        .name(ldapUser.getName())
        .groups(
            ldapUser.getAuthorities().stream()
                .map(group -> group.getAuthority().substring(5))
                .collect(Collectors.toList()))
        .build();
  }

  /**
   * 사용자 목록 조회.
   *
   * @return
   */
  public List<UserInfo> findAll() {
    List<LdapUser> ldapUserList = ldapService.findAll();
    return ldapUserList.stream()
        .map(
            ldapUser ->
                UserInfo.builder()
                    .email(ldapUser.getEmail())
                    .name(ldapUser.getName())
                    .groups(
                        ldapUser.getAuthorities().stream()
                            .map(group -> group.getAuthority().substring(5))
                            .collect(Collectors.toList()))
                    .build())
        .collect(Collectors.toList());
  }
}
