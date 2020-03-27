package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.exception.EntityNotFoundException;

import javax.persistence.EntityManager;
import java.util.UUID;

public class RepositoryHelperTest extends BaseTest {

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testGetReferenceIfExistsWrongId() throws Exception {

        Assertions.assertThatThrownBy(() -> repositoryHelper.getReferenceIfExists(PortalUser.class,
                UUID.randomUUID())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void testGetReferenceIfExistsWithEntity() {

        PortalUser portalUser = testObjectsFactory.createPortalUser();
        PortalUser portalUserReference = repositoryHelper.getReferenceIfExists(PortalUser.class, portalUser.getId());
        Assert.assertFalse(entityManager.contains(portalUserReference));
    }

    @Test
    public void testGetReferenceIfExistsLazyInitializationException() {

        PortalUser portalUser = testObjectsFactory.createPortalUser();
        PortalUser userReference = repositoryHelper.getReferenceIfExists(PortalUser.class, portalUser.getId());
        Assertions.assertThatThrownBy(()-> userReference.getName()).isInstanceOf(LazyInitializationException.class);
    }
}
