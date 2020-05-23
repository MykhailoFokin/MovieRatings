package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Role;
import solvve.course.dto.RoleReadDTO;
import solvve.course.repository.RoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonRoleService extends AbstractService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleReadDTO> getRolesByPerson(UUID personId) {
        List<Role> roles = roleRepository.findByPersonId(personId);
        return roles.stream().map(e ->
                translationService.translate(e, RoleReadDTO.class)).collect(Collectors.toList());
    }
}
