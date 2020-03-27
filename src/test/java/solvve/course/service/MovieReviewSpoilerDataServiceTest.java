package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.domain.PortalUser;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieSpoilerDataRepository;

import java.util.List;
import java.util.UUID;

public class MovieReviewSpoilerDataServiceTest extends BaseTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieReviewSpoilerDataService movieReviewSpoilerDataService;

    @Test
    public void testGetMovieSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        List<MovieSpoilerDataReadDTO> readDTO =
                movieReviewSpoilerDataService.getMovieReviewSpoilerDatas(movieReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(movieSpoilerData.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieSpoilerDataWrongId() {
        movieReviewSpoilerDataService.getMovieReviewSpoilerDatas(UUID.randomUUID());
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

        MovieSpoilerDataReadDTO read = movieReviewSpoilerDataService.createMovieReviewSpoilerData(movieReview.getId()
                , create);
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

        MovieSpoilerDataPatchDTO patch = new MovieSpoilerDataPatchDTO();
        patch.setMovieReviewId(movieReview.getId());
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        MovieSpoilerDataReadDTO read =
                movieReviewSpoilerDataService.patchMovieReviewSpoilerData(movieReview.getId(),
                        movieSpoilerData.getId(), patch);

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
        MovieSpoilerDataReadDTO read =
                movieReviewSpoilerDataService.patchMovieReviewSpoilerData(movieReview.getId(),
                        movieSpoilerData.getId(), patch);

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

        movieReviewSpoilerDataService.deleteMovieReviewSpoilerData(movieReview.getId(), movieSpoilerData.getId());
        Assert.assertFalse(movieSpoilerDataRepository.existsById(movieSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieSpoilerDataNotFound() {
        movieReviewSpoilerDataService.deleteMovieReviewSpoilerData(UUID.randomUUID(), UUID.randomUUID());
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
        MovieSpoilerDataReadDTO read =
                movieReviewSpoilerDataService.updateMovieReviewSpoilerData(movieReview.getId(),
                        movieSpoilerData.getId(), put);

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
        MovieSpoilerDataReadDTO read =
                movieReviewSpoilerDataService.updateMovieReviewSpoilerData(movieReview.getId(),
                        movieSpoilerData.getId(), put);

        Assert.assertNotNull(read.getMovieReviewId());
        Assert.assertNull(read.getStartIndex());
        Assert.assertNull(read.getEndIndex());

        MovieSpoilerData movieSpoilerDataAfterUpdate = movieSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieSpoilerDataAfterUpdate.getMovieReview().getId());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(movieSpoilerDataAfterUpdate.getEndIndex());
    }
}
