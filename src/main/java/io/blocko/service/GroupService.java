package io.blocko.service;

import io.blocko.dto.GroupUpdate;
import io.blocko.dto.UserInfo;
import io.blocko.exception.GroupAlreadyExistsException;
import io.blocko.exception.GroupInvalidDeleteException;
import io.blocko.exception.GroupNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final LdapTemplate template;

  public List<UserInfo> findDetailByGroup(String group) {
    if (!existsByGroup(group)) {
      throw new GroupNotFoundException();
    }

    List<UserInfo> userInfoList = new ArrayList<>();
    Name name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
    Filter filter = new EqualsFilter("objectClass", "person");
    try {
      userInfoList =
          template.search(
              name,
              filter.encode(),
              new AbstractContextMapper<UserInfo>() {
                @Override
                protected UserInfo doMapFromContext(DirContextOperations ctx) {
                  String email = ctx.getStringAttribute("uid");
                  String name = ctx.getStringAttribute("cn");
                  String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                  return UserInfo.builder().email(email).name(name).group(group).build();
                }
              });
      return userInfoList;
    } catch (NameNotFoundException e) {
      return userInfoList;
    }
  }

  /**
   * 그룹 존재 유무 확인.
   *
   * @param group
   * @return
   */
  private boolean existsByGroup(String group) {
    Name name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
    try {
      template.lookup(
          name,
          new AbstractContextMapper<String>() {
            @Override
            protected String doMapFromContext(DirContextOperations ctx) {
              return ctx.getStringAttribute("ou");
            }
          });
      return true;
    } catch (NameNotFoundException e) {
      return false;
    }
  }

  public List<String> findAll() {
    Filter filter = new EqualsFilter("objectClass", "organizationalUnit");
    List<String> groupList =
        template.search(
            LdapUtils.emptyLdapName(),
            filter.encode(),
            new AbstractContextMapper<String>() {
              @Override
              protected String doMapFromContext(DirContextOperations ctx) {
                return ctx.getStringAttribute("ou").toUpperCase();
              }
            });
    return groupList;
  }

  /**
   * 그룹 등록.
   *
   * @return
   */
  public String register(String group) {

    if (existsByGroup(group)) {
      throw new GroupAlreadyExistsException();
    }

    Name ldapName = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
    DirContextAdapter context = new DirContextAdapter(ldapName);
    context.setAttributeValue("objectClass", "organizationalUnit");
    context.setAttributeValue("ou", group.toLowerCase());
    template.bind(context);

    return group;
  }

  /**
   * 그룹 수정.
   *
   * @return
   */
  public String update(GroupUpdate groupUpdate) {
    String group = groupUpdate.getGroup();
    String toBeUpdatedGroup = groupUpdate.getToBeUpdatedGroup();

    if (!existsByGroup(group)) {
      throw new GroupNotFoundException();
    }

    if (existsByGroup(toBeUpdatedGroup)) {
      throw new GroupAlreadyExistsException();
    }

    Name name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
    Name updatedName =
        LdapNameBuilder.newInstance().add("ou", toBeUpdatedGroup.toLowerCase()).build();
    DirContextOperations context = template.lookupContext(name);
    template.rename(context.getDn(), updatedName);

    return toBeUpdatedGroup;
  }

  /**
   * 그룹 삭제.
   *
   * @return
   */
  public String delete(String group) {

    List<UserInfo> userInfoList = findDetailByGroup(group);

    if (userInfoList.size() > 0) {
      throw new GroupInvalidDeleteException();
    }

    LdapName name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
    template.unbind(name);

    return group;
  }
}
