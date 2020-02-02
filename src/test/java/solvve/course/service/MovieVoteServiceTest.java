package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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
import solvve.course.repository.MovieRepository;
import solvve.course.repository.MovieVoteRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_vote",
        " delete from portal_user",
        " delete from user_type",
        " delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieVoteServiceTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private MovieVoteService movieVoteService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private MovieVote createMovieVote(PortalUser portalUser, Movie movie) {
        MovieVote movieVote = new MovieVote();
        movieVote.setMovieId(movie);
        movieVote.setUserId(portalUser);
        movieVote.setRating(UserVoteRatingType.R9);
        return movieVoteRepository.save(movieVote);
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        //movie.setId(UUID.randomUUID());
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setGenres("Comedy");
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCompanies("Paramount");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setFilmingLocations("USA");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setLanguages("English");
        movie.setSoundMix("DolbySurround");
        movie = movieRepository.save(movie);
        return movie;
    }

    private PortalUser createPortalUser() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setLogin("Login");
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setUserType(userType);
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    @Transactional
    @Test
    public void testGetMovieVote() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        MovieVoteReadDTO readDTO = movieVoteService.getMovieVote(movieVote.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieVote,
                "userId","movieId");
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(movieVote.getUserId().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieVote.getMovieId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieVoteWrongId() {
        movieVoteService.getMovieVote(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieVote() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();

        MovieVoteCreateDTO create = new MovieVoteCreateDTO();
        create.setMovieId(movie.getId());
        create.setUserId(portalUser.getId());
        create.setRating(UserVoteRatingType.R9);
        MovieVoteReadDTO read = movieVoteService.createMovieVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieVote movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieVote,
                "userId","movieId");
        Assertions.assertThat(read.getUserId()).isEqualTo(movieVote.getUserId().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieVote.getMovieId().getId());
    }

    @Transactional
    @Test
    public void testPatchMovieVote() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        MovieVotePatchDTO patch = new MovieVotePatchDTO();
        patch.setMovieId(movie.getId());
        patch.setUserId(portalUser.getId());
        patch.setRating(UserVoteRatingType.R9);
        MovieVoteReadDTO read = movieVoteService.patchMovieVote(movieVote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(movieVote).isEqualToIgnoringGivenFields(read,
                "movieId","userId");
        Assertions.assertThat(movieVote.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(movieVote.getMovieId().getId()).isEqualTo(read.getMovieId());
    }

    @Transactional
    @Test
    public void testPatchMovieVoteEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        MovieVotePatchDTO patch = new MovieVotePatchDTO();
        MovieVoteReadDTO read = movieVoteService.patchMovieVote(movieVote.getId(), patch);

        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getRating());

        MovieVote movieVoteAfterUpdate = movieVoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieVoteAfterUpdate.getMovieId());
        Assert.assertNotNull(movieVoteAfterUpdate.getUserId());
        Assert.assertNotNull(movieVoteAfterUpdate.getRating());

        Assertions.assertThat(movieVote).isEqualToComparingFieldByField(movieVoteAfterUpdate);
    }

    @Test
    public void testDeleteMovieVote() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        movieVoteService.deleteMovieVote(movieVote.getId());
        Assert.assertFalse(movieVoteRepository.existsById(movieVote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieVoteNotFound() {
        movieVoteService.deleteMovieVote(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovieVote() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        MovieVotePutDTO put = new MovieVotePutDTO();
        put.setMovieId(movie.getId());
        put.setUserId(portalUser.getId());
        put.setRating(UserVoteRatingType.R9);
        MovieVoteReadDTO read = movieVoteService.putMovieVote(movieVote.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(movieVote).isEqualToIgnoringGivenFields(read,
                "movieId","userId");
        Assertions.assertThat(movieVote.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(movieVote.getMovieId().getId()).isEqualTo(read.getMovieId());
    }

    @Transactional
    @Test
    public void testPutMovieVoteEmptyPut() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieVote movieVote = createMovieVote(portalUser, movie);

        MovieVotePutDTO put = new MovieVotePutDTO();
        MovieVoteReadDTO read = movieVoteService.putMovieVote(movieVote.getId(), put);

        Assert.assertNull(read.getMovieId());
        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getRating());

        MovieVote movieVoteAfterUpdate = movieVoteRepository.findById(read.getId()).get();

        Assert.assertNull(movieVoteAfterUpdate.getMovieId().getId());
        Assert.assertNull(movieVoteAfterUpdate.getUserId().getId());
        Assert.assertNull(movieVoteAfterUpdate.getRating());

        Assertions.assertThat(movieVote).isEqualToComparingFieldByField(movieVoteAfterUpdate);
    }
}
