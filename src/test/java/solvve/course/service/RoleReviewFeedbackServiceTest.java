package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role_review_feedback; delete from role_review; delete from portal_user; delete from user_type; delete from role; delete from person;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewFeedbackServiceTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedbackService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private PersonRepository personRepository;

    private RoleReviewFeedback createRoleReviewFeedback(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(portalUser);
        roleReviewFeedback.setRoleId(role);
        roleReviewFeedback.setRoleReviewId(roleReview);
        roleReviewFeedback.setIsLiked(true);
        return roleReviewFeedbackRepository.save(roleReviewFeedback);
    }

    public Role createRole() {
        Person person = new Person();
        person.setName("Name");
        person = personRepository.save(person);

        Role role = new Role();
        //role.setId(UUID.randomUUID());
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");
        role.setPersonId(person);
        role = roleRepository.save(role);

        return role;
    }

    public PortalUser createPortalUser() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setLogin("Login");
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setUserType(userType);
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    public RoleReview createRoleReview(PortalUser portalUser, Role role) {
        RoleReview roleReview = new RoleReview();
        roleReview.setId(UUID.randomUUID());
        roleReview.setUserId(portalUser);
        roleReview.setRoleId(role);
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReview.setModeratorId(portalUser);
        roleReview = roleReviewRepository.save(roleReview);
        return roleReview;
    }

    @Transactional
    @Test
    public void testGetRoleReviewFeedback() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackReadDTO readDTO = roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleReviewFeedback);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewFeedbackWrongId() {
        roleReviewFeedbackService.getRoleReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleReviewFeedback() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setUserId(portalUser);
        create.setRoleId(role);
        create.setRoleReviewId(roleReview);
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.createRoleReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewFeedback roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReviewFeedback);
    }

    @Transactional
    @Test
    public void testPatchRoleReviewFeedback() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        patch.setUserId(portalUser);
        patch.setRoleId(role);
        patch.setRoleReviewId(roleReview);
        patch.setIsLiked(true);
        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchRoleReviewFeedbackEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate = roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(roleReviewFeedbackAfterUpdate);
    }

    @Test
    public void testDeleteRoleReviewFeedback() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

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
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        put.setUserId(portalUser);
        put.setRoleId(role);
        put.setRoleReviewId(roleReview);
        put.setIsLiked(true);
        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.putRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPutRoleReviewFeedbackEmptyPut() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback(portalUser, role, roleReview);

        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();
        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.putRoleReviewFeedback(roleReviewFeedback.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getRoleReviewId());
        Assert.assertNull(read.getIsLiked());

        RoleReviewFeedback roleReviewFeedbackAfterUpdate = roleReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNull(roleReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getRoleId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getRoleReviewId());
        Assert.assertNull(roleReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(roleReviewFeedbackAfterUpdate);
    }
}
