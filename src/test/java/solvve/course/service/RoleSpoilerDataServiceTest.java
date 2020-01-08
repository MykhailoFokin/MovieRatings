package solvve.course.service;

import org.assertj.core.api.Assertions;
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
@Sql(statements = "delete from role_spoiler_data; delete from role_review; delete from portal_user; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    private PortalUserReadDTO portalUserReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    private RoleReadDTO roleReadDTO;

    private RoleReviewReadDTO roleReviewReadDTO;

    @Before
    public void setup() {
        if (roleReviewReadDTO==null) {
            Role role = new Role();
            //role.setId(UUID.randomUUID());
            role.setTitle("Actor");
            role.setRoleType("Main_Role");
            role.setDescription("Description test");
            role = roleRepository.save(role);
            //RoleReadDTO readDTO = roleService.getRole(role.getId());
            roleReadDTO = roleService.getRole(role.getId());

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
            portalUserReadDTO = portalUserService.getPortalUsers(portalUser.getId());

            RoleReviewCreateDTO create = new RoleReviewCreateDTO();
            RoleReview roleReview = new RoleReview();
            roleReview.setUserId(portalUserReadDTO.getId());
            roleReview.setRoleId(roleReadDTO.getId());
            roleReview.setTextReview("This role can be described as junk.");
            roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            roleReview = roleReviewRepository.save(roleReview);
            roleReviewReadDTO = roleReviewService.getRoleReview(roleReview.getId());
        }
    }

    @Test
    public void testGetRoleSpoilerData() {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setId(UUID.randomUUID());
        roleSpoilerData.setRoleReviewId(roleReviewReadDTO.getId());
        roleSpoilerData.setStartIndex(100);
        roleSpoilerData.setEndIndex(150);
        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);

        RoleSpoilerDataReadDTO readDTO = roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleSpoilerData);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleSpoilerDataWrongId() {
        roleSpoilerDataService.getRoleSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleSpoilerData() {
        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();
        create.setRoleReviewId(roleReviewReadDTO.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        RoleSpoilerDataReadDTO read = roleSpoilerDataService.createRoleSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleSpoilerData roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleSpoilerData);
    }
}
