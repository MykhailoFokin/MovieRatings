package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.MovieReviewReadDTO;

public class MovieMovieReviewServiceTest extends BaseTest {

    @Autowired
    private MovieMovieReviewService movieMovieReviewService;

    @Test
    public void testGetMovieReviews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie,
                UserModeratedStatusType.SUCCESS);

        Assertions.assertThat(movieMovieReviewService.getMovieReviews(movie.getId()))
                .extracting(MovieReviewReadDTO::getId)
                .containsExactlyInAnyOrder(movieReview.getId());
    }

    @Test
    public void testGetMovieReviewsUnModerated() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie,
                UserModeratedStatusType.CREATED);
        testObjectsFactory.createMovieReview(portalUser, movie, UserModeratedStatusType.SUCCESS);

        Assertions.assertThat(movieMovieReviewService.getUnModeratedMovieReviews(movie.getId()))
                .extracting(MovieReviewReadDTO::getId)
                .containsExactlyInAnyOrder(movieReview.getId());
    }
}
