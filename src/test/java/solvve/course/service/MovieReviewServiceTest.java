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
import solvve.course.repository.MovieRepository;
import solvve.course.repository.MovieReviewRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie_review; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewServiceTest {

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

    private MovieReview createMovieReview() {
        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUserReadDTO.getId());
        movieReview.setMovieId(movieReadDTO.getId());
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview.setModeratorId(portalUserReadDTO.getId());
        return movieReviewRepository.save(movieReview);
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
    }

    @Test
    public void testGetMovieReview() {
        MovieReview movieReview = createMovieReview();

        MovieReviewReadDTO readDTO = movieReviewService.getMovieReview(movieReview.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieReview);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewWrongId() {
        movieReviewService.getMovieReview(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReview() {
        MovieReviewCreateDTO create = new MovieReviewCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setMovieId(movieReadDTO.getId());
        create.setTextReview("This movie can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUserReadDTO.getId());

        MovieReviewReadDTO read = movieReviewService.createMovieReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReview movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieReview);
    }

    @Test
    public void testPatchMovieReview() {
        MovieReview movieReview = createMovieReview();

        MovieReviewPatchDTO patch = new MovieReviewPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setMovieId(movieReadDTO.getId());
        patch.setTextReview("This movie can be described as junk.");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUserReadDTO.getId());
        MovieReviewReadDTO read = movieReviewService.patchMovieReview(movieReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReview = movieReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewEmptyPatch() {
        MovieReview movieReview = createMovieReview();

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
    public void testDeleteMovieReview() {
        MovieReview movieReview = createMovieReview();

        movieReviewService.deleteMovieReview(movieReview.getId());
        Assert.assertFalse(movieReviewRepository.existsById(movieReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewNotFound() {
        movieReviewService.deleteMovieReview(UUID.randomUUID());
    }
}
