package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MovieReviewRepositoryTest extends BaseTest {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Test
    public void testSave() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview r = new MovieReview();
        r.setPortalUser(portalUser);
        r.setMovie(movie);
        r.setModerator(portalUser);
        r.setTextReview("Review");
        r = movieReviewRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieReviewRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview entity = new MovieReview();
        entity.setPortalUser(portalUser);
        entity.setMovie(movie);
        entity.setModerator(portalUser);
        entity.setTextReview("Review");
        entity = movieReviewRepository.save(entity);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieReviewRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview entity = new MovieReview();
        entity.setPortalUser(portalUser);
        entity.setMovie(movie);
        entity.setModerator(portalUser);
        entity.setTextReview("Review");
        entity = movieReviewRepository.save(entity);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = movieReviewRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview entity = new MovieReview();
        entity.setPortalUser(portalUser);
        entity.setMovie(movie);
        entity.setModerator(portalUser);
        entity.setTextReview("Review");
        entity = movieReviewRepository.save(entity);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setTextReview("NewNameTest");
        movieReviewRepository.save(entity);
        entity = movieReviewRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveMovieReviewValidation() {
        MovieReview entity = new MovieReview();
        movieReviewRepository.save(entity);
    }
}
