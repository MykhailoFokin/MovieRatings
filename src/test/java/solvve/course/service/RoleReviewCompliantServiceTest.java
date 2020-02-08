package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_review_compliant",
        "delete from role_review",
        "delete from portal_user",
        "delete from user_type",
        "delete from role",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewCompliantServiceTest {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleReviewCompliantService roleReviewCompliantService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantReadDTO readDTO =
                roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "userId", "roleId", "roleReviewId", "moderatorId");
        Assertions.assertThat(readDTO.getUserId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getUserId().getId());
        Assertions.assertThat(readDTO.getRoleId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRoleId().getId());
        Assertions.assertThat(readDTO.getRoleReviewId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRoleReviewId().getId());
        Assertions.assertThat(readDTO.getModeratorId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getModeratorId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewCompliantWrongId() {
        roleReviewCompliantService.getRoleReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRoleReviewId(roleReview.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        RoleReviewCompliantReadDTO read = roleReviewCompliantService.createRoleReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewCompliant roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "userId", "roleId", "roleReviewId", "moderatorId");
        Assertions.assertThat(read.getUserId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getUserId().getId());
        Assertions.assertThat(read.getRoleId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRoleId().getId());
        Assertions.assertThat(read.getRoleReviewId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRoleReviewId().getId());
        Assertions.assertThat(read.getModeratorId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getModeratorId().getId());
    }

    @Transactional
    @Test
    public void testPatchRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setRoleReviewId(roleReview.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        RoleReviewCompliantReadDTO read =
                roleReviewCompliantService.patchRoleReviewCompliant(roleReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "userId", "roleId", "roleReviewId", "moderatorId");
        Assertions.assertThat(roleReviewCompliant.getUserId().getId())
                .isEqualToComparingFieldByField(read.getUserId());
        Assertions.assertThat(roleReviewCompliant.getRoleId().getId())
                .isEqualToComparingFieldByField(read.getRoleId());
        Assertions.assertThat(roleReviewCompliant.getRoleReviewId().getId())
                .isEqualToComparingFieldByField(read.getRoleReviewId());
        Assertions.assertThat(roleReviewCompliant.getModeratorId().getId())
                .isEqualToComparingFieldByField(read.getModeratorId());
    }

    @Transactional
    @Test
    public void testPatchRoleReviewCompliantEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        RoleReviewCompliantReadDTO read =
                roleReviewCompliantService.patchRoleReviewCompliant(roleReviewCompliant.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        RoleReviewCompliant roleReviewCompliantAfterUpdate =
                roleReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getUserId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getDescription());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModeratorId());

        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(roleReviewCompliantAfterUpdate);
    }

    @Test
    public void testDeleteRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        roleReviewCompliantService.deleteRoleReviewCompliant(roleReviewCompliant.getId());
        Assert.assertFalse(roleReviewCompliantRepository.existsById(roleReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewCompliantNotFound() {
        roleReviewCompliantService.deleteRoleReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();
        put.setUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setRoleReviewId(roleReview.getId());
        put.setDescription("Just punish him!");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        RoleReviewCompliantReadDTO read =
                roleReviewCompliantService.putRoleReviewCompliant(roleReviewCompliant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "userId", "roleId", "roleReviewId", "moderatorId");
        Assertions.assertThat(roleReviewCompliant.getUserId().getId())
                .isEqualToComparingFieldByField(read.getUserId());
        Assertions.assertThat(roleReviewCompliant.getRoleId().getId())
                .isEqualToComparingFieldByField(read.getRoleId());
        Assertions.assertThat(roleReviewCompliant.getRoleReviewId().getId())
                .isEqualToComparingFieldByField(read.getRoleReviewId());
        Assertions.assertThat(roleReviewCompliant.getModeratorId().getId())
                .isEqualToComparingFieldByField(read.getModeratorId());
    }

    @Transactional
    @Test
    public void testPutRoleReviewCompliantEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();
        RoleReviewCompliantReadDTO read =
                roleReviewCompliantService.putRoleReviewCompliant(roleReviewCompliant.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getRoleReviewId());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        RoleReviewCompliant roleReviewCompliantAfterUpdate =
                roleReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNull(roleReviewCompliantAfterUpdate.getUserId().getId());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getRoleId().getId());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getRoleReviewId().getId());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getDescription());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getModeratorId().getId());

        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(roleReviewCompliantAfterUpdate);
    }
}
