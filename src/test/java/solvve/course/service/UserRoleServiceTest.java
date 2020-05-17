package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserRoleRepository;

import java.util.UUID;

public class UserRoleServiceTest extends BaseTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void testGetUserRoles() {
        UserRole userRole = userRoleRepository.findByUserGroupType(UserGroupType.USER);

        UserRoleReadDTO readDTO = userRoleService.getUserRoles(userRole.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(userRole,"portalUserId");
    }

    @Test
    public void testGetUserRolesByUserGroupType() {
        UserRole userRole = userRoleRepository.findByUserGroupType(UserGroupType.USER);

        UserRoleReadDTO readDTO = userRoleService.getUserRolesByUserGroupType(UserGroupType.USER);
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(userRole,"portalUserId");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserRolesWrongId() {
        userRoleService.getUserRoles(UUID.randomUUID());
    }
}
