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

public class MovieReviewCompliantRepositoryTest extends BaseTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Test
    public void testSave() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewCompliant r = testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);
        assertNotNull(r.getId());
        assertTrue(movieReviewCompliantRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant entity = testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieReviewCompliantRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant entity = testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = movieReviewCompliantRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant entity = testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        entity = movieReviewCompliantRepository.save(entity);
        UUID entityId = entity.getId();

        testObjectsFactory.inTransaction(() -> {
            MovieReviewCompliant entityAfterUpdate = movieReviewCompliantRepository.findById(entityId).get();

            Instant updatedAtAfterReload = entityAfterUpdate.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveMovieReviewCompliantValidation() {
        MovieReviewCompliant entity = new MovieReviewCompliant();
        movieReviewCompliantRepository.save(entity);
    }
}
