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
import solvve.course.domain.*;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_review_feedback",
        "delete from movie_review",
        "delete from portal_user",
        "delete from user_type",
        "delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieReviewFeedbackRepositoryTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testSave() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewFeedback r = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);
        assertNotNull(r.getId());
        assertTrue(movieReviewFeedbackRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback entity = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieReviewFeedbackRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback entity = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = movieReviewFeedbackRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback entity = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setIsLiked(false);
        movieReviewFeedbackRepository.save(entity);
        entity = movieReviewFeedbackRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
