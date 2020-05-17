package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;
import solvve.course.dto.UserRoleReadDTO;
import solvve.course.repository.UserRoleRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UserRoleService extends AbstractService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public UserRoleReadDTO getUserRoles(UUID id) {
        UserRole userRole = repositoryHelper.getByIdRequired(UserRole.class, id);
        return translationService.translate(userRole, UserRoleReadDTO.class);
    }

    @Transactional(readOnly = true)
    public List<UserRoleReadDTO> getUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAllUserRoles();
        return translationService.translateList(userRoles, UserRoleReadDTO.class);
    }

    @Transactional(readOnly = true)
    public UserRoleReadDTO getUserRolesByUserGroupType(UserGroupType userGroupType) {
        UserRole userRole = userRoleRepository.findByUserGroupType(userGroupType);
        return translationService.translate(userRole, UserRoleReadDTO.class);
    }
}
