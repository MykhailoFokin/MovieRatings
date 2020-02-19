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
@Sql(statements = {"delete from role_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewServiceTest {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewService roleReviewService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewReadDTO readDTO = roleReviewService.getRoleReview(roleReview.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleReview,
                "userId", "roleId", "moderatorId");
        Assertions.assertThat(readDTO.getUserId())
                .isEqualToComparingFieldByField(roleReview.getUserId().getId());
        Assertions.assertThat(readDTO.getRoleId())
                .isEqualToComparingFieldByField(roleReview.getRoleId().getId());
        Assertions.assertThat(readDTO.getModeratorId())
                .isEqualToComparingFieldByField(roleReview.getModeratorId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewWrongId() {
        roleReviewService.getRoleReview(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);

        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        create.setUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        RoleReviewReadDTO read = roleReviewService.createRoleReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReview roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReview,
                "userId", "roleId", "moderatorId");
        Assertions.assertThat(read.getUserId())
                .isEqualToComparingFieldByField(roleReview.getUserId().getId());
        Assertions.assertThat(read.getRoleId())
                .isEqualToComparingFieldByField(roleReview.getRoleId().getId());
        Assertions.assertThat(read.getModeratorId())
                .isEqualToComparingFieldByField(roleReview.getModeratorId().getId());
    }

    @Test
    public void testPatchRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setTextReview("This role can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        RoleReviewReadDTO read = roleReviewService.patchRoleReview(roleReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(read,
                "userId", "roleId", "moderatorId",
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData");
        Assertions.assertThat(roleReview.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleReview.getRoleId().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReview.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPatchRoleReviewEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        RoleReviewReadDTO read = roleReviewService.patchRoleReview(roleReview.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getTextReview());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        RoleReview roleReviewAfterUpdate = roleReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewAfterUpdate.getUserId());
        Assert.assertNotNull(roleReviewAfterUpdate.getRoleId());
        Assert.assertNotNull(roleReviewAfterUpdate.getTextReview());
        Assert.assertNotNull(roleReviewAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(roleReviewAfterUpdate.getModeratorId());

        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(roleReviewAfterUpdate,
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData",
                "userId", "roleId", "moderatorId");
        Assertions.assertThat(roleReview.getUserId().getId()).isEqualTo(roleReviewAfterUpdate.getUserId().getId());
        Assertions.assertThat(roleReview.getRoleId().getId()).isEqualTo(roleReviewAfterUpdate.getRoleId().getId());
        Assertions.assertThat(roleReview.getModeratorId().getId())
                .isEqualTo(roleReviewAfterUpdate.getModeratorId().getId());
    }

    @Test
    public void testDeleteRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        roleReviewService.deleteRoleReview(roleReview.getId());
        Assert.assertFalse(roleReviewRepository.existsById(roleReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewNotFound() {
        roleReviewService.deleteRoleReview(UUID.randomUUID());
    }

    @Test
    public void testPutRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPutDTO put = new RoleReviewPutDTO();
        put.setUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setTextReview("This role can be described as junk.");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        RoleReviewReadDTO read = roleReviewService.updateRoleReview(roleReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(read,
                "userId", "roleId", "moderatorId",
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData");
        Assertions.assertThat(roleReview.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleReview.getRoleId().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReview.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Transactional
    @Test
    public void testPutRoleReviewEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPutDTO put = new RoleReviewPutDTO();
        RoleReviewReadDTO read = roleReviewService.updateRoleReview(roleReview.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getTextReview());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        RoleReview roleReviewAfterUpdate = roleReviewRepository.findById(read.getId()).get();

        Assert.assertNull(roleReviewAfterUpdate.getUserId().getId());
        Assert.assertNull(roleReviewAfterUpdate.getRoleId().getId());
        Assert.assertNull(roleReviewAfterUpdate.getTextReview());
        Assert.assertNull(roleReviewAfterUpdate.getModeratedStatus());
        Assert.assertNull(roleReviewAfterUpdate.getModeratorId().getId());

        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(roleReviewAfterUpdate);
    }
}
