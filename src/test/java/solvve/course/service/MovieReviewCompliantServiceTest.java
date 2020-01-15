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
@Sql(statements = "delete from movie_review_compliant; delete from movie_review; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieReviewCompliantServiceTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

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

    private MovieReviewCompliant createMovieReviewCompliant() {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(portalUserReadDTO.getId());
        movieReviewCompliant.setMovieId(movieReadDTO.getId());
        movieReviewCompliant.setMovieReviewId(movieReviewReadDTO.getId());
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReviewCompliant.setModeratorId(portalUserReadDTO.getId());
        return movieReviewCompliantRepository.save(movieReviewCompliant);
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
    public void testGetMovieReviewCompliant() {
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant();

        MovieReviewCompliantReadDTO readDTO = movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movieReviewCompliant);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewCompliantWrongId() {
        movieReviewCompliantService.getMovieReviewCompliant(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReviewCompliant() {
        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setMovieId(movieReadDTO.getId());
        create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUserReadDTO.getId());

        MovieReviewCompliantReadDTO read = movieReviewCompliantService.createMovieReviewCompliant(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieReviewCompliant movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movieReviewCompliant);
    }

    @Test
    public void testPatchMovieReviewCompliant() {
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant();

        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        patch.setUserId(portalUserReadDTO.getId());
        patch.setMovieId(movieReadDTO.getId());
        patch.setMovieReviewId(movieReviewReadDTO.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUserReadDTO.getId());
        MovieReviewCompliantReadDTO read = movieReviewCompliantService.patchMovieReviewCompliant(movieReviewCompliant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieReviewCompliant = movieReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(movieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliantEmptyPatch() {
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant();

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
        MovieReviewCompliant movieReviewCompliant = createMovieReviewCompliant();

        movieReviewCompliantService.deleteMovieReviewCompliant(movieReviewCompliant.getId());
        Assert.assertFalse(movieReviewCompliantRepository.existsById(movieReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewCompliantNotFound() {
        movieReviewCompliantService.deleteMovieReviewCompliant(UUID.randomUUID());
    }
}
