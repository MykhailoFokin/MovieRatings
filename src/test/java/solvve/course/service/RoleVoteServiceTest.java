package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_vote",
        " delete from portal_user",
        " delete from user_type",
        " delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleVoteServiceTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private RoleVoteService roleVoteService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVoteReadDTO readDTO = roleVoteService.getRoleVote(roleVote.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleVote,
                "userId","roleId");
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(roleVote.getUserId().getId());
        Assertions.assertThat(readDTO.getRoleId()).isEqualTo(roleVote.getRoleId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleVoteWrongId() {
        roleVoteService.getRoleVote(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);

        RoleVoteCreateDTO create = new RoleVoteCreateDTO();
        create.setRoleId(role.getId());
        create.setUserId(portalUser.getId());
        create.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "userId","roleId");
        Assertions.assertThat(roleVote.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleVote.getRoleId().getId()).isEqualTo(read.getRoleId());
    }

    @Transactional
    @Test
    public void testPatchRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        patch.setRoleId(role.getId());
        patch.setUserId(portalUser.getId());
        patch.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "userId","roleId");
        Assertions.assertThat(roleVote.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleVote.getRoleId().getId()).isEqualTo(read.getRoleId());
    }

    @Transactional
    @Test
    public void testPatchRoleVoteEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

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
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        roleVoteService.deleteRoleVote(roleVote.getId());
        Assert.assertFalse(roleVoteRepository.existsById(roleVote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleVoteNotFound() {
        roleVoteService.deleteRoleVote(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePutDTO put = new RoleVotePutDTO();
        put.setRoleId(role.getId());
        put.setUserId(portalUser.getId());
        put.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.putRoleVote(roleVote.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "userId","roleId");
        Assertions.assertThat(roleVote.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(roleVote.getRoleId().getId()).isEqualTo(read.getRoleId());
    }

    @Transactional
    @Test
    public void testPutRoleVoteEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePutDTO put = new RoleVotePutDTO();
        RoleVoteReadDTO read = roleVoteService.putRoleVote(roleVote.getId(), put);

        Assert.assertNull(read.getRoleId());
        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRating());

        RoleVote roleVoteAfterUpdate = roleVoteRepository.findById(read.getId()).get();

        Assert.assertNull(roleVoteAfterUpdate.getRoleId().getId());
        Assert.assertNull(roleVoteAfterUpdate.getUserId().getId());
        Assert.assertNull(roleVoteAfterUpdate.getRating());

        Assertions.assertThat(roleVote).isEqualToComparingFieldByField(roleVoteAfterUpdate);
    }
}
