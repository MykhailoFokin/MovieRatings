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
@Sql(statements = "delete from role_review; delete from portal_user; delete from user_type; delete from role; delete from person;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewServiceTest {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewService roleReviewService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PersonRepository personRepository;
    private RoleReview createRoleReview(PortalUser portalUser, Role role) {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(portalUser);
        roleReview.setRoleId(role);
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReview.setModeratorId(portalUser);
        return roleReviewRepository.save(roleReview);
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

    @Transactional
    @Test
    public void testGetRoleReview() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewReadDTO readDTO = roleReviewService.getRoleReview(roleReview.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleReview);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewWrongId() {
        roleReviewService.getRoleReview(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleReview() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();

        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        create.setUserId(portalUser);
        create.setRoleId(role);
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser);

        RoleReviewReadDTO read = roleReviewService.createRoleReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReview roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReview);
    }

    @Transactional
    @Test
    public void testPatchRoleReview() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        patch.setUserId(portalUser);
        patch.setRoleId(role);
        patch.setTextReview("This role can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser);
        RoleReviewReadDTO read = roleReviewService.patchRoleReview(roleReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchRoleReviewEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

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
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        roleReviewService.deleteRoleReview(roleReview.getId());
        Assert.assertFalse(roleReviewRepository.existsById(roleReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewNotFound() {
        roleReviewService.deleteRoleReview(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleReview() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewPutDTO put = new RoleReviewPutDTO();
        put.setUserId(portalUser);
        put.setRoleId(role);
        put.setTextReview("This role can be described as junk.");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser);
        RoleReviewReadDTO read = roleReviewService.putRoleReview(roleReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReview = roleReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPutRoleReviewEmptyPut() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewPutDTO put = new RoleReviewPutDTO();
        RoleReviewReadDTO read = roleReviewService.putRoleReview(roleReview.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getTextReview());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        RoleReview roleReviewAfterUpdate = roleReviewRepository.findById(read.getId()).get();

        Assert.assertNull(roleReviewAfterUpdate.getUserId());
        Assert.assertNull(roleReviewAfterUpdate.getRoleId());
        Assert.assertNull(roleReviewAfterUpdate.getTextReview());
        Assert.assertNull(roleReviewAfterUpdate.getModeratedStatus());
        Assert.assertNull(roleReviewAfterUpdate.getModeratorId());

        Assertions.assertThat(roleReview).isEqualToComparingFieldByField(roleReviewAfterUpdate);
    }
}
