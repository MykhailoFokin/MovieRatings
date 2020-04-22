package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.dto.RoleInLeaderBoardReadDTO;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoleRepositoryTest extends BaseTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role r = testObjectsFactory.createRole(person,movie);
        assertNotNull(r.getId());
        assertTrue(roleRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = roleRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        roleRepository.save(entity);

        testObjectsFactory.inTransaction(() -> {
            Role entityAtAfterReload = roleRepository.findById(entity.getId()).get();

            Instant updatedAtAfterReload = entityAtAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test
    public void testGetIdsOfRoles() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Set<UUID> expectedIdsOfRoles = new HashSet<>();
        expectedIdsOfRoles.add(testObjectsFactory.createRole(person, movie).getId());
        expectedIdsOfRoles.add(testObjectsFactory.createRole(person, movie).getId());

        testObjectsFactory.inTransaction(()-> {
            Assert.assertEquals(expectedIdsOfRoles, roleRepository.getIdsOfRoles().collect(Collectors.toSet()));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveRoleValidation() {
        Role entity = new Role();
        roleRepository.save(entity);
    }

    @Test
    public void testGetRolesLeaderBoard() {
        int rolesCount = 3;
        Set<UUID> roleIds = new HashSet<>();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        for (int i =0; i < rolesCount; ++i) {
            Role role = testObjectsFactory.createRoleWithRating(person, movie, 15D);
            roleIds.add(role.getId());

            testObjectsFactory.createRoleVote(portalUser, role, true);
            testObjectsFactory.createRoleVote(portalUser, role, true);
            testObjectsFactory.createRoleVote(portalUser, role, false);
        }

        List<RoleInLeaderBoardReadDTO> rolesLeaderBoard = roleRepository.getRolesLeaderBoard();
        Assertions.assertThat(rolesLeaderBoard).isSortedAccordingTo(
                Comparator.comparing(RoleInLeaderBoardReadDTO::getAverageRating).reversed());

        Assert.assertEquals(roleIds, rolesLeaderBoard.stream().map(RoleInLeaderBoardReadDTO::getId)
                .collect(Collectors.toSet()));

        for (RoleInLeaderBoardReadDTO r : rolesLeaderBoard) {
            Assert.assertNotNull(r.getRoleTitle());
            Assert.assertNotNull(r.getMovieTitle());
            Assert.assertNotNull(r.getAverageRating());
            Assert.assertEquals(2, r.getVotesCount());
        }
    }
}
