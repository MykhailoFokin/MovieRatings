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
@Sql(statements = "delete from role_review_compliant; delete from role_review; delete from portal_user; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewCompliantServiceTest {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleReviewCompliantService roleReviewCompliantService;

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

    private RoleReviewCompliant createRoleReviewCompliant() {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(portalUserReadDTO.getId());
        roleReviewCompliant.setRoleId(roleReadDTO.getId());
        roleReviewCompliant.setRoleReviewId(roleReviewReadDTO.getId());
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReviewCompliant.setModeratorId(portalUserReadDTO.getId());
        return roleReviewCompliantRepository.save(roleReviewCompliant);
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
    public void testGetRoleReviewCompliant() {
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant();

        RoleReviewCompliantReadDTO readDTO = roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleReviewCompliant);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewCompliantWrongId() {
        roleReviewCompliantService.getRoleReviewCompliant(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReviewCompliant() {
        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setRoleId(roleReadDTO.getId());
        create.setRoleReviewId(roleReviewReadDTO.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUserReadDTO.getId());

        RoleReviewCompliantReadDTO read = roleReviewCompliantService.createRoleReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewCompliant roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReviewCompliant);
    }

    @Test
    public void testPatchRoleReviewCompliant() {
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant();

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setRoleId(roleReadDTO.getId());
        patch.setRoleReviewId(roleReviewReadDTO.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUserReadDTO.getId());
        RoleReviewCompliantReadDTO read = roleReviewCompliantService.patchRoleReviewCompliant(roleReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCompliantEmptyPatch() {
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant();

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        RoleReviewCompliantReadDTO read = roleReviewCompliantService.patchRoleReviewCompliant(roleReviewCompliant.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        RoleReviewCompliant roleReviewCompliantAfterUpdate = roleReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getUserId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getDescription());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModeratorId());

        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(roleReviewCompliantAfterUpdate);
    }

    @Test
    public void testDeleteRoleReviewCompliant() {
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant();

        roleReviewCompliantService.deleteRoleReviewCompliant(roleReviewCompliant.getId());
        Assert.assertFalse(roleReviewCompliantRepository.existsById(roleReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewCompliantNotFound() {
        roleReviewCompliantService.deleteRoleReviewCompliant(UUID.randomUUID());
    }
}
