package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserType;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserGrantRepositoryTest extends BaseTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Test
    public void testSave() {
        UserType u = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UserGrant g = testObjectsFactory.createUserGrant(u, portalUser);

        g = userGrantRepository.save(g);
        assertNotNull(g.getId());
        assertTrue(userGrantRepository.findById(g.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        UserType u = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UserGrant entity = testObjectsFactory.createUserGrant(u, portalUser);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        UserType u = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UserGrant entity = testObjectsFactory.createUserGrant(u, portalUser);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        UserType u = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        UserGrant entity = testObjectsFactory.createUserGrant(u, portalUser);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setObjectName("NewNameTest");
        userGrantRepository.save(entity);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveUserGrantValidation() {
        UserGrant entity = new UserGrant();
        userGrantRepository.save(entity);
    }
}
