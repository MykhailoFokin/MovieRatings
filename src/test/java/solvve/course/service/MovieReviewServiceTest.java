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
import solvve.course.repository.MovieReviewRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_review; delete from portal_user; delete from user_type; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewServiceTest {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewService movieReviewService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private MovieReview createMovieReview(PortalUser portalUser, Movie movie) {
        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUser);
        movieReview.setMovieId(movie);
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview.setModeratorId(portalUser);
        return movieReviewRepository.save(movieReview);
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

    @Test
    @Transactional
    public void testGetMovieReview() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewReadDTO readDTO = movieReviewService.getMovieReview(movieReview.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieReview,"userId","movieId","moderatorId");
        Assertions.assertThat(readDTO.getUserId()).isEqualTo(movieReview.getUserId().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(movieReview.getMovieId().getId());
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(movieReview.getModeratorId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewWrongId() {
        movieReviewService.getMovieReview(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testCreateMovieReview() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();

        MovieReviewCreateDTO create = new MovieReviewCreateDTO();
        create.setUserId(portalUser.getId());
        create.setMovieId(movie.getId());
        create.setTextReview("This movie can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        MovieReviewReadDTO read = movieReviewService.createMovieReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReview movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReview,"userId","movieId","moderatorId");
        Assertions.assertThat(read.getUserId()).isEqualTo(movieReview.getUserId().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(movieReview.getMovieId().getId());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(movieReview.getModeratorId().getId());
    }

    @Test
    @Transactional
    public void testPatchMovieReview() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewPatchDTO patch = new MovieReviewPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setMovieId(movie.getId());
        patch.setTextReview("This movie can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        MovieReviewReadDTO read = movieReviewService.patchMovieReview(movieReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReview).isEqualToIgnoringGivenFields(read,"userId", "movieId", "moderatorId",
                "movieReviewCompliants","movieReviewFeedbacks","movieSpoilerData");
        Assertions.assertThat(movieReview.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(movieReview.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReview.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    @Transactional
    public void testPatchMovieReviewEmptyPatch() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewPatchDTO patch = new MovieReviewPatchDTO();
        MovieReviewReadDTO read = movieReviewService.patchMovieReview(movieReview.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getTextReview());
        Assert.assertNotNull(read.getModeratorId());
        Assert.assertNotNull(read.getModeratedStatus());

        MovieReview movieReviewAfterUpdate = movieReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewAfterUpdate.getUserId());
        Assert.assertNotNull(movieReviewAfterUpdate.getMovieId());
        Assert.assertNotNull(movieReviewAfterUpdate.getTextReview());
        Assert.assertNotNull(movieReviewAfterUpdate.getModeratorId());
        Assert.assertNotNull(movieReviewAfterUpdate.getModeratedStatus());

        Assertions.assertThat(movieReview).isEqualToComparingFieldByField(movieReviewAfterUpdate);
    }

    @Test
    @Transactional
    public void testDeleteMovieReview() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        movieReviewService.deleteMovieReview(movieReview.getId());
        Assert.assertFalse(movieReviewRepository.existsById(movieReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewNotFound() {
        movieReviewService.deleteMovieReview(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testPutMovieReview() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewPutDTO put = new MovieReviewPutDTO();
        put.setUserId(portalUser.getId());
        put.setMovieId(movie.getId());
        put.setTextReview("This movie can be described as junk.");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        MovieReviewReadDTO read = movieReviewService.putMovieReview(movieReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReview).isEqualToIgnoringGivenFields(read, "userId", "movieId", "moderatorId",
                "movieReviewCompliants","movieReviewFeedbacks","movieSpoilerData");
        Assertions.assertThat(movieReview.getUserId().getId()).isEqualTo(read.getUserId());
        Assertions.assertThat(movieReview.getMovieId().getId()).isEqualTo(read.getMovieId());
        Assertions.assertThat(movieReview.getModeratorId().getId()).isEqualTo(read.getModeratorId());
    }

    @Test
    @Transactional
    public void testPutMovieReviewEmptyPut() {
        Movie movie = createMovie();
        PortalUser portalUser = createPortalUser();
        MovieReview movieReview = createMovieReview(portalUser, movie);

        MovieReviewPutDTO put = new MovieReviewPutDTO();
        MovieReviewReadDTO read = movieReviewService.putMovieReview(movieReview.getId(), put);

        Assert.assertNull(read.getUserId());
        Assert.assertNull(read.getMovieId());
        Assert.assertNull(read.getTextReview());
        Assert.assertNull(read.getModeratorId());
        Assert.assertNull(read.getModeratedStatus());

        MovieReview movieReviewAfterUpdate = movieReviewRepository.findById(read.getId()).get();

        Assert.assertNull(movieReviewAfterUpdate.getUserId().getId());
        Assert.assertNull(movieReviewAfterUpdate.getMovieId().getId());
        Assert.assertNull(movieReviewAfterUpdate.getTextReview());
        Assert.assertNull(movieReviewAfterUpdate.getModeratorId().getId());
        Assert.assertNull(movieReviewAfterUpdate.getModeratedStatus());

        Assertions.assertThat(movieReview).isEqualToComparingFieldByField(movieReviewAfterUpdate);
    }
}
