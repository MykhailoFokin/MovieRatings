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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;
import solvve.course.utils.TestObjectsFactory;

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
public class MovieReviewFeedbackServiceTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackReadDTO readDTO =
                movieReviewFeedbackService.getMovieReviewFeedback(movieReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieReviewFeedback,
                "userId", "movieId", "movieReviewId");
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieReviewFeedback.getMovieId().getId());
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieReviewFeedback.getMovieReviewId().getId());
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(movieReviewFeedback.getUserId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewFeedbackWrongId() {
        movieReviewFeedbackService.getMovieReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        create.setUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setMovieReviewId(movieReview.getId());
        create.setIsLiked(true);

        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.createMovieReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewFeedback movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReviewFeedback,
                "userId", "movieId", "movieReviewId");
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReviewFeedback.getMovieId().getId());
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieReviewFeedback.getMovieReviewId().getId());
        Assertions.assertThat(read.getUserId()).isEqualTo(movieReviewFeedback.getUserId().getId());
    }

    @Transactional
    @Test
    public void testPatchMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setMovieReviewId(movieReview.getId());
        patch.setIsLiked(true);
        MovieReviewFeedbackReadDTO read =
                movieReviewFeedbackService.patchMovieReviewFeedback(movieReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToIgnoringGivenFields(read,
                "userId", "movieId", "movieReviewId");
        Assertions.assertThat(movieReviewFeedback.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewFeedback.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewFeedback.getUserId().getId()).isEqualTo(read.getUserId());
    }

    @Transactional
    @Test
    public void testPatchMovieReviewFeedbackEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        MovieReviewFeedbackReadDTO read =
                movieReviewFeedbackService.patchMovieReviewFeedback(movieReviewFeedback.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getIsLiked());

        MovieReviewFeedback movieReviewFeedbackAfterUpdate = movieReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieId());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieReviewId());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(movieReviewFeedbackAfterUpdate);
    }

    @Test
    public void testDeleteMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        movieReviewFeedbackService.deleteMovieReviewFeedback(movieReviewFeedback.getId());
        Assert.assertFalse(movieReviewFeedbackRepository.existsById(movieReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewFeedbackNotFound() {
        movieReviewFeedbackService.deleteMovieReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovieReviewFeedback() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        put.setUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setMovieReviewId(movieReview.getId());
        put.setIsLiked(true);
        MovieReviewFeedbackReadDTO read =
                movieReviewFeedbackService.putMovieReviewFeedback(movieReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToIgnoringGivenFields(read,
                "userId", "movieId", "movieReviewId");
        Assertions.assertThat(movieReviewFeedback.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewFeedback.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewFeedback.getUserId().getId()).isEqualTo(read.getUserId());
    }

    @Transactional
    @Test
    public void testPutMovieReviewFeedbackEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = testObjectsFactory.createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        MovieReviewFeedbackReadDTO read =
                movieReviewFeedbackService.putMovieReviewFeedback(movieReviewFeedback.getId(), put);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNull(read.getIsLiked());

        MovieReviewFeedback movieReviewFeedbackAfterUpdate = movieReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieId());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getMovieReviewId());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(movieReviewFeedbackAfterUpdate);
    }
}
