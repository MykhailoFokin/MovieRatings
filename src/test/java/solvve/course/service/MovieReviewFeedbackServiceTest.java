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
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_review_feedback; delete from movie_review; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewFeedbackServiceTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

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

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewService movieReviewService;

    private MovieReviewReadDTO movieReviewReadDTO;

    private MovieReviewFeedback createMovieReviewFeedback() {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(portalUserReadDTO.getId());
        movieReviewFeedback.setMovieId(movieReadDTO.getId());
        movieReviewFeedback.setMovieReviewId(movieReviewReadDTO.getId());
        movieReviewFeedback.setIsLiked(true);
        return movieReviewFeedbackRepository.save(movieReviewFeedback);
    }

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
            portalUserReadDTO = portalUserService.getPortalUser(portalUser.getId());
        }

        if (movieReviewReadDTO ==null) {
            MovieReview movieReview = new MovieReview();
            movieReview.setId(UUID.randomUUID());
            movieReview.setUserId(portalUserReadDTO.getId());
            movieReview.setMovieId(movieReadDTO.getId());
            movieReview.setTextReview("This movie can be described as junk.");
            movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
            movieReview.setModeratorId(portalUserReadDTO.getId());
            movieReview = movieReviewRepository.save(movieReview);
            movieReviewReadDTO = movieReviewService.getMovieReview(movieReview.getId());
        }
    }

    @Test
    public void testGetMovieReviewFeedback() {
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback();

        MovieReviewFeedbackReadDTO readDTO = movieReviewFeedbackService.getMovieReviewFeedback(movieReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieReviewFeedback);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewFeedbackWrongId() {
        movieReviewFeedbackService.getMovieReviewFeedback(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReviewFeedback() {
        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setMovieId(movieReadDTO.getId());
        create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setIsLiked(true);

        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.createMovieReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewFeedback movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieReviewFeedback);
    }



    @Test
    public void testPatchMovieReviewFeedback() {
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback();

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setMovieId(movieReadDTO.getId());
        patch.setMovieReviewId(movieReviewReadDTO.getId());
        patch.setIsLiked(true);
        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.patchMovieReviewFeedback(movieReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewFeedbackEmptyPatch() {
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback();

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.patchMovieReviewFeedback(movieReviewFeedback.getId(), patch);

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
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback();

        movieReviewFeedbackService.deleteMovieReviewFeedback(movieReviewFeedback.getId());
        Assert.assertFalse(movieReviewFeedbackRepository.existsById(movieReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewFeedbackNotFound() {
        movieReviewFeedbackService.deleteMovieReviewFeedback(UUID.randomUUID());
    }
}
