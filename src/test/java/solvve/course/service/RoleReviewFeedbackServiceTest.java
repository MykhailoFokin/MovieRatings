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
@Sql(statements = {"delete from role_review_feedback",
        " delete from role_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewFeedbackServiceTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedbackService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackReadDTO readDTO =
                roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleReviewFeedback,
                "userId", "roleId", "roleReviewId");
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(roleReviewFeedback.getUserId().getId());
        Assertions.assertThat(readDTO.getRoleId()).isEqualTo(roleReviewFeedback.getRoleId().getId());
        Assertions.assertThat(readDTO.getRoleReviewId()).isEqualTo(roleReviewFeedback.getRoleReviewId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewFeedbackWrongId() {
        roleReviewFeedbackService.getRoleReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRoleReviewId(roleReview.getId());
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.createRoleReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewFeedback roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReviewFeedback,
                "userId", "roleId", "roleReviewId");
        Assertions.assertThat(read.getUserId()).isEqualTo(roleReviewFeedback.getUserId().getId());
        Assertions.assertThat(read.getRoleId()).isEqualTo(roleReviewFeedback.getRoleId().getId());
        Assertions.assertThat(read.getRoleReviewId()).isEqualTo(roleReviewFeedback.getRoleReviewId().getId());
    }

    @Transactional
    @Test
    public void testPatchRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setRoleReviewId(roleReview.getId());
        patch.setIsLiked(true);
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToIgnoringGivenFields(read,
                "userId", "roleId", "roleReviewId");
        Assertions.assertThat(roleReviewFeedback.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleReviewFeedback.getRoleId().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReviewFeedback.getRoleReviewId().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Transactional
    @Test
    public void testPatchRoleReviewFeedbackEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate =
                roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(roleReviewFeedbackAfterUpdate);
    }

    @Test
    public void testDeleteRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        roleReviewFeedbackService.deleteRoleReviewFeedback(roleReviewFeedback.getId());
        Assert.assertFalse(roleReviewFeedbackRepository.existsById(roleReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewFeedbackNotFound() {
        roleReviewFeedbackService.deleteRoleReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        put.setUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setRoleReviewId(roleReview.getId());
        put.setIsLiked(true);
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.putRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToIgnoringGivenFields(read,
                "userId", "roleId", "roleReviewId");
        Assertions.assertThat(roleReviewFeedback.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleReviewFeedback.getRoleId().getId()).isEqualTo(read.getRoleId());
        Assertions.assertThat(roleReviewFeedback.getRoleReviewId().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Transactional
    @Test
    public void testPutRoleReviewFeedbackEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        RoleReviewFeedbackReadDTO read =
                roleReviewFeedbackService.putRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getRoleReviewId());
        Assert.assertNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate = roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNull(roleReviewFeedbackAfterUpdate.getUserId().getId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getRoleId().getId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getRoleReviewId().getId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(roleReviewFeedbackAfterUpdate);
    }
}
