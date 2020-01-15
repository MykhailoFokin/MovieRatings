package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.domain.Role;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleRepository;

import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public RoleReadDTO getRole(UUID id) {
        Role role = getRoleRequired(id);
        return toRead(role);
    }

    private RoleReadDTO toRead(Role role) {
        RoleReadDTO dto = new RoleReadDTO();
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        dto.setRoleType(role.getRoleType());
        dto.setDescription(role.getDescription());
        return dto;
    }

    public RoleReadDTO createRole(RoleCreateDTO create) {
        Role role = new Role();
        role.setTitle(create.getTitle());
        role.setRoleType(create.getRoleType());
        role.setDescription(create.getDescription());
        role.setPersonId(create.getPersonId());

        role = roleRepository.save(role);
        return toRead(role);
    }

    public RoleReadDTO patchRole(UUID id, RolePatchDTO patch) {
        Role role = getRoleRequired(id);

        if (patch.getTitle()!=null) {
            role.setTitle(patch.getTitle());
        }
        if (patch.getRoleType()!=null) {
            role.setRoleType(patch.getRoleType());
        }
        if (patch.getDescription()!=null) {
            role.setDescription(patch.getDescription());
        }
        if (patch.getPersonId()!=null) {
            role.setPersonId(patch.getPersonId());
        }
        role = roleRepository.save(role);
        return toRead(role);
    }

    private Role getRoleRequired(UUID id) {
        return roleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Role.class, id);
        });
    }

    public void deleteRole(UUID id) {
        roleRepository.delete(getRoleRequired(id));
    }
}
