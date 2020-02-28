package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.domain.PortalUser;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewFeedbackRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_review_feedback",
        " delete from movie_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewMovieReviewFeedbackServiceTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieReviewMovieReviewFeedbackService movieReviewMovieReviewFeedbackService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        List<MovieReviewFeedbackReadDTO> readDTO =
                movieReviewMovieReviewFeedbackService.getMovieReviewMovieReviewFeedback(movieReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(movieReviewFeedback.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewFeedbackWrongId() {
        movieReviewMovieReviewFeedbackService.getMovieReviewMovieReviewFeedback(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setMovieReviewId(movieReview.getId());
        create.setIsLiked(true);

        MovieReviewFeedbackReadDTO read =
                movieReviewMovieReviewFeedbackService.createMovieReviewMovieReviewFeedback(movieReview.getId(), create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewFeedback movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReviewFeedback,
                "portalUserId", "movieId", "movieReviewId");
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReviewFeedback.getMovie().getId());
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieReviewFeedback.getMovieReview().getId());
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(movieReviewFeedback.getPortalUser().getId());
    }

    @Test
    public void testPatchMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setMovieReviewId(movieReview.getId());
        patch.setIsLiked(true);
        MovieReviewFeedbackReadDTO read =
                movieReviewMovieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReview.getId(),
                        movieReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "movieReview");
        Assertions.assertThat(movieReviewFeedback.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewFeedback.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
    }

    @Test
    public void testPatchMovieReviewFeedbackEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        MovieReviewFeedbackReadDTO read =
                movieReviewMovieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReview.getId(),
                        movieReviewFeedback.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getIsLiked());

        MovieReviewFeedback movieReviewFeedbackAfterUpdate =
                movieReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovie());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieReview());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(movieReviewFeedback).isEqualToIgnoringGivenFields(movieReviewFeedbackAfterUpdate,
                "portalUser", "movie", "movieReview");
        Assertions.assertThat(movieReviewFeedback.getMovie().getId())
                .isEqualTo(movieReviewFeedbackAfterUpdate.getMovie().getId());
        Assertions.assertThat(movieReviewFeedback.getMovieReview().getId())
                .isEqualTo(movieReviewFeedbackAfterUpdate.getMovieReview().getId());
        Assertions.assertThat(movieReviewFeedback.getPortalUser().getId())
                .isEqualTo(movieReviewFeedbackAfterUpdate.getPortalUser().getId());
    }

    @Test
    public void testDeleteMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        movieReviewMovieReviewFeedbackService.deleteMovieReviewMovieReviewFeedback(movieReview.getId(),
                movieReviewFeedback.getId());
        Assert.assertFalse(movieReviewFeedbackRepository.existsById(movieReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewFeedbackNotFound() {
        movieReviewMovieReviewFeedbackService.deleteMovieReviewMovieReviewFeedback(UUID.randomUUID(),
                UUID.randomUUID());
    }

    @Test
    public void testPutMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setMovieReviewId(movieReview.getId());
        put.setIsLiked(true);
        MovieReviewFeedbackReadDTO read =
                movieReviewMovieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReview.getId(),
                        movieReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "movieReview");
        Assertions.assertThat(movieReviewFeedback.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewFeedback.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
    }

    @Test
    public void testPutMovieReviewFeedbackEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback =
                testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        MovieReviewFeedbackReadDTO read =
                movieReviewMovieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReview.getId(),
                        movieReviewFeedback.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNull(read.getIsLiked());

        MovieReviewFeedback movieReviewFeedbackAfterUpdate =
                movieReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovie());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieReview());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getIsLiked());
    }
}
