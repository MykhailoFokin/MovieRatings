package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoleReviewRepositoryTest extends BaseTest {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        RoleReview r = new RoleReview();
        r.setRole(role);
        r.setPortalUser(portalUser);
        r.setModerator(portalUser);
        r = roleReviewRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleReviewRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview entity = testObjectsFactory.createRoleReview(portalUser, role);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleReviewRepository.findById(entity.getId()).get();

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
        RoleReview entity = testObjectsFactory.createRoleReview(portalUser, role);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = roleReviewRepository.findById(entity.getId()).get();

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
        RoleReview entity = testObjectsFactory.createRoleReview(portalUser, role);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setTextReview("NewNameTest");
        entity = roleReviewRepository.save(entity);
        UUID entityId = entity.getId();

        testObjectsFactory.inTransaction(() -> {
            RoleReview entityAtAfterReload = roleReviewRepository.findById(entityId).get();

            Instant updatedAtAfterReload = entityAtAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveRoleReviewValidation() {
        RoleReview entity = new RoleReview();
        roleReviewRepository.save(entity);
    }
}
