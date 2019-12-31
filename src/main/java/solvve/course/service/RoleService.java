package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.domain.Role;
import solvve.course.dto.RoleCreateDTO;
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
        Role role = roleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Role.class, id);
        });
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

        role = roleRepository.save(role);
        return toRead(role);
    }
}
