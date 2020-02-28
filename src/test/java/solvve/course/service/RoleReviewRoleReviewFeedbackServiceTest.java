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
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewFeedbackRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_review_feedback",
        " delete from role_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewRoleReviewFeedbackServiceTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewRoleReviewFeedbackService roleReviewRoleReviewFeedbackService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback =
                testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        List<RoleReviewFeedbackReadDTO> readDTO =
                roleReviewRoleReviewFeedbackService.getRoleReviewRoleReviewFeedback(roleReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(roleReviewFeedback.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewFeedbackWrongId() {
        roleReviewRoleReviewFeedbackService.getRoleReviewRoleReviewFeedback(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRoleReviewId(roleReview.getId());
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read =
                roleReviewRoleReviewFeedbackService.createRoleReviewRoleReviewFeedback(roleReview.getId(), create);
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

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setRoleReviewId(roleReview.getId());
        patch.setIsLiked(true);
        RoleReviewFeedbackReadDTO read =
                roleReviewRoleReviewFeedbackService.patchRoleReviewRoleReviewFeedback(roleReview.getId(),
                        roleReviewFeedback.getId(),
                        patch);

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
                roleReviewRoleReviewFeedbackService.patchRoleReviewRoleReviewFeedback(roleReview.getId(),
                        roleReviewFeedback.getId(),
                        patch);

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

        roleReviewRoleReviewFeedbackService.deleteRoleReviewRoleReviewFeedback(roleReview.getId(),
                roleReviewFeedback.getId());
        Assert.assertFalse(roleReviewFeedbackRepository.existsById(roleReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewFeedbackNotFound() {
        roleReviewRoleReviewFeedbackService.deleteRoleReviewRoleReviewFeedback(UUID.randomUUID(), UUID.randomUUID());
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

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setRoleReviewId(roleReview.getId());
        put.setIsLiked(true);
        RoleReviewFeedbackReadDTO read =
                roleReviewRoleReviewFeedbackService.updateRoleReviewRoleReviewFeedback(roleReview.getId(),
                        roleReviewFeedback.getId(),
                        put);

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
                roleReviewRoleReviewFeedbackService.updateRoleReviewRoleReviewFeedback(roleReview.getId(),
                        roleReviewFeedback.getId(),
                        put);

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
