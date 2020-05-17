package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;
import solvve.course.dto.PortalUserUserRoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;

import java.util.List;
import java.util.UUID;

public class PortalUserUserRoleServiceTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PortalUserUserRoleService portalUserUserRoleService;

    @Test
    public void testAddUserRoleToPortalUser() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UUID userRoleId = testObjectsFactory.createUserRole(UserGroupType.USER).getId();

        List<PortalUserUserRoleReadDTO> res = portalUserUserRoleService.addUserRoleToPortalUser(portalUser.getId(),
                userRoleId);

        PortalUserUserRoleReadDTO expectedRead = new PortalUserUserRoleReadDTO();
        expectedRead.setId(userRoleId);
        expectedRead.setUserGroupType(UserGroupType.USER);
        Assertions.assertThat(res).containsExactlyInAnyOrder(expectedRead);

        testObjectsFactory.inTransaction(() -> {
            PortalUser portalUserAfterSave = portalUserRepository.findById(portalUser.getId()).get();
            Assertions.assertThat(portalUserAfterSave.getUserRoles()).extracting(UserRole::getId)
                    .containsExactlyInAnyOrder(userRoleId);
        });
    }

    @Test
    public void testDuplicatedUserRole() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UUID userRoleId = testObjectsFactory.createUserRole(UserGroupType.USER).getId();

        portalUserUserRoleService.addUserRoleToPortalUser(portalUser.getId(), userRoleId);
        Assertions.assertThatThrownBy(() -> {
            portalUserUserRoleService.addUserRoleToPortalUser(portalUser.getId(), userRoleId);
        }).isInstanceOf(LinkDuplicatedException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongPortalUserId() {
        UUID wrongPortalUserId = UUID.randomUUID();
        UUID userRoleId = testObjectsFactory.createUserRole(UserGroupType.USER).getId();
        portalUserUserRoleService.addUserRoleToPortalUser(wrongPortalUserId, userRoleId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongUserRoleId() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UUID userRoleId = UUID.randomUUID();
        portalUserUserRoleService.addUserRoleToPortalUser(portalUser.getId(), userRoleId);
    }

    @Test
    public void testRemoveUserRoleFromPortalUser() {
        UUID userRoleId1 = testObjectsFactory.createUserRole(UserGroupType.USER).getId();
        UUID userRoleId2 = testObjectsFactory.createUserRole(UserGroupType.ADMIN).getId();
        PortalUser portalUser = testObjectsFactory.createPortalUserWithUserRoles(List.of(userRoleId1, userRoleId2));

        portalUserUserRoleService.removeUserRoleFromPortalUser(portalUser.getId(), userRoleId1);

        testObjectsFactory.inTransaction(() -> {
            PortalUser portalUserAfterRemove = portalUserRepository.findById(portalUser.getId()).get();
            Assertions.assertThat(portalUserAfterRemove.getUserRoles()).extracting(UserRole::getId)
                    .containsExactlyInAnyOrder(userRoleId2);
        });
    }

    @Test
    public void testAddAndThenRemoveUserRoleFromPortalUser() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UUID userRoleId = testObjectsFactory.createUserRole(UserGroupType.USER).getId();
        portalUserUserRoleService.addUserRoleToPortalUser(portalUser.getId(), userRoleId);

        List<PortalUserUserRoleReadDTO> remainingCountries =
                portalUserUserRoleService.removeUserRoleFromPortalUser(portalUser.getId(),
                userRoleId);
        Assert.assertTrue(remainingCountries.isEmpty());

        testObjectsFactory.inTransaction(() -> {
            PortalUser portalUserAfterRemove = portalUserRepository.findById(portalUser.getId()).get();
            Assert.assertTrue(portalUserAfterRemove.getUserRoles().isEmpty());
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotAddedUserRole() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UUID userRoleId = testObjectsFactory.createUserRole(UserGroupType.USER).getId();

        portalUserUserRoleService.removeUserRoleFromPortalUser(portalUser.getId(), userRoleId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotExistedUserRole() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        portalUserUserRoleService.removeUserRoleFromPortalUser(portalUser.getId(), UUID.randomUUID());
    }

    @Test
    public void testGetPortalUserCountries() {
        UUID userRoleId1 = testObjectsFactory.createUserRole(UserGroupType.USER).getId();
        UUID userRoleId2 = testObjectsFactory.createUserRole(UserGroupType.USER).getId();
        PortalUser portalUser = testObjectsFactory.createPortalUserWithUserRoles(List.of(userRoleId1, userRoleId2));

        Assertions.assertThat(portalUserUserRoleService.getPortalUserUserRoles(portalUser.getId()))
                .extracting(PortalUserUserRoleReadDTO::getId)
                .containsExactlyInAnyOrder(userRoleId1, userRoleId2);
    }
}
