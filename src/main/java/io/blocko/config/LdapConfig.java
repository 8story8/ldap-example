package io.blocko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

  @Value("${ldap.url}")
  private String url;

  @Value("${ldap.base}")
  private String base;

  @Value("${ldap.userDn}")
  private String userDn;

  @Value("${ldap.password}")
  private String password;

  @Bean
  public LdapContextSource contextSource(){
    LdapContextSource contextSource = new LdapContextSource();
    contextSource.setUrl(url);
    contextSource.setBase(base);
    contextSource.setUserDn(userDn);
    contextSource.setPassword(password);
    return contextSource;
  }

  @Bean
  public LdapTemplate template(){
    return new LdapTemplate(contextSource());
  }
}
