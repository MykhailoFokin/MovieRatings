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
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role_review_feedback; delete from role_review; delete from portal_user; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    private PortalUserReadDTO portalUserReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    private RoleReadDTO roleReadDTO;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewService roleReviewService;

    private RoleReviewReadDTO roleReviewReadDTO;

    private RoleReviewFeedback createRoleReviewFeedback() {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(portalUserReadDTO.getId());
        roleReviewFeedback.setRoleId(roleReadDTO.getId());
        roleReviewFeedback.setRoleReviewId(roleReviewReadDTO.getId());
        roleReviewFeedback.setIsLiked(true);
        return roleReviewFeedbackRepository.save(roleReviewFeedback);
    }

    @Before
    public void setup() {
        if (roleReadDTO==null) {
            Role role = new Role();
            //role.setId(UUID.randomUUID());
            role.setTitle("Role Test");
            role.setTitle("Actor");
            role.setRoleType("Main_Role");
            role.setDescription("Description test");
            role = roleRepository.save(role);
            //RoleReadDTO readDTO = roleService.getRole(role.getId());
            roleReadDTO = roleService.getRole(role.getId());
        }

        if (portalUserReadDTO ==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            PortalUser portalUser = new PortalUser();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userTypes.getId());
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
            portalUserReadDTO = portalUserService.getPortalUser(portalUser.getId());
        }

        if (roleReviewReadDTO ==null) {
            RoleReview roleReview = new RoleReview();
            roleReview.setId(UUID.randomUUID());
            roleReview.setUserId(portalUserReadDTO.getId());
            roleReview.setRoleId(roleReadDTO.getId());
            roleReview.setTextReview("This role can be described as junk.");
            roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            roleReview.setModeratorId(portalUserReadDTO.getId());
            roleReview = roleReviewRepository.save(roleReview);
            roleReviewReadDTO = roleReviewService.getRoleReview(roleReview.getId());
        }
    }

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

    @Test
    public void testCreateRoleReviewFeedback() {
        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setRoleId(roleReadDTO.getId());
        create.setRoleReviewId(roleReviewReadDTO.getId());
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.createRoleReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewFeedback roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReviewFeedback);
    }

    @Test
    public void testPatchRoleReviewFeedback() {
        RoleReviewFeedback roleReviewFeedback = createRoleReviewFeedback();

        RoleReviewFeedbackPatchDTO patch = new RoleReviewFeedbackPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setRoleId(roleReadDTO.getId());
        patch.setRoleReviewId(roleReviewReadDTO.getId());
        patch.setIsLiked(true);
        RoleReviewFeedbackReadDTO read = roleReviewFeedbackService.patchRoleReviewFeedback(roleReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewFeedback = roleReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewFeedback).isEqualToComparingFieldByField(read);
    }

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
