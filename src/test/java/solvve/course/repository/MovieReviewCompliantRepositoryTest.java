package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_review_compliant; delete from movie; delete from movie_review; delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieReviewCompliantRepositoryTest {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testSave() {
        Movie movie = new Movie();
        movie = movieRepository.save(movie);

        UserTypes userTypes = new UserTypes();
        userTypes = userTypesRepository.save(userTypes);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userTypes);
        portalUser = portalUserRepository.save(portalUser);

        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUser);
        movieReview.setModeratorId(portalUser);
        movieReview = movieReviewRepository.save(movieReview);

        MovieReviewCompliant r = new MovieReviewCompliant();
        r.setMovieId(movie);
        r.setMovieReviewId(movieReview);
        r.setUserId(portalUser);
        r.setModeratorId(portalUser);
        r = movieReviewCompliantRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieReviewCompliantRepository.findById(r.getId()).isPresent());
    }
}
