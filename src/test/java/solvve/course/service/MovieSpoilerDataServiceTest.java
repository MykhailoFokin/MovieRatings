package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

public class MovieSpoilerDataServiceTest extends BaseTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @Test
    public void testGetMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataReadDTO readDTO = movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(readDTO.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReview().getId());
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

        MovieSpoilerDataCreateDTO create = testObjectsFactory.createMovieSpoilerDataCreateDTO();
        create.setMovieReviewId(movieReview.getId());

        MovieSpoilerDataReadDTO read = movieSpoilerDataService.createMovieSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieSpoilerData movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieSpoilerData,
                "movieReviewId");
        Assertions.assertThat(read.getMovieReviewId()).isEqualTo(movieSpoilerData.getMovieReview().getId());
    }

    @Test
    public void testPatchMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        MovieSpoilerDataPatchDTO patch = testObjectsFactory.createMovieSpoilerDataPatchDTO();
        patch.setMovieReviewId(movieReview.getId());
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.patchMovieSpoilerData(movieSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReview");
        Assertions.assertThat(movieSpoilerData.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
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

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReview());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(movieSpoilerDataAfterUpdate,
                "movieReview");
        Assertions.assertThat(movieSpoilerData.getMovieReview().getId())
                .isEqualTo(movieSpoilerDataAfterUpdate.getMovieReview().getId());
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

        MovieSpoilerDataPutDTO put = testObjectsFactory.createMovieSpoilerDataPutDTO();
        put.setMovieReviewId(movieReview.getId());
        MovieSpoilerDataReadDTO read = movieSpoilerDataService.updateMovieSpoilerData(movieSpoilerData.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieSpoilerData = movieSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(movieSpoilerData).isEqualToIgnoringGivenFields(read,
                "movieReview");
        Assertions.assertThat(movieSpoilerData.getMovieReview().getId()).isEqualTo(read.getMovieReviewId());
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

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReview().getId());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getEndIndex());
    }
}
