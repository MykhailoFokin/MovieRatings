package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Role;
import solvve.course.domain.RoleVote;
import solvve.course.domain.UserConfidenceType;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.RoleVoteRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RolePortalUserRoleVoteService extends AbstractService {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    public List<RoleVoteReadDTO> getRolesVotesByPortalUser(UUID roleId, UUID portalUserId) {
        List<RoleVote> roleVotes = roleVoteRepository.findByRoleIdAndPortalUserId(roleId, portalUserId);
        return roleVotes.stream().map(e ->
                translationService.translate(e, RoleVoteReadDTO.class)).collect(Collectors.toList());
    }

    public RoleVoteReadDTO createRoleVoteByPortalUser(UUID portalUserId, UUID roleId, RoleVoteCreateDTO create) {
        if (portalUserRepository.existsByIdAndUserConfidence(portalUserId, UserConfidenceType.BLOCKED)) {
            throw new AccessDeniedException("You are blocked by moderator!");
        }
        repositoryHelper.validateIFExists(Role.class, roleId);
        RoleVote roleVote = translationService.translate(create, RoleVote.class);
        roleVote = roleVoteRepository.save(roleVote);

        return translationService.translate(roleVote, RoleVoteReadDTO.class);
    }
}
