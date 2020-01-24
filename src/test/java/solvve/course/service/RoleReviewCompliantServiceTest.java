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
@Sql(statements = {"delete from role_review_compliant","delete from role_review","delete from portal_user","delete from user_type","delete from role","delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewCompliantServiceTest {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleReviewCompliantService roleReviewCompliantService;

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

    private RoleReviewCompliant createRoleReviewCompliant(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(portalUser);
        roleReviewCompliant.setRoleId(role);
        roleReviewCompliant.setRoleReviewId(roleReview);
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReviewCompliant.setModeratorId(portalUser);
        return roleReviewCompliantRepository.save(roleReviewCompliant);
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
    public void testGetRoleReviewCompliant() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantReadDTO readDTO = roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleReviewCompliant);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewCompliantWrongId() {
        roleReviewCompliantService.getRoleReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleReviewCompliant() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setUserId(portalUser);
        create.setRoleId(role);
        create.setRoleReviewId(roleReview);
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser);

        RoleReviewCompliantReadDTO read = roleReviewCompliantService.createRoleReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewCompliant roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleReviewCompliant);
    }

    @Transactional
    @Test
    public void testPatchRoleReviewCompliant() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        patch.setUserId(portalUser);
        patch.setRoleId(role);
        patch.setRoleReviewId(roleReview);
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser);
        RoleReviewCompliantReadDTO read = roleReviewCompliantService.patchRoleReviewCompliant(roleReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchRoleReviewCompliantEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant(portalUser, role, roleReview);

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
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant(portalUser, role, roleReview);

        roleReviewCompliantService.deleteRoleReviewCompliant(roleReviewCompliant.getId());
        Assert.assertFalse(roleReviewCompliantRepository.existsById(roleReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewCompliantNotFound() {
        roleReviewCompliantService.deleteRoleReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleReviewCompliant() {
        PortalUser portalUser = createPortalUser();
        Role role = createRole();
        RoleReview roleReview = createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant = createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();
        put.setUserId(portalUser);
        put.setRoleId(role);
        put.setRoleReviewId(roleReview);
        put.setDescription("Just punish him!");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser);
        RoleReviewCompliantReadDTO read = roleReviewCompliantService.putRoleReviewCompliant(roleReviewCompliant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToComparingFieldByField(read);
    }
}
