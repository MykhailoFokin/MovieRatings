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
import solvve.course.repository.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_spoiler_data; delete from movie_review; delete from portal_user; delete from user_type; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    @Autowired
    private UserTypeRepository userTypeRepository;

    private MovieReview movieReview;

    private MovieSpoilerData createMovieSpoilerData() {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(movieReview);
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);
        return movieSpoilerDataRepository.save(movieSpoilerData);
    }

    @Before
    public void setup() {
        if (movieReview==null) {
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

            movieReview = new MovieReview();
            movieReview.setUserId(portalUser);
            movieReview.setMovieId(movie);
            movieReview.setTextReview("This movie can be described as junk.");
            movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            movieReview = movieReviewRepository.save(movieReview);
        }
    }

    @Transactional
    @Test
    public void testGetMovieSpoilerData() {
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData();

        MovieSpoilerDataReadDTO readDTO = movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieSpoilerData);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieSpoilerDataWrongId() {
        movieSpoilerDataService.getMovieSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieSpoilerData() {
        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        create.setMovieReviewId(movieReview);
        create.setStartIndex(100);
        create.setEndIndex(150);

        MovieSpoilerDataReadDTO read = movieSpoilerDataService.createMovieSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieSpoilerData movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieSpoilerData);
    }

    @Transactional
    @Test
    public void testPatchMovieSpoilerData() {
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData();

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        patch.setMovieReviewId(movieReview);
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchMovieSpoilerDataEmptyPatch() {
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData();

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());

        MovieSpoilerData movieSpoilerDataAfterUpdate = movieSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReviewId());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(movieSpoilerData).isEqualToComparingFieldByField(movieSpoilerDataAfterUpdate);
    }

    @Test
    public void testDeleteMovieSpoilerData() {
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData();

        movieSpoilerDataService.deleteMovieSpoilerData(movieSpoilerData.getId());
        Assert.assertFalse(movieSpoilerDataRepository.existsById(movieSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieSpoilerDataNotFound() {
        movieSpoilerDataService.deleteMovieSpoilerData(UUID.randomUUID());
    }
}
