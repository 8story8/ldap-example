package io.blocko.service;

import io.blocko.auth.LdapUser;
import io.blocko.dto.UserDelete;
import io.blocko.dto.UserInfo;
import io.blocko.dto.UserRegistration;
import io.blocko.dto.UserUpdate;
import io.blocko.exception.UserAlreadyExistsException;
import io.blocko.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final String ROLE_PREFIX = "ROLE_";

  private final LdapTemplate template;

  /**
   * 사용자 인증.
   *
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
   *
   * @param email
   * @return
   */
  public UserInfo findByEmail(String email) {
    LdapQuery query = LdapQueryBuilder.query().where("uid").is(email);

    List<UserInfo> user =
        template.search(
            query,
            new AbstractContextMapper<UserInfo>() {
              @Override
              protected UserInfo doMapFromContext(DirContextOperations ctx) {
                String email = ctx.getStringAttribute("uid");
                String name = ctx.getStringAttribute("cn");
                String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                return UserInfo.builder().email(email).name(name).group(group).build();
              }
            });

    if (user.size() != 0) {
      throw new UserNotFoundException();
    }
    return user.get(0);
  }

  public Optional<UserInfo> findByGroupAndEmail(String group, String email) {
    Name ldapName =
        LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).add("uid", email).build();
    try {
      UserInfo user =
          template.lookup(
              ldapName,
              new AbstractContextMapper<UserInfo>() {
                @Override
                protected UserInfo doMapFromContext(DirContextOperations ctx) {
                  String email = ctx.getStringAttribute("uid");
                  String name = ctx.getStringAttribute("cn");
                  String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                  return UserInfo.builder().email(email).name(name).group(group).build();
                }
              });
      return Optional.of(user);
    } catch (NameNotFoundException e) {
      return Optional.ofNullable(null);
    }
  }

  /**
   * 사용자 목록 조회.
   *
   * @return
   */
  public List<UserInfo> findAll() {
    LdapQuery query = LdapQueryBuilder.query().where("objectClass").is("person");
    List<UserInfo> userList =
        template.search(
            query,
            new AbstractContextMapper<UserInfo>() {
              @Override
              protected UserInfo doMapFromContext(DirContextOperations ctx) {
                String email = ctx.getStringAttribute("uid");
                String name = ctx.getStringAttribute("cn");
                String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                return UserInfo.builder().email(email).name(name).group(group).build();
              }
            });
    return userList;
  }

  /**
   * 사용자 등록.
   *
   * @return
   */
  public UserInfo register(UserRegistration userRegistration) {
    String group = userRegistration.getGroup();
    String email = userRegistration.getEmail();
    String name = userRegistration.getName();
    String password = userRegistration.getPassword();

    UserInfo user = findByGroupAndEmail(group, email).orElse(null);

    if (user != null) {
      throw new UserAlreadyExistsException();
    }

    Name ldapName =
        LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).add("uid", email).build();
    DirContextAdapter context = new DirContextAdapter(ldapName);
    context.setAttributeValues(
        "objectClass", new String[] {"person", "uidObject", "simpleSecurityObject"});
    context.setAttributeValue("cn", name);
    context.setAttributeValue("sn", name);
    context.setAttributeValue("uid", email);
    context.setAttributeValue("userPassword", password);
    template.bind(context);

    return UserInfo.builder().email(email).name(name).group(group.toUpperCase()).build();
  }

  /**
   * 사용자 수정.
   *
   * @param userUpdate
   * @return
   */
  public UserInfo update(UserUpdate userUpdate) {
    String group = userUpdate.getGroup();
    String email = userUpdate.getEmail();
    UserInfo userInfo = findByGroupAndEmail(group, email).orElse(null);
    if (userInfo == null) {
      throw new UserNotFoundException();
    }
    Name name =
        LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).add("uid", email).build();
    DirContextOperations context = template.lookupContext(name);
    context.setAttributeValue("cn", userUpdate.getToBeUpdatedName());
    context.setAttributeValue("sn", userUpdate.getToBeUpdatedName());
    context.setAttributeValue("userPassword", userUpdate.getToBeUpdatedPassword());
    template.modifyAttributes(context);
    return UserInfo.builder()
        .email(email)
        .name(userUpdate.getToBeUpdatedName())
        .group(group)
        .build();
  }

  /**
   * 사용자 삭제.
   *
   * @return
   */
  public UserInfo delete(UserDelete userDelete) {
    UserInfo user = findByGroupAndEmail(userDelete.getGroup(), userDelete.getEmail()).orElse(null);

    if (user == null) {
      throw new UserNotFoundException();
    }

    LdapName name =
        LdapNameBuilder.newInstance()
            .add("ou", user.getGroup().toLowerCase())
            .add("uid", user.getEmail())
            .build();
    template.unbind(name);
    return user;
  }

  public UserDetails loadUserByEmail(String email) {
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
      throw new UsernameNotFoundException(email);
    }
    return user.get(0);
  }
}
