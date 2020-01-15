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

    private RoleVote createRoleVote() {
        RoleVote roleVote = new RoleVote();
        roleVote.setRoleId(roleReadDTO.getId());
        roleVote.setUserId(portalUserReadDTO.getId());
        roleVote.setRating(UserVoteRatingType.R9);
        return roleVoteRepository.save(roleVote);
    }

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
            portalUserReadDTO = portalUserService.getPortalUser(portalUser.getId());
        }
    }

    @Test
    public void testGetRoleVote() {
        RoleVote roleVote = createRoleVote();

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
        create.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleVote);
    }

    @Test
    public void testPatchRoleVote() {
        RoleVote roleVote = createRoleVote();

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        patch.setRoleId(roleReadDTO.getId());
        patch.setUserId(portalUserReadDTO.getId());
        patch.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleVoteEmptyPatch() {
        RoleVote roleVote = createRoleVote();

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRating());

        RoleVote roleVoteAfterUpdate = roleVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleVoteAfterUpdate.getRoleId());
        Assert.assertNotNull(roleVoteAfterUpdate.getUserId());
        Assert.assertNotNull(roleVoteAfterUpdate.getRating());

        Assertions.assertThat(roleVote).isEqualToComparingFieldByField(roleVoteAfterUpdate);
    }

    @Test
    public void testDeleteRoleVote() {
        RoleVote roleVote = createRoleVote();

        roleVoteService.deleteRoleVote(roleVote.getId());
        Assert.assertFalse(roleVoteRepository.existsById(roleVote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleVoteNotFound() {
        roleVoteService.deleteRoleVote(UUID.randomUUID());
    }
}
