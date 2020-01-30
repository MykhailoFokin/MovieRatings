package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;
import solvve.course.repository.MovieRepository;
import solvve.course.repository.ReleaseDetailRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from release_detail; delete from movie; delete from country;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReleaseDetailServiceTest {

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Autowired
    private ReleaseDetailService releaseDetailService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryRepository countryRepository;

    public Movie createMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie");
        movie = movieRepository.save(movie);
        return movie;
    }

    public Country createCountry() {
        Country country = new Country();
        country.setName("Germany");
        country = countryRepository.save(country);
        return  country;
    }

    private ReleaseDetail createCountries(Movie movie, Country country) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setId(UUID.randomUUID());
        releaseDetail.setMovieId(movie);
        releaseDetail.setCountryId(country);
        releaseDetail.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        return releaseDetailRepository.save(releaseDetail);
    }

    @Transactional
    @Test
    public void testGetCountries() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        ReleaseDetailReadDTO readDTO = releaseDetailService.getReleaseDetails(releaseDetail.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(releaseDetail);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        releaseDetailService.getReleaseDetails(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateCountries() {
        Movie movie = createMovie();
        Country country = createCountry();

        ReleaseDetailCreateDTO create = new ReleaseDetailCreateDTO();
        create.setMovieId(movie);
        create.setCountryId(country);
        create.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.createReleaseDetails(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        ReleaseDetail releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(releaseDetail);
    }

    @Transactional
    @Test
    public void testPatchCountries() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        ReleaseDetailPatchDTO patch = new ReleaseDetailPatchDTO();
        patch.setMovieId(movie);
        patch.setCountryId(country);
        patch.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.patchReleaseDetails(releaseDetail.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(releaseDetail).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchCountriesEmptyPatch() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        ReleaseDetailPatchDTO patch = new ReleaseDetailPatchDTO();
        ReleaseDetailReadDTO read = releaseDetailService.patchReleaseDetails(releaseDetail.getId(), patch);

        Assert.assertNotNull(read.getReleaseDate());

        ReleaseDetail releaseDetailAfterUpdate = releaseDetailRepository.findById(read.getId()).get();

        Assert.assertNotNull(releaseDetailAfterUpdate.getReleaseDate());

        Assertions.assertThat(releaseDetail).isEqualToComparingFieldByField(releaseDetailAfterUpdate);
    }

    @Test
    public void testDeleteCountries() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        releaseDetailService.deleteReleaseDetails(releaseDetail.getId());
        Assert.assertFalse(releaseDetailRepository.existsById(releaseDetail.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCountriesNotFound() {
        releaseDetailService.deleteReleaseDetails(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutCountries() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        ReleaseDetailPutDTO put = new ReleaseDetailPutDTO();
        put.setMovieId(movie);
        put.setCountryId(country);
        put.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.putReleaseDetails(releaseDetail.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(releaseDetail).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPutCountriesEmptyPut() {
        Movie movie = createMovie();
        Country country = createCountry();
        ReleaseDetail releaseDetail = createCountries(movie, country);

        ReleaseDetailPutDTO put = new ReleaseDetailPutDTO();
        ReleaseDetailReadDTO read = releaseDetailService.putReleaseDetails(releaseDetail.getId(), put);

        Assert.assertNull(read.getReleaseDate());

        ReleaseDetail releaseDetailAfterUpdate = releaseDetailRepository.findById(read.getId()).get();

        Assert.assertNull(releaseDetailAfterUpdate.getReleaseDate());

        Assertions.assertThat(releaseDetail).isEqualToComparingFieldByField(releaseDetailAfterUpdate);
    }
}
