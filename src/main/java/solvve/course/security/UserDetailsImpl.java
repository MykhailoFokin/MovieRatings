package solvve.course.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import solvve.course.domain.PortalUser;

import java.util.stream.Collectors;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    public UserDetailsImpl(PortalUser user) {
        super(user.getEmail(), user.getEncodedPassword(),
                user.getUserRoles().stream().map(r -> new SimpleGrantedAuthority(r.getType().toString()))
                    .collect(Collectors.toList()));
    }
}
