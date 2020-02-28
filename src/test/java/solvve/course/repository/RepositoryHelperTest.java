package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.PortalUser;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.utils.TestObjectsFactory;

import javax.persistence.EntityManager;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RepositoryHelperTest {

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testGetReferenceIfExistsWrongId() throws Exception {

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> repositoryHelper.getReferenceIfExists(PortalUser.class,  UUID.randomUUID()));
    }

    @Test
    public void testGetReferenceIfExistsWithEntity() {

        PortalUser portalUser = testObjectsFactory.createPortalUser();

        PortalUser portalUserReference = repositoryHelper.getReferenceIfExists(PortalUser.class, portalUser.getId());

        Assert.assertFalse(entityManager.contains(portalUserReference));
    }
}
