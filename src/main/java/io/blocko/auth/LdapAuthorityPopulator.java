package io.blocko.auth;

import java.util.Collection;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LdapAuthorityPopulator implements LdapAuthoritiesPopulator {

  private static final String ROLE_PRIFIX = "ROLE_";

  private final LdapTemplate template;

  @Override
  public Collection<? extends GrantedAuthority> getGrantedAuthorities(
      DirContextOperations userData, String email) {
    String userDn = userData.getNameInNamespace();
    Collection<GrantedAuthority> authorities = new HashSet<>();
    // authorities.add((GrantedAuthority) userRole);
    return authorities;
  }
}
