package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.repository.PortalUserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ModeratorPortalUserService extends AbstractService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    public List<PortalUserReadDTO> getBlockedPortalUsers(UUID portalUserId) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        List<PortalUser> blockedPortalUsers = portalUserRepository.findByUserConfidence(UserConfidenceType.BLOCKED);
        return blockedPortalUsers.stream().map(e ->
                translationService.translate(e, PortalUserReadDTO.class)).collect(Collectors.toList());
    }

    public PortalUserReadDTO blockUnblockPortalUser(UUID moderatorId, UUID portalUserId,
                                                    PortalUserPatchDTO patch) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, portalUserId);
        translationService.map(patch, portalUser);
        portalUser = portalUserRepository.save(portalUser);

        return translationService.translate(portalUser, PortalUserReadDTO.class);
    }
}
