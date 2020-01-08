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
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.RoleRepository;
import solvve.course.repository.RoleVoteRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role_vote; delete from portal_user; delete from user_types; delete from role;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    private PortalUserReadDTO portalUserReadDTO;

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
            portalUserReadDTO = portalUserService.getPortalUsers(portalUser.getId());
        }
    }

    @Test
    public void testGetRoleVote() {
        RoleVote roleVote = new RoleVote();
        roleVote.setId(UUID.randomUUID());
        roleVote.setRoleId(roleReadDTO.getId());
        roleVote.setUserId(portalUserReadDTO.getId());
        roleVote.setRating(UserVoteRatingType.valueOf("R9"));
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
        create.setUserId(portalUserReadDTO.getId());
        create.setRating(UserVoteRatingType.valueOf("R9"));
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleVote);
    }
}
