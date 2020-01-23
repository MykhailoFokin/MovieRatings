package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieVote;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserTypes;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_vote; delete from movie; delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieVoteRepositoryTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Test
    public void testSave() {
        Movie movie = new Movie();
        movie = movieRepository.save(movie);

        UserTypes userTypes = new UserTypes();
        userTypes = userTypesRepository.save(userTypes);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userTypes);
        portalUser = portalUserRepository.save(portalUser);

        MovieVote r = new MovieVote();
        r.setMovieId(movie);
        r.setUserId(portalUser);
        r = movieVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieVoteRepository.findById(r.getId()).isPresent());
    }
}
