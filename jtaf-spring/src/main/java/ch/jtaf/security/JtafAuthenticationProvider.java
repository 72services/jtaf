package ch.jtaf.security;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.SecurityGroup;
import ch.jtaf.entity.SecurityUser;
import org.jboss.crypto.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JtafAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DataService dataService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, authentication.getCredentials().toString());
        SecurityUser user = dataService.getUserByUsernameAndPassword(authentication.getName(), passwordHash);
        if (user != null) {
            List<SecurityGroup> groups = dataService.getGroupsByUsername(user.getEmail());
            List<SimpleGrantedAuthority> roles = new ArrayList<>();
            for (SecurityGroup group : groups) {
                roles.add(new SimpleGrantedAuthority("ROLE_" + group.getName()));
            }
            return new UsernamePasswordAuthenticationToken(user.getEmail(), null, roles);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
