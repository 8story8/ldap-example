package io.blocko.auth;

import io.blocko.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LdapService {

  private static final String ROLE_PREFIX = "ROLE_";

  private final LdapTemplate template;

  /**
   * 사용자 인증.
   * @param email
   * @param password
   * @return
   */
  public boolean authenticate(String email, String password) {
    Filter filter = new EqualsFilter("uid", email);
    return template.authenticate(LdapUtils.emptyLdapName(), filter.encode(), password);
  }

  /**
   * 사용자 조회.
   * @param email
   * @return
   */
  public Optional<LdapUser> findByEmail(String email) {
    LdapQuery query = LdapQueryBuilder.query().where("uid").is(email);
    List<LdapUser> user =
        template.search(
            query,
            new AbstractContextMapper<LdapUser>() {
              @Override
              protected LdapUser doMapFromContext(DirContextOperations ctx) {
                String email = ctx.getStringAttribute("uid");
                String name = ctx.getStringAttribute("cn");
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(
                    new SimpleGrantedAuthority(
                        ROLE_PREFIX + LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase()));
                return LdapUser.builder().email(email).name(name).authorities(authorities).build();
              }
            });

    if (user.size() != 1) {
      throw new UserNotFoundException();
    }

    return Optional.of(user.get(0));
  }

  /**
   * 사용자 목록 조회.
   * @return
   */
  public List<LdapUser> findAll() {
    LdapQuery query = LdapQueryBuilder.query().where("objectClass").is("person");
    List<LdapUser> userList =
        template.search(
            query,
            new AbstractContextMapper<LdapUser>() {
              @Override
              protected LdapUser doMapFromContext(DirContextOperations ctx) {
                String email = ctx.getStringAttribute("uid");
                String name = ctx.getStringAttribute("cn");
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(
                    new SimpleGrantedAuthority(
                        ROLE_PREFIX + LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase()));
                return LdapUser.builder().email(email).name(name).authorities(authorities).build();
              }
            });
    return userList;
  }
}
