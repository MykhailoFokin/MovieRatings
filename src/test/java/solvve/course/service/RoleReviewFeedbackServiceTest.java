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
@Sql(statements = "delete from role_review_feedback; delete from role_review; delete from portal_user; delete from user_types; delete from role; delete from persons;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewFeedbackServiceTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedbackService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewService roleReviewService;

    @Autowired
    private PersonsRepository personsRepository;

    private RoleReview roleReview;

    private PortalUser portalUser;

    private Role role;

    private RoleReviewFeedback createRoleReviewFeedback() {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(portalUser);
        roleReviewFeedback.setRoleId(role);
        roleReviewFeedback.setRoleReviewId(roleReview);
        roleReviewFeedback.setIsLiked(true);
        return roleReviewFeedbackRepository.save(roleReviewFeedback);
    }

    @Before
    public void setup() {
        if (role==null) {
            Persons person = new Persons();
            person.setName("Name");
            person = personsRepository.save(person);

            role = new Role();
            //role.setId(UUID.randomUUID());
            role.setTitle("Role Test");
            role.setTitle("Actor");
            role.setRoleType("Main_Role");
            role.setDescription("Description test");
            role.setPersonId(person);
            role = roleRepository.save(role);
        }

        if (portalUser==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            portalUser = new PortalUser();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userTypes);
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
        }

        if (roleReview==null) {
            roleReview = new RoleReview();
            roleReview.setId(UUID.randomUUID());
            roleReview.setUserId(portalUser);
            roleReview.setRoleId(role);
            roleReview.setTextReview("This role can be described as junk.");
            roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            roleReview.setModeratorId(portalUser);
            roleReview = roleReviewRepository.save(roleReview);
        }
    }

    @Transactional
    @Test
    public void testGetRoleReviewFeedback() {
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback();

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
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback();

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
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback();

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
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback();

        roleReviewFeedbackService.deleteRoleReviewFeedback(roleReviewFeedback.getId());
        Assert.assertFalse(roleReviewFeedbackRepository.existsById(roleReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewFeedbackNotFound() {
        roleReviewFeedbackService.deleteRoleReviewFeedback(UUID.randomUUID());
    }
}
