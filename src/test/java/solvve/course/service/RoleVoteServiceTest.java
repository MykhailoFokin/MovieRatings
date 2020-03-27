package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

public class RoleVoteServiceTest extends BaseTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private RoleVoteService roleVoteService;

    @Test
    public void testGetRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVoteReadDTO readDTO = roleVoteService.getRoleVote(roleVote.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleVote,
                "portalUserId","roleId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(roleVote.getPortalUser().getId());
        Assertions.assertThat(readDTO.getRoleId()).isEqualTo(roleVote.getRole().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleVoteWrongId() {
        roleVoteService.getRoleVote(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RoleVoteCreateDTO create = new RoleVoteCreateDTO();
        create.setRoleId(role.getId());
        create.setPortalUserId(portalUser.getId());
        create.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.createRoleVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "portalUser","role");
        Assertions.assertThat(roleVote.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleVote.getRole().getId()).isEqualTo(read.getRoleId());
    }

    @Test
    public void testPatchRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        patch.setRoleId(role.getId());
        patch.setPortalUserId(portalUser.getId());
        patch.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "portalUser","role");
        Assertions.assertThat(roleVote.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleVote.getRole().getId()).isEqualTo(read.getRoleId());
    }

    @Test
    public void testPatchRoleVoteEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePatchDTO patch = new RoleVotePatchDTO();
        RoleVoteReadDTO read = roleVoteService.patchRoleVote(roleVote.getId(), patch);

        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRating());

        RoleVote roleVoteAfterUpdate = roleVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleVoteAfterUpdate.getRole());
        Assert.assertNotNull(roleVoteAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleVoteAfterUpdate.getRating());

        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(roleVoteAfterUpdate, "portalUser", "role");
        Assertions.assertThat(roleVote.getPortalUser().getId())
                .isEqualToComparingFieldByField(roleVoteAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(roleVote.getRole().getId())
                .isEqualToComparingFieldByField(roleVoteAfterUpdate.getRole().getId());
    }

    @Test
    public void testDeleteRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        roleVoteService.deleteRoleVote(roleVote.getId());
        Assert.assertFalse(roleVoteRepository.existsById(roleVote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleVoteNotFound() {
        roleVoteService.deleteRoleVote(UUID.randomUUID());
    }

    @Test
    public void testPutRoleVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePutDTO put = new RoleVotePutDTO();
        put.setRoleId(role.getId());
        put.setPortalUserId(portalUser.getId());
        put.setRating(UserVoteRatingType.R9);
        RoleVoteReadDTO read = roleVoteService.updateRoleVote(roleVote.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(roleVote).isEqualToIgnoringGivenFields(read,
                "portalUser","role");
        Assertions.assertThat(roleVote.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(roleVote.getRole().getId()).isEqualTo(read.getRoleId());
    }

    @Test
    public void testPutRoleVoteEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        RoleVotePutDTO put = new RoleVotePutDTO();
        RoleVoteReadDTO read = roleVoteService.updateRoleVote(roleVote.getId(), put);

        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRating());

        RoleVote roleVoteAfterUpdate = roleVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleVoteAfterUpdate.getRole().getId());
        Assert.assertNotNull(roleVoteAfterUpdate.getPortalUser().getId());
        Assert.assertNotNull(roleVoteAfterUpdate.getRating());

        Assertions.assertThat(roleVote).isEqualToComparingOnlyGivenFields(roleVoteAfterUpdate,
                "id");
        Assertions.assertThat(roleVote.getRole().getId()).isEqualTo(roleVoteAfterUpdate.getRole().getId());
        Assertions.assertThat(roleVote.getPortalUser().getId()).isEqualTo(roleVoteAfterUpdate.getPortalUser().getId());
    }
}
