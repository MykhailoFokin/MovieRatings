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
@Sql(statements = {"delete from movie_review_compliant",
        " delete from movie_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewCompliantServiceTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    private MovieReviewCompliant createMovieReviewCompliant(PortalUser portalUser, Movie movie, MovieReview movieReview) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(portalUser);
        movieReviewCompliant.setMovieId(movie);
        movieReviewCompliant.setMovieReviewId(movieReview);
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReviewCompliant.setModeratorId(portalUser);
        return movieReviewCompliantRepository.save(movieReviewCompliant);
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
    public void testGetMovieReviewCompliant() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantReadDTO readDTO = movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieReviewCompliant, "userId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(movieReviewCompliant.getUserId().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieReviewCompliant.getMovieId().getId());
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieReviewCompliant.getMovieReviewId().getId());
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(movieReviewCompliant.getModeratorId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewCompliantWrongId() {
        movieReviewCompliantService.getMovieReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovieReviewCompliant() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setMovieReviewId(movieReview.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        MovieReviewCompliantReadDTO read = movieReviewCompliantService.createMovieReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewCompliant movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReviewCompliant, "userId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(read.getUserId()).isEqualTo(movieReviewCompliant.getUserId().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReviewCompliant.getMovieId().getId());
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieReviewCompliant.getMovieReviewId().getId());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(movieReviewCompliant.getModeratorId().getId());
    }

    @Transactional
    @Test
    public void testPatchMovieReviewCompliant() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setMovieReviewId(movieReview.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        MovieReviewCompliantReadDTO read = movieReviewCompliantService.patchMovieReviewCompliant(movieReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewCompliant).isEqualToIgnoringGivenFields(read, "userId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(movieReviewCompliant.getUserId().getId()).isEqualToComparingFieldByField(read.getUserId());
        Assertions.assertThat(movieReviewCompliant.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewCompliant.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewCompliant.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Transactional
    @Test
    public void testPatchMovieReviewCompliantEmptyPatch() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        MovieReviewCompliantReadDTO read = movieReviewCompliantService.patchMovieReviewCompliant(movieReviewCompliant.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        MovieReviewCompliant movieReviewCompliantAfterUpdate = movieReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getUserId());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovieId());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getMovieReviewId());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getDescription());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(movieReviewCompliantAfterUpdate.getModeratorId());

        Assertions.assertThat(movieReviewCompliant).isEqualToComparingFieldByField(movieReviewCompliantAfterUpdate);
    }

    @Test
    public void testDeleteMovieReviewCompliant() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        movieReviewCompliantService.deleteMovieReviewCompliant(movieReviewCompliant.getId());
        Assert.assertFalse(movieReviewCompliantRepository.existsById(movieReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewCompliantNotFound() {
        movieReviewCompliantService.deleteMovieReviewCompliant(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovieReviewCompliant() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        put.setUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setMovieReviewId(movieReview.getId());
        put.setDescription("Just punish him!");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        MovieReviewCompliantReadDTO read = movieReviewCompliantService.putMovieReviewCompliant(movieReviewCompliant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewCompliant).isEqualToIgnoringGivenFields(read, "userId", "movieId", "movieReviewId", "moderatorId");
        Assertions.assertThat(movieReviewCompliant.getUserId().getId()).isEqualToComparingFieldByField(read.getUserId());
        Assertions.assertThat(movieReviewCompliant.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReviewCompliant.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
        Assertions.assertThat(movieReviewCompliant.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Transactional
    @Test
    public void testPutMovieReviewCompliantEmptyPut() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant(portalUser, movie, movieReview);

        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        MovieReviewCompliantReadDTO read = movieReviewCompliantService.putMovieReviewCompliant(movieReviewCompliant.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getMovieId());
        Assert.assertNull(read.getMovieReviewId());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        MovieReviewCompliant movieReviewCompliantAfterUpdate = movieReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNull(movieReviewCompliantAfterUpdate.getUserId().getId());
        Assert.assertNull(movieReviewCompliantAfterUpdate.getMovieId().getId());
        Assert.assertNull(movieReviewCompliantAfterUpdate.getMovieReviewId().getId());
        Assert.assertNull(movieReviewCompliantAfterUpdate.getDescription());
        Assert.assertNull(movieReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNull(movieReviewCompliantAfterUpdate.getModeratorId().getId());

        Assertions.assertThat(movieReviewCompliant).isEqualToComparingFieldByField(movieReviewCompliantAfterUpdate);
    }
}
