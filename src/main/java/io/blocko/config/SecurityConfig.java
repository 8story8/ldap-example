package io.blocko.config;

import io.blocko.auth.LdapAccessDeniedHandler;
import io.blocko.auth.LdapAuthenticationProvider;
import io.blocko.auth.LdapTokenAuthenticationEntryPoint;
import io.blocko.auth.LdapTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final LdapAuthenticationProvider ldapAuthenticationProvider;
  private final LdapTokenAuthenticationEntryPoint ldapTokenAuthenticationEntryPoint;
  private final LdapAccessDeniedHandler ldapAccessDeniedHandler;
  private final LdapTokenFilter ldapTokenFilter;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
        "/v2/api-docs", "/swagger-resources/**",
        "/swagger-ui.html", "/webjars/**", "/swagger/**");
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
        .antMatchers(HttpMethod.GET, "/users")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/users/{email}")
        .hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.POST, "/users")
        .permitAll()
        .antMatchers(HttpMethod.PUT, "/users")
        .hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.DELETE, "/users")
        .hasAnyRole("ADMIN", "USER")
        .antMatchers("/groups/**")
        .hasRole("ADMIN")
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
