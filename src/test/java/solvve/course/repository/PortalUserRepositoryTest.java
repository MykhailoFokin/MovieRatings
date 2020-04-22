package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserType;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PortalUserRepositoryTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testSave() {
        UserType userType = testObjectsFactory.createUserType();

        PortalUser r = new PortalUser();
        r.setUserType(userType);
        r.setLogin("Login");
        r = portalUserRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(portalUserRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = portalUserRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = portalUserRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setName("NewNameTest");
        portalUserRepository.save(entity);

        testObjectsFactory.inTransaction(() -> {
            PortalUser entityAtAfterReload = portalUserRepository.findById(entity.getId()).get();

            Instant updatedAtAfterReload = entityAtAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSavePortalUserValidation() {
        PortalUser entity = new PortalUser();
        portalUserRepository.save(entity);
    }
}
