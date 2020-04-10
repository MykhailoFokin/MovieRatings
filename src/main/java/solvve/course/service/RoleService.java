package solvve.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Role;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleRepository;
import solvve.course.repository.RoleVoteRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RoleService extends AbstractService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Transactional(readOnly = true)
    public RoleReadDTO getRole(UUID id) {
        Role role = repositoryHelper.getByIdRequired(Role.class, id);
        return translationService.translate(role, RoleReadDTO.class);
    }

    public RoleReadDTO createRole(RoleCreateDTO create) {
        Role role = translationService.translate(create, Role.class);

        role = roleRepository.save(role);
        return translationService.translate(role, RoleReadDTO.class);
    }

    public RoleReadDTO patchRole(UUID id, RolePatchDTO patch) {
        Role role = repositoryHelper.getByIdRequired(Role.class, id);

        translationService.map(patch, role);

        role = roleRepository.save(role);
        return translationService.translate(role, RoleReadDTO.class);
    }

    public void deleteRole(UUID id) {
        roleRepository.delete(repositoryHelper.getByIdRequired(Role.class, id));
    }

    public RoleReadDTO updateRole(UUID id, RolePutDTO put) {
        Role role = repositoryHelper.getByIdRequired(Role.class, id);

        translationService.updateEntity(put, role);

        role = roleRepository.save(role);
        return translationService.translate(role, RoleReadDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfRole(UUID roleId) {
        Double averageRating = roleVoteRepository.calcAverageMarkOfRole(roleId);
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new EntityNotFoundException(Role.class, roleId));

        log.info("Setting new average rating of role: {}. Old value: {}, new value: {}", roleId,
                role.getAverageRating(), averageRating);
        role.setAverageRating(averageRating);
        roleRepository.save(role);
    }

    public List<RoleInLeaderBoardReadDTO> getRolesLeaderBoard() {
        return roleRepository.getRolesLeaderBoard();
    }
}
