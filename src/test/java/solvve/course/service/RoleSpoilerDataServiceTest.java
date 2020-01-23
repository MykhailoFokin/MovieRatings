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
@Sql(statements = "delete from role_spoiler_data; delete from role_review; delete from portal_user; delete from user_type; delete from role; delete from person;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleSpoilerDataServiceTest {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

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

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    private RoleReview roleReview;

    private RoleSpoilerData createRoleSpoilerData() {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReviewId(roleReview);
        roleSpoilerData.setStartIndex(100);
        roleSpoilerData.setEndIndex(150);
        return roleSpoilerDataRepository.save(roleSpoilerData);
    }

    @Before
    public void setup() {
        if (roleReview==null) {
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

            RoleReviewCreateDTO create = new RoleReviewCreateDTO();
            roleReview = new RoleReview();
            roleReview.setUserId(portalUser);
            roleReview.setRoleId(role);
            roleReview.setTextReview("This role can be described as junk.");
            roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            roleReview = roleReviewRepository.save(roleReview);
        }
    }

    @Transactional
    @Test
    public void testGetRoleSpoilerData() {
        RoleSpoilerData roleSpoilerData = createRoleSpoilerData();

        RoleSpoilerDataReadDTO readDTO = roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleSpoilerData);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleSpoilerDataWrongId() {
        roleSpoilerDataService.getRoleSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleSpoilerData() {
        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();
        create.setRoleReviewId(roleReview);
        create.setStartIndex(100);
        create.setEndIndex(150);

        RoleSpoilerDataReadDTO read = roleSpoilerDataService.createRoleSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleSpoilerData roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleSpoilerData);
    }

    @Transactional
    @Test
    public void testPatchRoleSpoilerData() {
        RoleSpoilerData roleSpoilerData = createRoleSpoilerData();

        RoleSpoilerDataPatchDTO patch = new RoleSpoilerDataPatchDTO();
        patch.setRoleReviewId(roleReview);
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.patchRoleSpoilerData(roleSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(roleSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchRoleSpoilerDataEmptyPatch() {
        RoleSpoilerData roleSpoilerData = createRoleSpoilerData();

        RoleSpoilerDataPatchDTO patch = new RoleSpoilerDataPatchDTO();
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.patchRoleSpoilerData(roleSpoilerData.getId(), patch);

        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());

        RoleSpoilerData roleSpoilerDataAfterUpdate = roleSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(roleSpoilerData).isEqualToComparingFieldByField(roleSpoilerDataAfterUpdate);
    }

    @Test
    public void testDeleteRoleSpoilerData() {
        RoleSpoilerData roleSpoilerData = createRoleSpoilerData();

        roleSpoilerDataService.deleteRoleSpoilerData(roleSpoilerData.getId());
        Assert.assertFalse(roleSpoilerDataRepository.existsById(roleSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleSpoilerDataNotFound() {
        roleSpoilerDataService.deleteRoleSpoilerData(UUID.randomUUID());
    }
}
