package solvve.course.repository;

import org.junit.Assert;
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
import solvve.course.domain.UserType;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;
import static solvve.course.domain.UserVoteRatingType.R2;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_vote",
        "delete from movie",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieVoteRepositoryTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        MovieVote r = new MovieVote();
        r.setMovieId(movie);
        r.setUserId(portalUser);
        r = movieVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieVoteRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieVote entity = testObjectsFactory.createMovieVote(portalUser, movie);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieVoteRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieVote entity = testObjectsFactory.createMovieVote(portalUser, movie);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = movieVoteRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieVote entity = testObjectsFactory.createMovieVote(portalUser, movie);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setRating(R2);
        movieVoteRepository.save(entity);
        entity = movieVoteRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
