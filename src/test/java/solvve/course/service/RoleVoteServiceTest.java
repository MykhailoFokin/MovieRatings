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
@Sql(statements = "delete from role_vote; delete from portal_user; delete from user_type; delete from role; delete from person;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    private Role role;

    private PortalUser portalUser;

    private RoleVote createRoleVote() {
        RoleVote roleVote = new RoleVote();
        roleVote.setRoleId(role);
        roleVote.setUserId(portalUser);
        roleVote.setRating(UserVoteRatingType.R9);
        return roleVoteRepository.save(roleVote);
    }

    @Before
    public void setup() {
        if (role==null) {
            Person person = new Person();
            person.setSurname("Surname");
            person.setName("Name");
            person.setMiddleName("MiddleName");
            person = personRepository.save(person);

            role = new Role();
            role.setId(UUID.randomUUID());
            role.setTitle("Actor");
            role.setRoleType("Main_Role");
            role.setDescription("Description test");
            role.setPersonId(person);
            role = roleRepository.save(role);
        }

        if (portalUser==null) {
            UserType userType = new UserType();
            userType.setUserGroup(UserGroupType.USER);
            userType = userTypeRepository.save(userType);

            portalUser = new PortalUser();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userType);
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
        }
    }

    @Transactional
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

    @Transactional
    @Test
    public void testCreateRoleVote() {
        RoleVoteCreateDTO create = new RoleVoteCreateDTO();
        create.setRoleId(role);
        create.setUserId(portalUser);
        create.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(roleVote);
    }

    @Transactional
    @Test
    public void testPatchRoleVote() {
        RoleVote roleVote = createRoleVote();

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        patch.setRoleId(role);
        patch.setUserId(portalUser);
        patch.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToComparingFieldByField(read);
    }

    @Transactional
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
