package solvve.course.job;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserVoteRatingType;
import solvve.course.repository.MovieRepository;
import solvve.course.service.MovieService;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_vote",
        "delete from movie",
        "delete from portal_user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UpdateAverageRatingOfMoviesJobTest {

    @Autowired
    private UpdateAverageRatingOfMoviesJob updateAverageRatingOfMoviesJob;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private MovieRepository movieRepository;

    @SpyBean
    private MovieService movieService;

    @Test
    public void testUpdateAverageRatingOfMovies() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        testObjectsFactory.createMovieVote(portalUser1, movie, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R7);

        updateAverageRatingOfMoviesJob.updateAverageRatingOfMovies();

        movie = movieRepository.findById(movie.getId()).get();
        Assert.assertEquals(5.0, movie.getAverageRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testMoviesUpdatedIndependently() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        testObjectsFactory.createMovieVote(portalUser, movie, UserVoteRatingType.R6);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R7);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocationOnMock -> {
            if (failedId[0] == null) {
                failedId[0] = invocationOnMock.getArgument(0);
                throw new RuntimeException();
            }
            return invocationOnMock.callRealMethod();
        }).when(movieService).updateAverageRatingOfMovie(Mockito.any());

        updateAverageRatingOfMoviesJob.updateAverageRatingOfMovies();

        for (Movie m : movieRepository.findAll()) {
            if (m.getId().equals(failedId[0])) {
                Assert.assertNull(m.getAverageRating());
            } else {
                Assert.assertNotNull(m.getAverageRating());
            }
        }
    }
}
