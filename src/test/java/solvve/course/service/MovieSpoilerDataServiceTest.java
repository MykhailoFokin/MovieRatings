package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_spoiler_data",
        " delete from movie_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieSpoilerDataServiceTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataReadDTO readDTO = movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReviewId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieSpoilerDataWrongId() {
        movieSpoilerDataService.getMovieSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);

        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        create.setMovieReviewId(movieReview.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        MovieSpoilerDataReadDTO read = movieSpoilerDataService.createMovieSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieSpoilerData movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReviewId().getId());
    }

    @Test
    public void testPatchMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        patch.setMovieReviewId(movieReview.getId());
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReviewId");
        Assertions.assertThat(movieSpoilerData.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
    }

    @Test
    public void testPatchMovieSpoilerDataEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());

        MovieSpoilerData movieSpoilerDataAfterUpdate = movieSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReviewId());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(movieSpoilerDataAfterUpdate,
                "movieReviewId");
        Assertions.assertThat(movieSpoilerData.getMovieReviewId().getId())
                .isEqualTo(movieSpoilerDataAfterUpdate.getMovieReviewId().getId());
    }

    @Test
    public void testDeleteMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        movieSpoilerDataService.deleteMovieSpoilerData(movieSpoilerData.getId());
        Assert.assertFalse(movieSpoilerDataRepository.existsById(movieSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieSpoilerDataNotFound() {
        movieSpoilerDataService.deleteMovieSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testPutMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();
        put.setMovieReviewId(movieReview.getId());
        put.setStartIndex(100);
        put.setEndIndex(150);
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.updateMovieSpoilerData(movieSpoilerData.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReviewId");
        Assertions.assertThat(movieSpoilerData.getMovieReviewId().getId()).isEqualTo(read.getMovieReviewId());
    }

    @Test
    public void testPutMovieSpoilerDataEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.updateMovieSpoilerData(movieSpoilerData.getId(), put);

        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNull(read.getStartIndex());
        Assert.assertNull(read.getEndIndex());

        MovieSpoilerData movieSpoilerDataAfterUpdate = movieSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReviewId().getId());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getEndIndex());
    }
}
