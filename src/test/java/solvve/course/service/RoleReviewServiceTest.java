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
                "portalUserId", "roleId", "moderatorId");
        Assertions.assertThat(readDTO.getPortalUserId())
                .isEqualToComparingFieldByField(roleReview.getPortalUser().getId());
        Assertions.assertThat(readDTO.getRoleId())
                .isEqualToComparingFieldByField(roleReview.getRole().getId());
        Assertions.assertThat(readDTO.getModeratorId())
                .isEqualToComparingFieldByField(roleReview.getModerator().getId());
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
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        RoleReviewReadDTO read = roleReviewService.createRoleReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReview roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReview,
                "portalUserId", "roleId", "moderatorId");
        Assertions.assertThat(read.getPortalUserId())
                .isEqualToComparingFieldByField(roleReview.getPortalUser().getId());
        Assertions.assertThat(read.getRoleId())
                .isEqualToComparingFieldByField(roleReview.getRole().getId());
        Assertions.assertThat(read.getModeratorId())
                .isEqualToComparingFieldByField(roleReview.getModerator().getId());
    }

    @Test
    public void testPatchRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setTextReview("This role can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        RoleReviewReadDTO read = roleReviewService.patchRoleReview(roleReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(read,
                "portalUser", "role", "moderator",
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData");
        Assertions.assertThat(roleReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleReview.getRole().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReview.getModerator().getId()).isEqualTo(read.getModeratorId());
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

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getTextReview());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        RoleReview roleReviewAfterUpdate = roleReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleReviewAfterUpdate.getRole());
        Assert.assertNotNull(roleReviewAfterUpdate.getTextReview());
        Assert.assertNotNull(roleReviewAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(roleReviewAfterUpdate.getModerator());

        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(roleReviewAfterUpdate,
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData",
                "portalUser", "role", "moderator");
        Assertions.assertThat(roleReview.getPortalUser().getId())
                .isEqualTo(roleReviewAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(roleReview.getRole().getId()).isEqualTo(roleReviewAfterUpdate.getRole().getId());
        Assertions.assertThat(roleReview.getModerator().getId())
                .isEqualTo(roleReviewAfterUpdate.getModerator().getId());
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
        put.setPortalUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setTextReview("This role can be described as junk.");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        RoleReviewReadDTO read = roleReviewService.updateRoleReview(roleReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToIgnoringGivenFields(read,
                "portalUser", "role", "moderator",
                "roleReviewCompliants","roleReviewFeedbacks","roleSpoilerData");
        Assertions.assertThat(roleReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleReview.getRole().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReview.getModerator().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPutRoleReviewEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewPutDTO put = new RoleReviewPutDTO();
        RoleReviewReadDTO read = roleReviewService.updateRoleReview(roleReview.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNull(read.getTextReview());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        RoleReview roleReviewAfterUpdate = roleReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleReviewAfterUpdate.getRole());
        Assert.assertNull(roleReviewAfterUpdate.getTextReview());
        Assert.assertNull(roleReviewAfterUpdate.getModeratedStatus());
        Assert.assertNull(roleReviewAfterUpdate.getModerator());

        Assertions.assertThat(roleReview).isEqualToComparingOnlyGivenFields(roleReviewAfterUpdate,"id");
        Assertions.assertThat(roleReview.getPortalUser().getId())
                .isEqualTo(roleReviewAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(roleReview.getRole().getId())
                .isEqualTo(roleReviewAfterUpdate.getRole().getId());
    }
}
