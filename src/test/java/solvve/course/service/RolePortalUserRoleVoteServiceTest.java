package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.RoleVoteRepository;

import java.util.List;

public class RolePortalUserRoleVoteServiceTest extends BaseTest {

    @Autowired
    private RolePortalUserRoleVoteService rolePortalUserRoleVoteService;

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Test
    public void testGetRolesByPerson() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleVote roleVote = testObjectsFactory.createRoleVote(portalUser, role);

        List<RoleVoteReadDTO> readDTO = rolePortalUserRoleVoteService.getRolesVotesByPortalUser(role.getId(),
                portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id")
                .containsExactlyInAnyOrder(roleVote.getId());
    }

    @Test
    public void testCreateRoleVoteByPortalUserNormal() {
        PortalUser portalUser = testObjectsFactory.createPortalUser(UserConfidenceType.NORMAL);
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RoleVoteCreateDTO create = testObjectsFactory.createRoleVoteCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRating(UserVoteRatingType.R8);

        RoleVoteReadDTO read =
                rolePortalUserRoleVoteService.createRoleVoteByPortalUser(portalUser.getId(), role.getId(), create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleVote roleVote = roleVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleVote,
                "portalUserId", "roleId");
        Assertions.assertThat(read.getPortalUserId())
                .isEqualToComparingFieldByField(roleVote.getPortalUser().getId());
        Assertions.assertThat(read.getRoleId())
                .isEqualToComparingFieldByField(roleVote.getRole().getId());
    }

    @Test
    public void testCreateRoleVoteByPortalUserBlocked() {
        PortalUser portalUser = testObjectsFactory.createPortalUser(UserConfidenceType.BLOCKED);
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RoleVoteCreateDTO create = testObjectsFactory.createRoleVoteCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRating(UserVoteRatingType.R8);

        Assertions.assertThatThrownBy(() -> {
            rolePortalUserRoleVoteService.createRoleVoteByPortalUser(portalUser.getId(), role.getId(), create);
        }).isInstanceOf(AccessDeniedException.class);
    }
}
