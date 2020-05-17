package solvve.course.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import solvve.course.domain.PortalUser;

import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    private UUID id;

    public UserDetailsImpl(PortalUser user) {
        super(user.getEmail(), user.getEncodedPassword(),
                user.getUserRoles().stream().map(r -> new SimpleGrantedAuthority(r.getUserGroupType().toString()))
                    .collect(Collectors.toList()));
        id = user.getId();
    }
}
