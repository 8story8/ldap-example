package io.blocko.config;

import io.blocko.auth.LdapAccessDeniedHandler;
import io.blocko.auth.LdapAuthenticationProvider;
import io.blocko.auth.LdapTokenAuthenticationEntryPoint;
import io.blocko.auth.LdapTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final LdapAuthenticationProvider ldapAuthenticationProvider;
  private final LdapTokenAuthenticationEntryPoint ldapTokenAuthenticationEntryPoint;
  private final LdapAccessDeniedHandler ldapAccessDeniedHandler;

  private final LdapTokenFilter ldapTokenFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ldapAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/login")
        .permitAll()
        .antMatchers("/users").hasRole("ADMIN")
        .antMatchers("/users/{email}").hasAnyRole("ADMIN", "USER")
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(ldapTokenAuthenticationEntryPoint)
        .accessDeniedHandler(ldapAccessDeniedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(ldapTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin()
        .disable();
  }
}
