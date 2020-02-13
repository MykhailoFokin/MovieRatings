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
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class PortalUserRepositoryTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        UserType userType = testObjectsFactory.createUserType();

        PortalUser r = new PortalUser();
        r.setUserTypeId(userType);
        r = portalUserRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(portalUserRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = portalUserRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = portalUserRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        PortalUser entity = testObjectsFactory.createPortalUser();

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setName("NewNameTest");
        portalUserRepository.save(entity);
        entity = portalUserRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
