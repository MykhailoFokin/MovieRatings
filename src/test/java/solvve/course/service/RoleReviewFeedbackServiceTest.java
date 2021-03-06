package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

public class RoleReviewFeedbackServiceTest extends BaseTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedbackService;

    @Test
    public void testGetRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackReadDTO readDTO =
                roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleReviewFeedback,
                "portalUserId", "roleId", "roleReviewId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(roleReviewFeedback.getPortalUser().getId());
        Assertions.assertThat(readDTO.getRoleId()).isEqualTo(roleReviewFeedback.getRole().getId());
        Assertions.assertThat(readDTO.getRoleReviewId()).isEqualTo(roleReviewFeedback.getRoleReview().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewFeedbackWrongId() {
        roleReviewFeedbackService.getRoleReviewFeedback(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewFeedbackCreateDTO create = testObjectsFactory.createRoleReviewFeedbackCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRoleReviewId(roleReview.getId());

        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.createRoleReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewFeedback roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReviewFeedback,
                "portalUserId", "roleId", "roleReviewId");
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(roleReviewFeedback.getPortalUser().getId());
        Assertions.assertThat(read.getRoleId()).isEqualTo(roleReviewFeedback.getRole().getId());
        Assertions.assertThat(read.getRoleReviewId()).isEqualTo(roleReviewFeedback.getRoleReview().getId());
    }

    @Test
    public void testPatchRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = testObjectsFactory.createRoleReviewFeedbackPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setRoleReviewId(roleReview.getId());
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "role", "roleReview");
        Assertions.assertThat(roleReviewFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleReviewFeedback.getRole().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReviewFeedback.getRoleReview().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Test
    public void testPatchRoleReviewFeedbackEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate =
                roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRole());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleReview());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(roleReviewFeedback).isEqualToIgnoringGivenFields(roleReviewFeedbackAfterUpdate,
                "portalUser", "role", "roleReview");
        Assertions.assertThat(roleReviewFeedback.getPortalUser().getId())
                .isEqualTo(roleReviewFeedbackAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(roleReviewFeedback.getRole().getId())
                .isEqualTo(roleReviewFeedbackAfterUpdate.getRole().getId());
        Assertions.assertThat(roleReviewFeedback.getRoleReview().getId())
                .isEqualTo(roleReviewFeedbackAfterUpdate.getRoleReview().getId());
    }

    @Test
    public void testDeleteRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        roleReviewFeedbackService.deleteRoleReviewFeedback(roleReviewFeedback.getId());
        Assert.assertFalse(roleReviewFeedbackRepository.existsById(roleReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewFeedbackNotFound() {
        roleReviewFeedbackService.deleteRoleReviewFeedback(UUID.randomUUID());
    }

    @Test
    public void testPutRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = testObjectsFactory.createRoleReviewFeedbackPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setRoleReviewId(roleReview.getId());
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.updateRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "role", "roleReview");
        Assertions.assertThat(roleReviewFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleReviewFeedback.getRole().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReviewFeedback.getRoleReview().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Test
    public void testPutRoleReviewFeedbackEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.updateRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate = roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getPortalUser().getId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRole().getId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleReview().getId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getIsLiked());
    }
}
