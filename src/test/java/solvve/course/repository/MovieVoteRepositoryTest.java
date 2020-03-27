package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.*;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;
import static solvve.course.domain.UserVoteRatingType.R2;

public class MovieVoteRepositoryTest extends BaseTest {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Test
    public void testSave() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        MovieVote r = new MovieVote();
        r.setMovie(movie);
        r.setPortalUser(portalUser);
        r.setRating(UserVoteRatingType.R9);
        r = movieVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieVoteRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
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
    public void testUpdatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieVote entity = testObjectsFactory.createMovieVote(portalUser, movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = movieVoteRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        MovieVote entity = testObjectsFactory.createMovieVote(portalUser, movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setRating(R2);
        movieVoteRepository.save(entity);
        entity = movieVoteRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }

    @Test
    public void testCalcAverageMarkOfMovie() {
        Movie movie = testObjectsFactory.createMovie();
        UserType userType1 = testObjectsFactory.createUserType();
        PortalUser portalUser1 = testObjectsFactory.createPortalUser(userType1);
        testObjectsFactory.createMovieVote(portalUser1, movie, UserVoteRatingType.R7);
        UserType userType2 = testObjectsFactory.createUserType();
        PortalUser portalUser2 = testObjectsFactory.createPortalUser(userType2);
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R9);

        Assert.assertEquals(7.0, movieVoteRepository.calcAverageMarkOfMovie(movie.getId()), Double.MIN_NORMAL);
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveMovieVoteValidation() {
        MovieVote entity = new MovieVote();
        movieVoteRepository.save(entity);
    }
}
