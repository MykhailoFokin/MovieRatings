package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;
import solvve.course.repository.MovieVoteRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_vote; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieVoteServiceTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private MovieVoteService movieVoteService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    private PortalUserReadDTO portalUserReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    private MovieReadDTO movieReadDTO;

    @Before
    public void setup() {
        if (movieReadDTO==null) {
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
            //MovieReadDTO readDTO = movieService.getMovie(movie.getId());
            movieReadDTO = movieService.getMovie(movie.getId());
        }

        if (portalUserReadDTO ==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            PortalUser portalUser = new PortalUser();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userTypes.getId());
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
            portalUserReadDTO = portalUserService.getPortalUsers(portalUser.getId());
        }
    }

    @Test
    public void testGetMovieVote() {
        MovieVote movieVote = new MovieVote();
        movieVote.setId(UUID.randomUUID());
        movieVote.setMovieId(movieReadDTO.getId());
        movieVote.setUserId(portalUserReadDTO.getId());
        movieVote.setRating(UserVoteRatingType.valueOf("R9"));
        movieVote = movieVoteRepository.save(movieVote);

        MovieVoteReadDTO readDTO = movieVoteService.getMovieVote(movieVote.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieVote);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieVoteWrongId() {
        movieVoteService.getMovieVote(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieVote() {
        MovieVoteCreateDTO create = new MovieVoteCreateDTO();
        create.setMovieId(movieReadDTO.getId());
        create.setUserId(portalUserReadDTO.getId());
        create.setRating(UserVoteRatingType.valueOf("R9"));
        MovieVoteReadDTO read = movieVoteService.createMovieVote(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieVote movieVote = movieVoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieVote);
    }
}
