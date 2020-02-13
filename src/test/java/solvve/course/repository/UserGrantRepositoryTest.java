package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserType;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from user_grant",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class UserGrantRepositoryTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testSave() {
        UserType u = testObjectsFactory.createUserType();
        UserGrant g = new UserGrant();
        g.setUserTypeId(u);
        g = userGrantRepository.save(g);
        assertNotNull(g.getId());
        assertTrue(userGrantRepository.findById(g.getId()).isPresent());
    }

    @Transactional
    @Test
    public void testCteatedAtIsSet() {
        UserType u = testObjectsFactory.createUserType();
        UserGrant entity = testObjectsFactory.createUserGrant(u);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Transactional
    @Test
    public void testModifiedAtIsSet() {
        UserType u = testObjectsFactory.createUserType();
        UserGrant entity = testObjectsFactory.createUserGrant(u);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Transactional
    @Test
    public void testModifiedAtIsModified() {
        UserType u = testObjectsFactory.createUserType();
        UserGrant entity = testObjectsFactory.createUserGrant(u);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setObjectName("NewNameTest");
        userGrantRepository.save(entity);
        entity = userGrantRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
