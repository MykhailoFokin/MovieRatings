package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_review_compliant",
        "delete from movie_review",
        "delete from movie",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieReviewCompliantRepositoryTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

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
    public void testCteatedAtIsSet() {
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
    public void testupdatedAtIsSet() {
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
    public void testupdatedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant entity = testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        movieReviewCompliantRepository.save(entity);
        entity = movieReviewCompliantRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
