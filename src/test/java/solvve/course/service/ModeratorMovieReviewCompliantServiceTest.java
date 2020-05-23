package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.domain.PortalUser;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewCompliantRepository;
import solvve.course.repository.MovieReviewRepository;

import java.util.List;
import java.util.UUID;

public class ModeratorMovieReviewCompliantServiceTest extends BaseTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private ModeratorMovieReviewCompliantService moderatorMovieReviewCompliantService;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Test
    public void testGetMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        List<MovieReviewCompliantReadDTO> readDTO =
                moderatorMovieReviewCompliantService.getCreatedMovieReviewCompliants(portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(movieReviewCompliant.getId());
    }

    @Test
    public void testGetMovieReviewCompliantWrongId() {
        List<MovieReviewCompliantReadDTO> readDTO =
                moderatorMovieReviewCompliantService.getCreatedMovieReviewCompliants(UUID.randomUUID());
        Assertions.assertThat(readDTO.size()).isEqualTo(0);
    }

    @Test
    public void testPatchMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant =
                testObjectsFactory.createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPatchDTO patch = testObjectsFactory.createMovieReviewCompliantPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setMovieReviewId(movieReview.getId());
        patch.setModeratorId(portalUser.getId());
        MovieReviewCompliantReadDTO read =
                moderatorMovieReviewCompliantService.patchMovieReviewCompliantByModeratedStatus(
                        movieReviewCompliant.getId(), patch, portalUser.getId());

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
                moderatorMovieReviewCompliantService.patchMovieReviewCompliantByModeratedStatus(
                        movieReviewCompliant.getId(), patch, portalUser.getId());

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

        moderatorMovieReviewCompliantService.deleteMovieReviewByCompliantByModerator(portalUser.getId(),
                movieReviewCompliant.getId());

        Assert.assertFalse(movieReviewCompliantRepository.existsById(movieReviewCompliant.getId()));
        Assert.assertFalse(movieReviewRepository.existsById(movieReviewCompliant.getMovieReview().getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewCompliantNotFound() {
        moderatorMovieReviewCompliantService.deleteMovieReviewByCompliantByModerator(UUID.randomUUID(),
                UUID.randomUUID());
    }
}
