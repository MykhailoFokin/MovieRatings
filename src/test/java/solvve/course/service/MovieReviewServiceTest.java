package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewRepository;

import java.util.UUID;

public class MovieReviewServiceTest extends BaseTest {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewService movieReviewService;

    @Test
    public void testGetMovieReview() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewReadDTO readDTO = movieReviewService.getMovieReview(movieReview.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieReview,
                "portalUserId","movieId","moderatorId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(movieReview.getPortalUser().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieReview.getMovie().getId());
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(movieReview.getModerator().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewWrongId() {
        movieReviewService.getMovieReview(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReview() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();

        MovieReviewCreateDTO create = new MovieReviewCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setTextReview("This movie can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        MovieReviewReadDTO read = movieReviewService.createMovieReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReview movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReview,
                "portalUserId","movieId","moderatorId");
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(movieReview.getPortalUser().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReview.getMovie().getId());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(movieReview.getModerator().getId());
    }

    @Test
    public void testPatchMovieReview() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewPatchDTO patch = new MovieReviewPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setTextReview("This movie can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        MovieReviewReadDTO read = movieReviewService.patchMovieReview(movieReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReview).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "moderator",
                "movieReviewCompliants","movieReviewFeedbacks","movieSpoilerData");
        Assertions.assertThat(movieReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(movieReview.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReview.getModerator().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPatchMovieReviewEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewPatchDTO patch = new MovieReviewPatchDTO();
        MovieReviewReadDTO read = movieReviewService.patchMovieReview(movieReview.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getTextReview());
        Assert.assertNotNull(read.getModeratorId());
        Assert.assertNotNull(read.getModeratedStatus());

        MovieReview movieReviewAfterUpdate = movieReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewAfterUpdate.getMovie());
        Assert.assertNotNull(movieReviewAfterUpdate.getTextReview());
        Assert.assertNotNull(movieReviewAfterUpdate.getModerator());
        Assert.assertNotNull(movieReviewAfterUpdate.getModeratedStatus());

        Assertions.assertThat(movieReview).isEqualToIgnoringGivenFields(movieReviewAfterUpdate,
                "portalUser", "movie", "moderator",
                "movieReviewCompliants","movieReviewFeedbacks","movieSpoilerData");
        Assertions.assertThat(movieReview.getPortalUser().getId())
                .isEqualTo(movieReviewAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(movieReview.getMovie().getId()).isEqualTo(movieReviewAfterUpdate.getMovie().getId());
        Assertions.assertThat(movieReview.getModerator().getId())
                .isEqualTo(movieReviewAfterUpdate.getModerator().getId());
    }

    @Test
    public void testDeleteMovieReview() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        movieReviewService.deleteMovieReview(movieReview.getId());
        Assert.assertFalse(movieReviewRepository.existsById(movieReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewNotFound() {
        movieReviewService.deleteMovieReview(UUID.randomUUID());
    }

    @Test
    public void testPutMovieReview() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewPutDTO put = new MovieReviewPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setTextReview("This movie can be described as junk.");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        MovieReviewReadDTO read = movieReviewService.updateMovieReview(movieReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReview).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "moderator",
                "movieReviewCompliants","movieReviewFeedbacks","movieSpoilerData");
        Assertions.assertThat(movieReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(movieReview.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReview.getModerator().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPutMovieReviewEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewPutDTO put = new MovieReviewPutDTO();
        MovieReviewReadDTO read = movieReviewService.updateMovieReview(movieReview.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getTextReview());
        Assert.assertNull(read.getModeratorId());
        Assert.assertNull(read.getModeratedStatus());

        MovieReview movieReviewAfterUpdate = movieReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewAfterUpdate.getMovie());
        Assert.assertNotNull(movieReviewAfterUpdate.getTextReview());
        Assert.assertNull(movieReviewAfterUpdate.getModerator());
        Assert.assertNull(movieReviewAfterUpdate.getModeratedStatus());
    }
}
