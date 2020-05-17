package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserRole;
import solvve.course.dto.PortalUserUserRoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortalUserUserRoleService extends AbstractService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    public List<PortalUserUserRoleReadDTO> getPortalUserUserRoles(UUID portalUserId) {
        List<UserRole> userRoles = getPortalUserRolesByPortalUserIdRequired(portalUserId);
        return translationService.translateList(userRoles, PortalUserUserRoleReadDTO.class);
    }

    @Transactional
    public List<PortalUserUserRoleReadDTO> addUserRoleToPortalUser(UUID portalUserId, UUID id) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, portalUserId);

        UserRole userRole = repositoryHelper.getByIdRequired(UserRole.class, id);

        if (portalUser.getUserRoles().stream().anyMatch(ur -> ur.getId().equals(id))) {
            throw new LinkDuplicatedException(String.format("PortalUser %s already has UserRole %s", portalUserId, id));
        }

        portalUser.getUserRoles().add(userRole);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser.getUserRoles().stream()
                .map(ur -> translationService.translate(ur, PortalUserUserRoleReadDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PortalUserUserRoleReadDTO> removeUserRoleFromPortalUser(UUID portalUserId, UUID id) {
        PortalUser portalUser = repositoryHelper.getByIdRequired(PortalUser.class, portalUserId);

        boolean removed = portalUser.getUserRoles().removeIf(l -> l.getId().equals(id));
        if (!removed) {
            throw new EntityNotFoundException("PortalUser " + portalUserId + " has no UserRole " + id);
        }

        portalUser = portalUserRepository.save(portalUser);

        return translationService.translateList(portalUser.getUserRoles(), PortalUserUserRoleReadDTO.class);
    }

    private List<UserRole> getPortalUserRolesByPortalUserIdRequired(UUID portalUserId) {
        return userRoleRepository.findUserRolesByPortalUserId(portalUserId).orElseThrow(() -> {
            throw new EntityNotFoundException("PortalUser " + portalUserId + " has no UserRoles");
        });
    }
}
