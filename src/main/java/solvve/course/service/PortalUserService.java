package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;
import solvve.course.dto.*;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@Service
public class PortalUserService extends AbstractService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional(readOnly = true)
    public PortalUserReadDTO getPortalUser(UUID id) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, id);
        return translationService.translate(portalUser, PortalUserReadDTO.class);
    }

    public PortalUserReadDTO createPortalUser(PortalUserCreateDTO create) {
        PortalUser portalUser = translationService.translate(create, PortalUser.class);

        portalUser.getUserRoles().add(userRoleRepository.findByUserGroupType(UserGroupType.USER));
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);
        portalUser.setUserType(userType);
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.translate(portalUser, PortalUserReadDTO.class);
    }

    public PortalUserReadDTO patchPortalUser(UUID id, PortalUserPatchDTO patch) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, id);

        translationService.map(patch, portalUser);

        portalUser = portalUserRepository.save(portalUser);

        return translationService.translate(portalUser, PortalUserReadDTO.class);
    }

    public void deletePortalUser(UUID id) {
        portalUserRepository.delete(repositoryHelper.getByIdRequired(PortalUser.class, id));
    }

    public PortalUserReadDTO updatePortalUser(UUID id, PortalUserPutDTO put) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, id);

        translationService.updateEntity(put, portalUser);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.translate(portalUser, PortalUserReadDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePortalUserConfidence(UUID id, UserConfidenceType userConfidenceType) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, id);
        portalUser.setUserConfidence(userConfidenceType);
        portalUserRepository.save(portalUser);
    }
}
