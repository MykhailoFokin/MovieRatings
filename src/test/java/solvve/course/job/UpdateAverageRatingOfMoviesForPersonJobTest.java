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
import solvve.course.domain.*;
import solvve.course.repository.MovieRepository;
import solvve.course.repository.PersonRepository;
import solvve.course.service.MovieService;
import solvve.course.service.PersonService;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {
        "delete from role",
        "delete from person",
        "delete from movie_vote",
        "delete from movie",
        "delete from portal_user"
        }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UpdateAverageRatingOfMoviesForPersonJobTest {

    @Autowired
    private UpdateAverageRatingOfMoviesForPersonJob updateAverageRatingOfMoviesForPersonJob;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private PersonRepository personRepository;

    @SpyBean
    private PersonService personService;

    @Test
    public void testUpdateAverageRatingOfMovies() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        testObjectsFactory.createRole(person, movie);
        testObjectsFactory.createMovieVote(portalUser1, movie, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R7);

        updateAverageRatingOfMoviesForPersonJob.updateAverageRatingOfMoviesForPersons();

        person = personRepository.findById(person.getId()).get();
        Assert.assertEquals(5.0, person.getAverageMovieRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testMoviesUpdatedIndependently() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        testObjectsFactory.createRole(person, movie);
        testObjectsFactory.createMovieVote(portalUser1, movie, UserVoteRatingType.R6);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R8);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocationOnMock -> {
            if (failedId[0] == null) {
                failedId[0] = invocationOnMock.getArgument(0);
                throw new RuntimeException();
            }
            return invocationOnMock.callRealMethod();
        }).when(personService).updateAverageRatingOfMovieForPerson(Mockito.any());

        updateAverageRatingOfMoviesForPersonJob.updateAverageRatingOfMoviesForPersons();

        for (Person m : personRepository.findAll()) {
            if (m.getId().equals(failedId[0])) {
                Assert.assertEquals(0.0, m.getAverageMovieRating(), Double.MIN_NORMAL);
            } else {
                Assert.assertNotNull(m.getAverageMovieRating());
            }
        }
    }
}
