package solvve.course.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.UserConfidenceType;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@Service
public class CustomSecurityService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    public boolean isNotBlocked(UUID portalUserId) {
        return !portalUserRepository.existsByIdAndUserConfidence(portalUserId, UserConfidenceType.BLOCKED);
    }
}
