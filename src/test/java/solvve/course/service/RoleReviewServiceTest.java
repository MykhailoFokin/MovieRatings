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
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.RoleRepository;
import solvve.course.repository.RoleReviewRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role_review; delete from portal_user; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewServiceTest {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewService roleReviewService;

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

    private RoleReview createRoleReview() {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(portalUserReadDTO.getId());
        roleReview.setRoleId(roleReadDTO.getId());
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReview.setModeratorId(portalUserReadDTO.getId());
        return roleReviewRepository.save(roleReview);
    }

    @Before
    public void setup() {
        if (roleReadDTO==null) {
            Role role = new Role();
            //role.setId(UUID.randomUUID());
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
    }

    @Test
    public void testGetRoleReview() {
        RoleReview roleReview = createRoleReview();

        RoleReviewReadDTO readDTO = roleReviewService.getRoleReview(roleReview.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleReview);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewWrongId() {
        roleReviewService.getRoleReview(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReview() {
        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setRoleId(roleReadDTO.getId());
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUserReadDTO.getId());

        RoleReviewReadDTO read = roleReviewService.createRoleReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReview roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReview);
    }

    @Test
    public void testPatchRoleReview() {
        RoleReview roleReview = createRoleReview();

        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setRoleId(roleReadDTO.getId());
        patch.setTextReview("This role can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUserReadDTO.getId());
        RoleReviewReadDTO read = roleReviewService.patchRoleReview(roleReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewEmptyPatch() {
        RoleReview roleReview = createRoleReview();

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

        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(roleReviewAfterUpdate);
    }

    @Test
    public void testDeleteRoleReview() {
        RoleReview roleReview = createRoleReview();

        roleReviewService.deleteRoleReview(roleReview.getId());
        Assert.assertFalse(roleReviewRepository.existsById(roleReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewNotFound() {
        roleReviewService.deleteRoleReview(UUID.randomUUID());
    }
}
