package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewRepository;

import java.util.List;
import java.util.UUID;

public class PortalUserRoleReviewServiceTest extends BaseTest {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private PortalUserRoleReviewService portalUserRoleReviewService;

    @Test
    public void testGetRoleReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        List<RoleReviewReadDTO> readDTO = portalUserRoleReviewService.getPortalUserRoleReview(portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(roleReview.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewWrongId() {
        portalUserRoleReviewService.getPortalUserRoleReview(UUID.randomUUID());
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

        RoleReviewReadDTO read = portalUserRoleReviewService.createPortalUserRoleReview(portalUser.getId(), create);
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
        RoleReviewReadDTO read = portalUserRoleReviewService.patchPortalUserRoleReview(portalUser.getId(),
                roleReview.getId(),
                patch);

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
        RoleReviewReadDTO read = portalUserRoleReviewService.patchPortalUserRoleReview(portalUser.getId(),
                roleReview.getId(),
                patch);

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

        portalUserRoleReviewService.deletePortalUserRoleReview(portalUser.getId(), roleReview.getId());
        Assert.assertFalse(roleReviewRepository.existsById(roleReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewNotFound() {
        portalUserRoleReviewService.deletePortalUserRoleReview(UUID.randomUUID(), UUID.randomUUID());
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
        RoleReviewReadDTO read = portalUserRoleReviewService.updatePortalUserRoleReview(portalUser.getId(),
                roleReview.getId()
                , put);

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
        RoleReviewReadDTO read = portalUserRoleReviewService.updatePortalUserRoleReview(portalUser.getId(),
                roleReview.getId()
                , put);

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
