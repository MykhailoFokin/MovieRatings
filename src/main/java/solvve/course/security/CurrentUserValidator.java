package solvve.course.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@Component
public class CurrentUserValidator {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private AuthenticationResolver authenticationResolver;

    public boolean isCurrentUser(UUID portalUserId) {
        Authentication authentication = authenticationResolver.getCurrentAuthentication();
        return portalUserRepository.existsByIdAndEmail(portalUserId, authentication.getName());
    }
}
