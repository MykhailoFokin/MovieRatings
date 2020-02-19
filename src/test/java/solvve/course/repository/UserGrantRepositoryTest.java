package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserType;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from user_grant",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class UserGrantRepositoryTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

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
    public void testCteatedAtIsSet() {
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
    public void testupdatedAtIsSet() {
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
    public void testupdatedAtIsModified() {
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
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
