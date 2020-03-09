package solvve.course.service;

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
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
import solvve.course.dto.MoviePutDTO;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;
import org.assertj.core.api.Assertions;
import solvve.course.utils.TestObjectsFactory;

import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_prod_countries",
        "delete from movie_vote",
        "delete from movie",
        "delete from country",
        "delete from portal_user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetMovie() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        MovieReadDTO readDTO = movieService.getMovie(movie.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }

    @Test
    public void testCreateMovie() {
        MovieCreateDTO create = testObjectsFactory.createMovieCreateDTO();
        MovieReadDTO read = movieService.createMovie(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Movie movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movie);
    }

    @Test
    public void testPatchMovie() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        MoviePatchDTO patch = testObjectsFactory.createMoviePatchDTO();
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(movie).isEqualToIgnoringGivenFields(read,
                "movieProdCountries","crews","movieReview","movieReviewCompliants"
                ,"movieReviewFeedbacks","movieVotes","releaseDetails","genres","movieProdCompanies"
                ,"movieProdLanguages","role");
    }

    @Test
    public void testPatchMovieEmptyPatch() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        MoviePatchDTO patch = new MoviePatchDTO();
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assert.assertNotNull(read.getTitle());
        Assert.assertNotNull(read.getYear());
        Assert.assertNotNull(read.getAspectRatio());
        Assert.assertNotNull(read.getCamera());
        Assert.assertNotNull(read.getColour());
        Assert.assertNotNull(read.getCritique());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getLaboratory());
        Assert.assertNotNull(read.getSoundMix());
        Assert.assertNotNull(read.getIsPublished());

        Movie movieAfterUpdate = movieRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieAfterUpdate.getTitle());
        Assert.assertNotNull(movieAfterUpdate.getYear());
        Assert.assertNotNull(movieAfterUpdate.getAspectRatio());
        Assert.assertNotNull(movieAfterUpdate.getCamera());
        Assert.assertNotNull(movieAfterUpdate.getColour());
        Assert.assertNotNull(movieAfterUpdate.getCritique());
        Assert.assertNotNull(movieAfterUpdate.getDescription());
        Assert.assertNotNull(movieAfterUpdate.getLaboratory());
        Assert.assertNotNull(movieAfterUpdate.getSoundMix());
        Assert.assertNotNull(movieAfterUpdate.getIsPublished());

        Assertions.assertThat(movie).isEqualToIgnoringGivenFields(movieAfterUpdate,
                "movieProdCountries","crews","movieReview","movieReviewCompliants"
                ,"movieReviewFeedbacks","movieVotes","releaseDetails","genres","movieProdCompanies"
                ,"movieProdLanguages","role");
    }

    @Test
    public void testDeleteMovie() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        movieService.deleteMovie(movie.getId());
        Assert.assertFalse(movieRepository.existsById(movie.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieNotFound() {
        movieService.deleteMovie(UUID.randomUUID());
    }

    @Test
    public void testPutMovie() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        MoviePutDTO put = testObjectsFactory.createMoviePutDTO();
        MovieReadDTO read = movieService.updateMovie(movie.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(movie).isEqualToIgnoringGivenFields(read,
                "movieProdCountries","crews","movieReview","movieReviewCompliants"
                        ,"movieReviewFeedbacks","movieVotes","releaseDetails","genres","movieProdCompanies"
                        ,"movieProdLanguages","role");
    }

    @Test
    public void testPutMovieEmptyPut() {
        Set<Country> countries = testObjectsFactory.createCountrySet();
        Movie movie = testObjectsFactory.createMovie(countries);

        MoviePutDTO put = new MoviePutDTO();
        MovieReadDTO read = movieService.updateMovie(movie.getId(), put);

        Assert.assertNull(read.getTitle());
        Assert.assertNull(read.getYear());
        Assert.assertNull(read.getAspectRatio());
        Assert.assertNull(read.getCamera());
        Assert.assertNull(read.getColour());
        Assert.assertNull(read.getCritique());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getLaboratory());
        Assert.assertNull(read.getSoundMix());
        Assert.assertNull(read.getIsPublished());

        Movie movieAfterUpdate = movieRepository.findById(read.getId()).get();

        Assert.assertNull(movieAfterUpdate.getTitle());
        Assert.assertNull(movieAfterUpdate.getYear());
        Assert.assertNull(movieAfterUpdate.getAspectRatio());
        Assert.assertNull(movieAfterUpdate.getCamera());
        Assert.assertNull(movieAfterUpdate.getColour());
        Assert.assertNull(movieAfterUpdate.getCritique());
        Assert.assertNull(movieAfterUpdate.getDescription());
        Assert.assertNull(movieAfterUpdate.getLaboratory());
        Assert.assertNull(movieAfterUpdate.getSoundMix());
        Assert.assertNull(movieAfterUpdate.getIsPublished());
    }

    @Test
    public void testUpdateAverageRatingOfMovie() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        testObjectsFactory.createMovieVote(portalUser1, movie, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createMovieVote(portalUser2, movie, UserVoteRatingType.R7);

        movieService.updateAverageRatingOfMovie(movie.getId());
        movie = movieRepository.findById(movie.getId()).get();
        Assert.assertEquals(5.0, movie.getAverageRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testUpdateAverageRatingOfMovieEmptyMovie() {
        Assertions.assertThatThrownBy(()-> movieService.updateAverageRatingOfMovie(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
