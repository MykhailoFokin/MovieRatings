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
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.ReleaseDetailRepository;
import solvve.course.utils.TestObjectsFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from release_detail",
        " delete from movie",
        " delete from country"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReleaseDetailServiceTest {

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Autowired
    private ReleaseDetailService releaseDetailService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetReleaseDetail() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        ReleaseDetailReadDTO readDTO = releaseDetailService.getReleaseDetails(releaseDetail.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(releaseDetail,
                "movieId", "countryId");
        Assertions.assertThat(readDTO.getMovieId())
                .isEqualToComparingFieldByField(releaseDetail.getMovieId().getId());
        Assertions.assertThat(readDTO.getCountryId())
                .isEqualToComparingFieldByField(releaseDetail.getCountryId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetReleaseDetailWrongId() {
        releaseDetailService.getReleaseDetails(UUID.randomUUID());
    }

    @Test
    public void testCreateReleaseDetail() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();

        ReleaseDetailCreateDTO create = new ReleaseDetailCreateDTO();
        create.setMovieId(movie.getId());
        create.setCountryId(country.getId());
        create.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.createReleaseDetails(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        ReleaseDetail releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(releaseDetail,
                "movieId", "countryId");
        Assertions.assertThat(read.getMovieId())
                .isEqualToComparingFieldByField(releaseDetail.getMovieId().getId());
        Assertions.assertThat(read.getCountryId())
                .isEqualToComparingFieldByField(releaseDetail.getCountryId().getId());
    }

    @Test
    public void testPatchReleaseDetail() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        ReleaseDetailPatchDTO patch = new ReleaseDetailPatchDTO();
        patch.setMovieId(movie.getId());
        patch.setCountryId(country.getId());
        patch.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.patchReleaseDetails(releaseDetail.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(releaseDetail).isEqualToIgnoringGivenFields(read,
                "movieId", "countryId");
        Assertions.assertThat(releaseDetail.getMovieId().getId())
                .isEqualToComparingFieldByField(read.getMovieId());
        Assertions.assertThat(releaseDetail.getCountryId().getId())
                .isEqualToComparingFieldByField(read.getCountryId());
    }

    @Test
    public void testPatchReleaseDetailEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        ReleaseDetailPatchDTO patch = new ReleaseDetailPatchDTO();
        ReleaseDetailReadDTO read = releaseDetailService.patchReleaseDetails(releaseDetail.getId(), patch);

        Assert.assertNotNull(read.getReleaseDate());

        ReleaseDetail releaseDetailAfterUpdate = releaseDetailRepository.findById(read.getId()).get();

        Assert.assertNotNull(releaseDetailAfterUpdate.getReleaseDate());

        Assertions.assertThat(releaseDetail).isEqualToIgnoringGivenFields(releaseDetailAfterUpdate,
                "movieId", "countryId");
        Assertions.assertThat(releaseDetail.getMovieId().getId())
                .isEqualTo(releaseDetailAfterUpdate.getMovieId().getId());
        Assertions.assertThat(releaseDetail.getCountryId().getId())
                .isEqualTo(releaseDetailAfterUpdate.getCountryId().getId());
    }

    @Test
    public void testDeleteReleaseDetail() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        releaseDetailService.deleteReleaseDetails(releaseDetail.getId());
        Assert.assertFalse(releaseDetailRepository.existsById(releaseDetail.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteReleaseDetailNotFound() {
        releaseDetailService.deleteReleaseDetails(UUID.randomUUID());
    }

    @Test
    public void testPutReleaseDetail() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        ReleaseDetailPutDTO put = new ReleaseDetailPutDTO();
        put.setMovieId(movie.getId());
        put.setCountryId(country.getId());
        put.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        ReleaseDetailReadDTO read = releaseDetailService.updateReleaseDetails(releaseDetail.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        releaseDetail = releaseDetailRepository.findById(read.getId()).get();
        Assertions.assertThat(releaseDetail).isEqualToIgnoringGivenFields(read,
                "movieId", "countryId");
        Assertions.assertThat(releaseDetail.getMovieId().getId())
                .isEqualToComparingFieldByField(read.getMovieId());
        Assertions.assertThat(releaseDetail.getCountryId().getId())
                .isEqualToComparingFieldByField(read.getCountryId());
    }

    @Test
    public void testPutReleaseDetailEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        Country country = testObjectsFactory.createCountry();
        ReleaseDetail releaseDetail = testObjectsFactory.createReleaseDetail(movie, country);

        ReleaseDetailPutDTO put = new ReleaseDetailPutDTO();
        ReleaseDetailReadDTO read = releaseDetailService.updateReleaseDetails(releaseDetail.getId(), put);

        Assert.assertNull(read.getReleaseDate());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getCountryId());

        ReleaseDetail releaseDetailAfterUpdate = releaseDetailRepository.findById(read.getId()).get();

        Assert.assertNull(releaseDetailAfterUpdate.getReleaseDate());
        Assert.assertNotNull(releaseDetailAfterUpdate.getMovieId());
        Assert.assertNotNull(releaseDetailAfterUpdate.getCountryId());

        Assertions.assertThat(releaseDetail).isEqualToComparingOnlyGivenFields(releaseDetailAfterUpdate, "id");
        Assertions.assertThat(releaseDetail.getCountryId().getId())
                .isEqualTo(releaseDetailAfterUpdate.getCountryId().getId());
        Assertions.assertThat(releaseDetail.getMovieId().getId())
                .isEqualTo(releaseDetailAfterUpdate.getMovieId().getId());
    }
}
