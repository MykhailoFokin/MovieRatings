package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.repository.MovieReviewFeedbackRepository;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_review_feedback",
        "delete from movie_review_compliant",
        "delete from movie_review",
        "delete from movie",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserMovieServiceTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private PortalUserMovieService portalUserMovieService;

    @Test
    public void testGetPortalUserMovies() {
        Instant startFrom = Instant.now();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Movie m4 = testObjectsFactory.createMovie();
        Movie m5 = testObjectsFactory.createMovie();
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();
        PortalUser p3 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieReview(p1, m1);
        MovieReview mr2 = testObjectsFactory.createMovieReview(p1, m2);
        testObjectsFactory.createMovieReview(p2, m2);
        testObjectsFactory.createMovieReview(p2, m3);
        MovieReview mr5 = testObjectsFactory.createMovieReview(p3, m4);
        MovieReview mr6 = testObjectsFactory.createMovieReview(p3, m5);
        testObjectsFactory.createMovieReviewCompliant(p1, m4, mr5);
        MovieReviewFeedback mrf1 = testObjectsFactory.createMovieReviewFeedback(p1, m5, mr6);
        testObjectsFactory.createMovieReviewFeedback(p1, m2, mr2);
        Instant startTo = Instant.now();

        mrf1.setIsLiked(Boolean.FALSE);
        movieReviewFeedbackRepository.save(mrf1);

        Assertions.assertThat(portalUserMovieService.getPortalUserMovies(p1.getId(), startFrom, startTo)).extracting(
                "id").containsExactly(m4.getId(), m2.getId(), m1.getId());
    }

    @Test
    public void testGetPortalUserRecommendedMovies() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Movie m4 = testObjectsFactory.createMovie();
        Movie m5 = testObjectsFactory.createMovie();
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();
        PortalUser p3 = testObjectsFactory.createPortalUser();
        MovieReview mr1 = testObjectsFactory.createMovieReview(p1, m1);
        MovieReview mr2 = testObjectsFactory.createMovieReview(p1, m2);
        testObjectsFactory.createMovieReview(p2, m2);
        testObjectsFactory.createMovieReview(p2, m3);
        MovieReview mr5 = testObjectsFactory.createMovieReview(p3, m4);
        MovieReview mr6 = testObjectsFactory.createMovieReview(p3, m5);
        MovieReview mr7 = testObjectsFactory.createMovieReview(p3, m1);
        testObjectsFactory.createMovieReviewFeedback(p1, m1, mr1);
        testObjectsFactory.createMovieReviewFeedback(p1, m2, mr2);
        testObjectsFactory.createMovieReviewFeedback(p3, m4, mr5);
        testObjectsFactory.createMovieReviewFeedback(p3, m5, mr6);
        testObjectsFactory.createMovieReviewFeedback(p3, m1, mr7);

        Assertions.assertThat(portalUserMovieService.getPortalUserRecommendedMovies(p1.getId())).extracting(
                "id").containsExactly(m5.getId(), m4.getId());
    }
}
