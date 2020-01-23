package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_review_feedback; delete from movie_review; delete from portal_user; delete from user_types; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieReviewFeedbackRepositoryTest {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional
    @Test
    public void testSave() {
        Movie movie = new Movie();
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
        movie.setIsPublished(true);
        movie = movieRepository.save(movie);

        UserTypes userTypes = new UserTypes();
        userTypes.setUserGroup(UserGroupType.USER);
        userTypes = userTypesRepository.save(userTypes);

        PortalUser portalUser = new PortalUser();
        portalUser.setName("Name");
        portalUser.setUserType(userTypes);
        portalUser = portalUserRepository.save(portalUser);

        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUser);
        movieReview.setMovieId(movie);
        movieReview.setModeratorId(portalUser);
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview.setTextReview("sdsdsdsd");
        movieReview = movieReviewRepository.save(movieReview);

        MovieReviewFeedback r = new MovieReviewFeedback();
        r.setMovieId(movie);
        r.setIsLiked(Boolean.TRUE);
        r.setMovieReviewId(movieReview);
        r.setUserId(portalUser);
        r = movieReviewFeedbackRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieReviewFeedbackRepository.findById(r.getId()).isPresent());
    }
}
