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
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
import solvve.course.dto.MoviePutDTO;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;
import solvve.course.repository.MovieRepository;
import org.assertj.core.api.Assertions;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from movie_prod_countries","delete from movie","delete from country"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CountryRepository countryRepository;

    private Movie createMovie(Set<Country> countrySet) {
        Movie movie = new Movie();
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setGenres("Comedy");
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCompanies("Paramount");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setFilmingLocations("USA");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setLanguages("English");
        movie.setSoundMix("DolbySurround");
        movie.setIsPublished(true);
        movie.setMovieProdCountries(countrySet);
        return movieRepository.save(movie);
    }

    private Set<Country> createCountrySet() {
        Country c = new Country();
        c.setName("C1");
        c = countryRepository.save(c);
        Set<Country> sc = new HashSet<>();
        sc.add(c);
        return sc;
    }

    @Transactional
    @Test
    public void testGetMovie() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        MovieReadDTO readDTO = movieService.getMovie(movie.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMovie() {MovieCreateDTO create = new MovieCreateDTO();
        create.setTitle("Movie Test");
        create.setYear((short) 2019);
        create.setGenres("Comedy");
        create.setAspectRatio("1:10");
        create.setCamera("Panasonic");
        create.setColour("Black");
        create.setCompanies("Paramount");
        create.setCritique("123");
        create.setDescription("Description");
        create.setFilmingLocations("USA");
        create.setLaboratory("CaliforniaDreaming");
        create.setLanguages("English");
        create.setSoundMix("DolbySurround");
        create.setIsPublished(true);
        MovieReadDTO read = movieService.createMovie(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Movie movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movie);
    }

    @Transactional
    @Test
    public void testPatchMovie() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        MoviePatchDTO patch = new MoviePatchDTO();
        patch.setTitle("Movie Test");
        patch.setYear((short) 2019);
        patch.setGenres("Comedy");
        patch.setAspectRatio("1:10");
        patch.setCamera("Panasonic");
        patch.setColour("Black");
        patch.setCompanies("Paramount");
        patch.setCritique("123");
        patch.setDescription("Description");
        patch.setFilmingLocations("USA");
        patch.setLaboratory("CaliforniaDreaming");
        patch.setLanguages("English");
        patch.setSoundMix("DolbySurround");
        patch.setIsPublished(true);
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(movie).isEqualToIgnoringGivenFields(read,"movieProdCountries","crews","movieReview","movieReviewCompliants"
                ,"movieReviewFeedbacks","movieVotes","releaseDetails");
    }

    @Transactional
    @Test
    public void testPatchMovieEmptyPatch() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        MoviePatchDTO patch = new MoviePatchDTO();
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assert.assertNotNull(read.getTitle());
        Assert.assertNotNull(read.getYear());
        Assert.assertNotNull(read.getGenres());
        Assert.assertNotNull(read.getAspectRatio());
        Assert.assertNotNull(read.getCamera());
        Assert.assertNotNull(read.getColour());
        Assert.assertNotNull(read.getCompanies());
        Assert.assertNotNull(read.getCritique());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getFilmingLocations());
        Assert.assertNotNull(read.getLaboratory());
        Assert.assertNotNull(read.getLanguages());
        Assert.assertNotNull(read.getSoundMix());
        Assert.assertNotNull(read.getIsPublished());

        Movie movieAfterUpdate = movieRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieAfterUpdate.getTitle());
        Assert.assertNotNull(movieAfterUpdate.getYear());
        Assert.assertNotNull(movieAfterUpdate.getGenres());
        Assert.assertNotNull(movieAfterUpdate.getAspectRatio());
        Assert.assertNotNull(movieAfterUpdate.getCamera());
        Assert.assertNotNull(movieAfterUpdate.getColour());
        Assert.assertNotNull(movieAfterUpdate.getCompanies());
        Assert.assertNotNull(movieAfterUpdate.getCritique());
        Assert.assertNotNull(movieAfterUpdate.getDescription());
        Assert.assertNotNull(movieAfterUpdate.getFilmingLocations());
        Assert.assertNotNull(movieAfterUpdate.getLaboratory());
        Assert.assertNotNull(movieAfterUpdate.getLanguages());
        Assert.assertNotNull(movieAfterUpdate.getSoundMix());
        Assert.assertNotNull(movieAfterUpdate.getIsPublished());

        Assertions.assertThat(movie).isEqualToComparingFieldByField(movieAfterUpdate);
    }

    @Transactional
    @Test
    public void testDeleteMovie() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        movieService.deleteMovie(movie.getId());
        Assert.assertFalse(movieRepository.existsById(movie.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieNotFound() {
        movieService.deleteMovie(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutMovie() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        MoviePutDTO put = new MoviePutDTO();
        put.setTitle("Movie Test");
        put.setYear((short) 2019);
        put.setGenres("Comedy");
        put.setAspectRatio("1:10");
        put.setCamera("Panasonic");
        put.setColour("Black");
        put.setCompanies("Paramount");
        put.setCritique("123");
        put.setDescription("Description");
        put.setFilmingLocations("USA");
        put.setLaboratory("CaliforniaDreaming");
        put.setLanguages("English");
        put.setSoundMix("DolbySurround");
        put.setIsPublished(true);
        MovieReadDTO read = movieService.putMovie(movie.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(movie).isEqualToIgnoringGivenFields(read,"movieProdCountries","crews","movieReview","movieReviewCompliants"
                        ,"movieReviewFeedbacks","movieVotes","releaseDetails");
    }

    @Transactional
    @Test
    public void testPutMovieEmptyPut() {
        Set<Country> countrySet = createCountrySet();
        Movie movie = createMovie(countrySet);

        MoviePutDTO put = new MoviePutDTO();
        MovieReadDTO read = movieService.putMovie(movie.getId(), put);

        Assert.assertNull(read.getTitle());
        Assert.assertNull(read.getYear());
        Assert.assertNull(read.getGenres());
        Assert.assertNull(read.getAspectRatio());
        Assert.assertNull(read.getCamera());
        Assert.assertNull(read.getColour());
        Assert.assertNull(read.getCompanies());
        Assert.assertNull(read.getCritique());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getFilmingLocations());
        Assert.assertNull(read.getLaboratory());
        Assert.assertNull(read.getLanguages());
        Assert.assertNull(read.getSoundMix());
        Assert.assertNull(read.getIsPublished());

        Movie movieAfterUpdate = movieRepository.findById(read.getId()).get();

        Assert.assertNull(movieAfterUpdate.getTitle());
        Assert.assertNull(movieAfterUpdate.getYear());
        Assert.assertNull(movieAfterUpdate.getGenres());
        Assert.assertNull(movieAfterUpdate.getAspectRatio());
        Assert.assertNull(movieAfterUpdate.getCamera());
        Assert.assertNull(movieAfterUpdate.getColour());
        Assert.assertNull(movieAfterUpdate.getCompanies());
        Assert.assertNull(movieAfterUpdate.getCritique());
        Assert.assertNull(movieAfterUpdate.getDescription());
        Assert.assertNull(movieAfterUpdate.getFilmingLocations());
        Assert.assertNull(movieAfterUpdate.getLaboratory());
        Assert.assertNull(movieAfterUpdate.getLanguages());
        Assert.assertNull(movieAfterUpdate.getSoundMix());
        Assert.assertNull(movieAfterUpdate.getIsPublished());

        Assertions.assertThat(movie).isEqualToComparingFieldByField(movieAfterUpdate);
    }
}
