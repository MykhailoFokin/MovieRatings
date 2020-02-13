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
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.domain.PortalUser;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_spoiler_data",
        "delete from movie_review",
        "delete from portal_user",
        "delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieSpoilerDataRepositoryTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieSpoilerData r = new MovieSpoilerData();
        r.setMovieReviewId(movieReview);
        r = movieSpoilerDataRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieSpoilerDataRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData entity = testObjectsFactory.createMovieSpoilerData(movieReview);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieSpoilerDataRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData entity = testObjectsFactory.createMovieSpoilerData(movieReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = movieSpoilerDataRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Movie movie = testObjectsFactory.createMovie();
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData entity = testObjectsFactory.createMovieSpoilerData(movieReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setEndIndex(3333);
        movieSpoilerDataRepository.save(entity);
        entity = movieSpoilerDataRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
