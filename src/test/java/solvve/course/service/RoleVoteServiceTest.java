package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
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
import solvve.course.repository.PortalUsersRepository;
import solvve.course.repository.RoleRepository;
import solvve.course.repository.RoleVoteRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role_vote; delete from portal_users; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleVoteServiceTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private RoleVoteService roleVoteService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PortalUsersRepository portalUsersRepository;

    @Autowired
    private PortalUsersService portalUsersService;

    private PortalUsersReadDTO portalUsersReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    private RoleReadDTO roleReadDTO;

    @Before
    public void setup() {
        if (roleReadDTO==null) {
            Role role = new Role();
            role.setId(UUID.randomUUID());
            role.setTitle("Actor");
            role.setRoleType("Main_Role");
            role.setDescription("Description test");
            //RoleReadDTO readDTO = roleService.getRole(role.getId());
            role = roleRepository.save(role);
            roleReadDTO = roleService.getRole(role.getId());
        }

        if (portalUsersReadDTO==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            PortalUsers portalUser = new PortalUsers();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userTypes.getId());
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUsersRepository.save(portalUser);
            portalUsersReadDTO = portalUsersService.getPortalUsers(portalUser.getId());
        }
    }

    @Test
    public void testGetRoleVote() {
        RoleVote roleVote = new RoleVote();
        roleVote.setId(UUID.randomUUID());
        roleVote.setRoleId(roleReadDTO.getId());
        roleVote.setUserId(portalUsersReadDTO.getId());
        roleVote.setRating(UserVoteRatingType.valueOf("R9"));
        roleVote.setDescription("Description");
        roleVote.setSpoilerStartIndex(40);
        roleVote.setSpoilerEndIndex(60);
        roleVote.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleVote.setModeratorId(portalUsersReadDTO.getId());
        roleVote = roleVoteRepository.save(roleVote);

        RoleVoteReadDTO readDTO = roleVoteService.getRoleVote(roleVote.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(roleVote);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleVoteWrongId() {
        roleVoteService.getRoleVote(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleVote() {
        RoleVoteCreateDTO create = new RoleVoteCreateDTO();
        create.setRoleId(roleReadDTO.getId());
        create.setUserId(portalUsersReadDTO.getId());
        create.setRating(UserVoteRatingType.valueOf("R9"));
        create.setDescription("Description");
        create.setSpoilerStartIndex(40);
        create.setSpoilerEndIndex(60);
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUsersReadDTO.getId());
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleVote);
    }
}
