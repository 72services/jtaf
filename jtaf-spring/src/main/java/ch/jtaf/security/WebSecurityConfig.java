package ch.jtaf.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST).hasRole("user")
                .antMatchers(HttpMethod.PUT).hasRole("user")
                .antMatchers(HttpMethod.DELETE).hasRole("user")
                .antMatchers("/", "/css/**", "/i18n/**", "/images/**", "/util/**").permitAll()
                .antMatchers("/input/**").hasRole("user")
                .antMatchers("/spaces/**").hasRole("user")
                .antMatchers("/user/**").hasRole("user")
                .and().formLogin().loginPage("/login.html").failureUrl("/login_error.html")
                .and().csrf().disable();
    }
}
