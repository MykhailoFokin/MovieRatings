package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.domain.Role;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleRepository;

import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleReadDTO getRole(UUID id) {
        Role role = getRoleRequired(id);
        return translationService.toRead(role);
    }

    public RoleReadDTO createRole(RoleCreateDTO create) {
        Role role = translationService.toEntity(create);

        role = roleRepository.save(role);
        return translationService.toRead(role);
    }

    public RoleReadDTO patchRole(UUID id, RolePatchDTO patch) {
        Role role = getRoleRequired(id);

        translationService.patchEntity(patch, role);

        role = roleRepository.save(role);
        return translationService.toRead(role);
    }

    private Role getRoleRequired(UUID id) {
        return roleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Role.class, id);
        });
    }

    public void deleteRole(UUID id) {
        roleRepository.delete(getRoleRequired(id));
    }

    public RoleReadDTO updateRole(UUID id, RolePutDTO put) {
        Role role = getRoleRequired(id);

        translationService.updateEntity(put, role);

        role = roleRepository.save(role);
        return translationService.toRead(role);
    }
}
