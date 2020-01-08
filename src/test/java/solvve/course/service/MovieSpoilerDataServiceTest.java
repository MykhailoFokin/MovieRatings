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
import solvve.course.repository.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_spoiler_data; delete from movie_review; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieSpoilerDataServiceTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewService movieReviewService;

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

    private MovieReviewReadDTO movieReviewReadDTO;

    @Before
    public void setup() {
        if (movieReviewReadDTO==null) {
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

            MovieReviewCreateDTO create = new MovieReviewCreateDTO();
            MovieReview movieReview = new MovieReview();
            movieReview.setUserId(portalUserReadDTO.getId());
            movieReview.setMovieId(movieReadDTO.getId());
            movieReview.setTextReview("This movie can be described as junk.");
            movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            movieReview = movieReviewRepository.save(movieReview);
            movieReviewReadDTO = movieReviewService.getMovieReview(movieReview.getId());
        }
    }

    @Test
    public void testGetMovieSpoilerData() {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setId(UUID.randomUUID());
        movieSpoilerData.setMovieReviewId(movieReviewReadDTO.getId());
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        MovieSpoilerDataReadDTO readDTO = movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieSpoilerData);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieSpoilerDataWrongId() {
        movieSpoilerDataService.getMovieSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieSpoilerData() {
        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        MovieSpoilerDataReadDTO read = movieSpoilerDataService.createMovieSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieSpoilerData movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieSpoilerData);
    }
}
