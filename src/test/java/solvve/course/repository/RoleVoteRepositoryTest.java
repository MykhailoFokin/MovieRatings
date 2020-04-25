package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.*;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoleVoteRepositoryTest extends BaseTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        RoleVote r = new RoleVote();
        r.setRole(role);
        r.setPortalUser(portalUser);
        r.setRating(UserVoteRatingType.R2);
        r = roleVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleVoteRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleVote entity = testObjectsFactory.createRoleVote(portalUser, role);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleVoteRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleVote entity = testObjectsFactory.createRoleVote(portalUser, role);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = roleVoteRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleVote entity = testObjectsFactory.createRoleVote(portalUser, role);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setRating(UserVoteRatingType.R2);
        roleVoteRepository.save(entity);
        entity.setRating(UserVoteRatingType.R3);
        roleVoteRepository.save(entity);

        RoleVote entityAfterSave = roleVoteRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entityAfterSave.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }

    @Test
    public void testCalcAverageMarkOfRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser1 = testObjectsFactory.createPortalUser(userType);
        testObjectsFactory.createRoleVote(portalUser1, role, UserVoteRatingType.R7);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser(userType);
       testObjectsFactory.createRoleVote(portalUser2, role, UserVoteRatingType.R9);

        Assert.assertEquals(7.0, roleVoteRepository.calcAverageMarkOfRole(role.getId()), Double.MIN_NORMAL);
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveRoleVoteValidation() {
        RoleVote entity = new RoleVote();
        roleVoteRepository.save(entity);
    }
}
