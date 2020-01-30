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
@Sql(statements = "delete from movie_review_feedback; delete from movie_review; delete from portal_user; delete from user_type; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewFeedbackServiceTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    private MovieReviewFeedback createMovieReviewFeedback(PortalUser portalUser, Movie movie, MovieReview movieReview) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(portalUser);
        movieReviewFeedback.setMovieId(movie);
        movieReviewFeedback.setMovieReviewId(movieReview);
        movieReviewFeedback.setIsLiked(true);
        return movieReviewFeedbackRepository.save(movieReviewFeedback);
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

    private MovieReview createMovieReview(PortalUser portalUser, Movie movie) {
        MovieReview movieReview = new MovieReview();
        movieReview.setId(UUID.randomUUID());
        movieReview.setUserId(portalUser);
        movieReview.setMovieId(movie);
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview.setModeratorId(portalUser);
        movieReview = movieReviewRepository.save(movieReview);
        return movieReview;
    }

    @Transactional
    @Test
    public void testGetMovieReviewFeedback() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackReadDTO readDTO = movieReviewFeedbackService.getMovieReviewFeedback(movieReviewFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieReviewFeedback);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewFeedbackWrongId() {
        movieReviewFeedbackService.getMovieReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieReviewFeedback() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        create.setUserId(portalUser);
        create.setMovieId(movie);
        create.setMovieReviewId(movieReview);
        create.setIsLiked(true);

        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.createMovieReviewFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewFeedback movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieReviewFeedback);
    }

    @Transactional
    @Test
    public void testPatchMovieReviewFeedback() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPatchDTO patch = new MovieReviewFeedbackPatchDTO();
        patch.setUserId(portalUser);
        patch.setMovieId(movie);
        patch.setMovieReviewId(movieReview);
        patch.setIsLiked(true);
        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.patchMovieReviewFeedback(movieReviewFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchMovieReviewFeedbackEmptyPatch() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

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
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

        movieReviewFeedbackService.deleteMovieReviewFeedback(movieReviewFeedback.getId());
        Assert.assertFalse(movieReviewFeedbackRepository.existsById(movieReviewFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewFeedbackNotFound() {
        movieReviewFeedbackService.deleteMovieReviewFeedback(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovieReviewFeedback() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        put.setUserId(portalUser);
        put.setMovieId(movie);
        put.setMovieReviewId(movieReview);
        put.setIsLiked(true);
        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.putMovieReviewFeedback(movieReviewFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReviewFeedback = movieReviewFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPutMovieReviewFeedbackEmptyPut() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewFeedback movieReviewFeedback = createMovieReviewFeedback(portalUser, movie, movieReview);

        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();
        MovieReviewFeedbackReadDTO read = movieReviewFeedbackService.putMovieReviewFeedback(movieReviewFeedback.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getMovieId());
        Assert.assertNull(read.getMovieReviewId());
        Assert.assertNull(read.getIsLiked());

        MovieReviewFeedback movieReviewFeedbackAfterUpdate = movieReviewFeedbackRepository.findById(read.getId()).get();

        Assert.assertNull(movieReviewFeedbackAfterUpdate.getUserId());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getMovieId());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getMovieReviewId());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(movieReviewFeedback).isEqualToComparingFieldByField(movieReviewFeedbackAfterUpdate);
    }
}
