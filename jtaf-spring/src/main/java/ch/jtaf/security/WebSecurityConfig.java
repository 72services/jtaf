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
                .antMatchers("/", "/css/**", "/i18n/**", "/images/**", "/util/**").permitAll()
                .antMatchers("/input/**").hasRole("user")
                .antMatchers("/spaces/**").hasRole("user")
                .antMatchers("/user/**").hasRole("user")
                .antMatchers(HttpMethod.PUT, "/res/users/**").hasRole("user")
                .antMatchers(HttpMethod.DELETE, "/res/users/**").hasRole("user")
                .antMatchers(HttpMethod.POST, "/res/athletes/**", "/res/categories/**", "/res/clubs/**", "/res/competitions/**", "/res/events/**", "/res/series/**", "/res/spaces/**").hasRole("user")
                .antMatchers(HttpMethod.PUT, "/res/athletes/**", "/res/categories/**", "/res/clubs/**", "/res/competitions/**", "/res/events/**", "/res/series/**", "/res/spaces/**").hasRole("user")
                .antMatchers(HttpMethod.DELETE, "/res/athletes/**", "/res/categories/**", "/res/clubs/**", "/res/competitions/**", "/res/events/**", "/res/series/**", "/res/spaces/**").hasRole("user")
                .and()
                .httpBasic();
    }
}
