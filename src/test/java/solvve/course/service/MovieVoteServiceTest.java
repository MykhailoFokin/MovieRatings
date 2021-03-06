package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteRepository;

import java.util.UUID;

public class MovieVoteServiceTest extends BaseTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private MovieVoteService movieVoteService;

    @Test
    public void testGetMovieVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        MovieVoteReadDTO readDTO = movieVoteService.getMovieVote(movieVote.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieVote,
                "portalUserId","movieId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(movieVote.getPortalUser().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieVote.getMovie().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieVoteWrongId() {
        movieVoteService.getMovieVote(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();

        MovieVoteCreateDTO create = testObjectsFactory.createMovieVoteCreateDTO();
        create.setMovieId(movie.getId());
        create.setPortalUserId(portalUser.getId());
        MovieVoteReadDTO read = movieVoteService.createMovieVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieVote movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieVote,
                "portalUserId","movieId");
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(movieVote.getPortalUser().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieVote.getMovie().getId());
    }

    @Test
    public void testPatchMovieVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        MovieVotePatchDTO patch = testObjectsFactory.createMovieVotePatchDTO();
        patch.setMovieId(movie.getId());
        patch.setPortalUserId(portalUser.getId());
        MovieVoteReadDTO read = movieVoteService.patchMovieVote(movieVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(movieVote).isEqualToIgnoringGivenFields(read,
                "movie","portalUser");
        Assertions.assertThat(movieVote.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(movieVote.getMovie().getId()).isEqualTo(read.getMovieId());
    }

    @Test
    public void testPatchMovieVoteEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        MovieVotePatchDTO patch = new MovieVotePatchDTO();
        MovieVoteReadDTO read = movieVoteService.patchMovieVote(movieVote.getId(), patch);

        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRating());

        MovieVote movieVoteAfterUpdate = movieVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieVoteAfterUpdate.getMovie());
        Assert.assertNotNull(movieVoteAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieVoteAfterUpdate.getRating());

        Assertions.assertThat(movieVote).isEqualToIgnoringGivenFields(movieVoteAfterUpdate, "portalUser", "movie");
        Assertions.assertThat(movieVote.getPortalUser().getId())
                .isEqualTo(movieVoteAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(movieVote.getMovie().getId()).isEqualTo(movieVoteAfterUpdate.getMovie().getId());
    }

    @Test
    public void testDeleteMovieVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        movieVoteService.deleteMovieVote(movieVote.getId());
        Assert.assertFalse(movieVoteRepository.existsById(movieVote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieVoteNotFound() {
        movieVoteService.deleteMovieVote(UUID.randomUUID());
    }

    @Test
    public void testPutMovieVote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        MovieVotePutDTO put = testObjectsFactory.createMovieVotePutDTO();
        put.setMovieId(movie.getId());
        put.setPortalUserId(portalUser.getId());
        MovieVoteReadDTO read = movieVoteService.updateMovieVote(movieVote.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(movieVote).isEqualToIgnoringGivenFields(read,
                "movie","portalUser");
        Assertions.assertThat(movieVote.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(movieVote.getMovie().getId()).isEqualTo(read.getMovieId());
    }

    @Test
    public void testPutMovieVoteEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieVote movieVote = testObjectsFactory.createMovieVote(portalUser, movie);

        MovieVotePutDTO put = new MovieVotePutDTO();
        MovieVoteReadDTO read = movieVoteService.updateMovieVote(movieVote.getId(), put);

        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRating());

        MovieVote movieVoteAfterUpdate = movieVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieVoteAfterUpdate.getMovie().getId());
        Assert.assertNotNull(movieVoteAfterUpdate.getPortalUser().getId());
        Assert.assertNotNull(movieVoteAfterUpdate.getRating());

        Assertions.assertThat(movieVote).isEqualToComparingOnlyGivenFields(movieVoteAfterUpdate, "id");
        Assertions.assertThat(movieVote.getMovie().getId()).isEqualTo(movieVoteAfterUpdate.getMovie().getId());
        Assertions.assertThat(movieVote.getPortalUser().getId())
                .isEqualTo(movieVoteAfterUpdate.getPortalUser().getId());
    }
}
