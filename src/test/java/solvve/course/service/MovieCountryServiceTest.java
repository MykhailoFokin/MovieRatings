package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.CountryRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;

public class MovieCountryServiceTest extends BaseTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieCountryService movieCountryService;

    @Test
    public void testAddCountryToMovie() {
        Movie movie = testObjectsFactory.createMovie();
        UUID countryId = testObjectsFactory.createCountry("CountryName").getId();

        List<MovieCountryReadDTO> res = movieCountryService.addCountryToMovie(movie.getId(), countryId);

        MovieCountryReadDTO expectedRead = new MovieCountryReadDTO();
        expectedRead.setId(countryId);
        expectedRead.setName("CountryName");
        Assertions.assertThat(res).containsExactlyInAnyOrder(expectedRead);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterSave = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterSave.getMovieProdCountries()).extracting(Country::getId)
                    .containsExactlyInAnyOrder(countryId);
        });
    }

    @Test
    public void testDuplicatedCountry() {
        Movie movie = testObjectsFactory.createMovie();
        UUID countryId = testObjectsFactory.createCountry("CountryName").getId();

        movieCountryService.addCountryToMovie(movie.getId(), countryId);
        Assertions.assertThatThrownBy(() -> {
            movieCountryService.addCountryToMovie(movie.getId(), countryId);
        }).isInstanceOf(LinkDuplicatedException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongMovieId() {
        UUID wrongMovieId = UUID.randomUUID();
        UUID countryId = testObjectsFactory.createCountry("CountryName").getId();
        movieCountryService.addCountryToMovie(wrongMovieId, countryId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongCountryId() {
        Movie movie = testObjectsFactory.createMovie();
        UUID countryId = UUID.randomUUID();
        movieCountryService.addCountryToMovie(movie.getId(), countryId);
    }

    @Test
    public void testRemoveCountryFromMovie() {
        UUID countryId1 = testObjectsFactory.createCountry("CountryName").getId();
        UUID countryId2 = testObjectsFactory.createCountry("CountryName").getId();
        Movie movie = testObjectsFactory.createMovieWithCountries(List.of(countryId1, countryId2));

        movieCountryService.removeCountryFromMovie(movie.getId(), countryId1);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterRemove.getMovieProdCountries()).extracting(Country::getId)
                    .containsExactlyInAnyOrder(countryId2);
        });
    }

    @Test
    public void testAddAndThenRemoveCountryFromMovie() {
        Movie movie = testObjectsFactory.createMovie();
        UUID countryId = testObjectsFactory.createCountry("CountryName").getId();
        movieCountryService.addCountryToMovie(movie.getId(), countryId);

        List<MovieCountryReadDTO> remainingCountries = movieCountryService.removeCountryFromMovie(movie.getId(),
                countryId);
        Assert.assertTrue(remainingCountries.isEmpty());

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assert.assertTrue(movieAfterRemove.getMovieProdCountries().isEmpty());
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotAddedCountry() {
        Movie movie = testObjectsFactory.createMovie();
        UUID countryId = testObjectsFactory.createCountry("CountryName").getId();

        movieCountryService.removeCountryFromMovie(movie.getId(), countryId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotExistedCountry() {
        Movie movie = testObjectsFactory.createMovie();
        movieCountryService.removeCountryFromMovie(movie.getId(), UUID.randomUUID());
    }

    @Test
    public void testGetMovieCountries() {
        UUID countryId1 = testObjectsFactory.createCountry("CountryName").getId();
        UUID countryId2 = testObjectsFactory.createCountry("CountryName").getId();
        Movie movie = testObjectsFactory.createMovieWithCountries(List.of(countryId1, countryId2));

        Assertions.assertThat(movieCountryService.getMovieCountries(movie.getId()))
                .extracting(MovieCountryReadDTO::getId)
                .containsExactlyInAnyOrder(countryId1, countryId2);
    }
}
