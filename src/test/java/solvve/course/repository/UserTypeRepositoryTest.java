package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserTypeRepositoryTest extends BaseTest {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Test
    public void testSave() {
        UserType r = new UserType();
        r.setUserGroup(UserGroupType.USER);
        r = userTypeRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(userTypeRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        UserType entity = testObjectsFactory.createUserType();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = userTypeRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        UserType entity = testObjectsFactory.createUserType();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = userTypeRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        UserType entity = testObjectsFactory.createUserType();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setUserGroup(UserGroupType.CONTENTMANAGER);
        userTypeRepository.save(entity);

        testObjectsFactory.inTransaction(() -> {
            UserType entityAtAfterReload = userTypeRepository.findById(entity.getId()).get();

            Instant updatedAtAfterReload = entityAtAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveUserTypeValidation() {
        UserType entity = new UserType();
        userTypeRepository.save(entity);
    }
}
