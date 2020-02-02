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
@Sql(statements = {"delete from movie_spoiler_data",
        " delete from movie_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieSpoilerDataServiceTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private MovieSpoilerData createMovieSpoilerData(MovieReview movieReview) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(movieReview);
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);
        return movieSpoilerDataRepository.save(movieSpoilerData);
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

    public MovieReview createMovieReview(PortalUser portalUser, Movie movie) {
        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUser);
        movieReview.setMovieId(movie);
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview = movieReviewRepository.save(movieReview);

        return movieReview;
    }

    @Transactional
    @Test
    public void testGetMovieSpoilerData() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

        MovieSpoilerDataReadDTO readDTO = movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReviewId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieSpoilerDataWrongId() {
        movieSpoilerDataService.getMovieSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieSpoilerData() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        create.setMovieReviewId(movieReview.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        MovieSpoilerDataReadDTO read = movieSpoilerDataService.createMovieSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieSpoilerData movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReviewId().getId());
    }

    @Transactional
    @Test
    public void testPatchMovieSpoilerData() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        patch.setMovieReviewId(movieReview.getId());
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReviewId");
        Assertions.assertThat(movieSpoilerData.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
    }

    @Transactional
    @Test
    public void testPatchMovieSpoilerDataEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

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
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

        movieSpoilerDataService.deleteMovieSpoilerData(movieSpoilerData.getId());
        Assert.assertFalse(movieSpoilerDataRepository.existsById(movieSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieSpoilerDataNotFound() {
        movieSpoilerDataService.deleteMovieSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovieSpoilerData() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();
        put.setMovieReviewId(movieReview.getId());
        put.setStartIndex(100);
        put.setEndIndex(150);
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.putMovieSpoilerData(movieSpoilerData.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReviewId");
        Assertions.assertThat(movieSpoilerData.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
    }

    @Transactional
    @Test
    public void testPutMovieSpoilerDataEmptyPut() {
        PortalUser portalUser = createPortalUser();
        Movie movie = createMovie();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = createMovieSpoilerData(movieReview);

        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.putMovieSpoilerData(movieSpoilerData.getId(), put);

        Assert.assertNull(read.getMovieReviewId());
        Assert.assertNull(read.getStartIndex());
        Assert.assertNull(read.getEndIndex());

        MovieSpoilerData movieSpoilerDataAfterUpdate = movieSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNull(movieSpoilerDataAfterUpdate.getMovieReviewId().getId());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(movieSpoilerData).isEqualToComparingFieldByField(movieSpoilerDataAfterUpdate);
    }
}
