package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

public class MovieReviewCompliantServiceTest extends BaseTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testGetMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantReadDTO readDTO =
                movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieReviewCompliant,
                "portalUserId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(movieReviewCompliant.getPortalUser().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieReviewCompliant.getMovie().getId());
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieReviewCompliant.getMovieReview().getId());
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(movieReviewCompliant.getModerator().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewCompliantWrongId() {
        movieReviewCompliantService.getMovieReviewCompliant(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setMovieReviewId(movieReview.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        MovieReviewCompliantReadDTO read = movieReviewCompliantService.createMovieReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewCompliant movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReviewCompliant,
                "portalUserId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(movieReviewCompliant.getPortalUser().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReviewCompliant.getMovie().getId());
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieReviewCompliant.getMovieReview().getId());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(movieReviewCompliant.getModerator().getId());
    }

    @Test
    public void testPatchMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setMovieReviewId(movieReview.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        MovieReviewCompliantReadDTO read =
                movieReviewCompliantService.patchMovieReviewCompliant(movieReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewCompliant).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "movieReview", "moderator");
        Assertions.assertThat(movieReviewCompliant.getPortalUser().getId())
                .isEqualToComparingFieldByField(read.getPortalUserId());
        Assertions.assertThat(movieReviewCompliant.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewCompliant.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewCompliant.getModerator().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPatchMovieReviewCompliantEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        MovieReviewCompliantReadDTO read =
                movieReviewCompliantService.patchMovieReviewCompliant(movieReviewCompliant.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        MovieReviewCompliant movieReviewCompliantAfterUpdate =
                movieReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovie());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovieReview());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getDescription());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getModerator());

        Assertions.assertThat(movieReviewCompliant).isEqualToIgnoringGivenFields(movieReviewCompliantAfterUpdate,
                "portalUser", "movie", "movieReview", "moderator");
        Assertions.assertThat(movieReviewCompliant.getPortalUser().getId())
                .isEqualTo(movieReviewCompliantAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(movieReviewCompliant.getMovie().getId())
                .isEqualTo(movieReviewCompliantAfterUpdate.getMovie().getId());
        Assertions.assertThat(movieReviewCompliant.getMovieReview().getId())
                .isEqualTo(movieReviewCompliantAfterUpdate.getMovieReview().getId());
        Assertions.assertThat(movieReviewCompliant.getModerator().getId())
                .isEqualTo(movieReviewCompliantAfterUpdate.getModerator().getId());
    }

    @Test
    public void testDeleteMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        movieReviewCompliantService.deleteMovieReviewCompliant(movieReviewCompliant.getId());
        Assert.assertFalse(movieReviewCompliantRepository.existsById(movieReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewCompliantNotFound() {
        movieReviewCompliantService.deleteMovieReviewCompliant(UUID.randomUUID());
    }

    @Test
    public void testPutMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setMovieReviewId(movieReview.getId());
        put.setDescription("Just punish him!");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        MovieReviewCompliantReadDTO read =
                movieReviewCompliantService.updateMovieReviewCompliant(movieReviewCompliant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewCompliant).isEqualToIgnoringGivenFields(read,
                "portalUser", "movie", "movieReview", "moderator");
        Assertions.assertThat(movieReviewCompliant.getPortalUser().getId())
                .isEqualToComparingFieldByField(read.getPortalUserId());
        Assertions.assertThat(movieReviewCompliant.getMovie().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewCompliant.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewCompliant.getModerator().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    public void testPutMovieReviewCompliantEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        portalUser.getMovieReviewCompliantsModerator().remove(portalUser);
        portalUserRepository.save(portalUser);
        MovieReviewCompliantReadDTO read =
                movieReviewCompliantService.updateMovieReviewCompliant(movieReviewCompliant.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        testObjectsFactory.inTransaction(()-> {
            MovieReviewCompliant movieReviewCompliantAfterUpdate =
                    movieReviewCompliantRepository.findById(read.getId()).get();

            Assert.assertNotNull(movieReviewCompliantAfterUpdate.getPortalUser().getId());
            Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovie().getId());
            Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovieReview().getId());
            Assert.assertNull(movieReviewCompliantAfterUpdate.getDescription());
            Assert.assertNull(movieReviewCompliantAfterUpdate.getModeratedStatus());
            Assert.assertNull(movieReviewCompliantAfterUpdate.getModerator());

            Assertions.assertThat(movieReviewCompliant.getPortalUser().getId())
                    .isEqualTo(movieReviewCompliantAfterUpdate.getPortalUser().getId());
            Assertions.assertThat(movieReviewCompliant.getMovieReview().getId())
                    .isEqualTo(movieReviewCompliantAfterUpdate.getMovieReview().getId());
            Assertions.assertThat(movieReviewCompliant.getMovie().getId())
                    .isEqualTo(movieReviewCompliantAfterUpdate.getMovie().getId());
        });
    }
}
