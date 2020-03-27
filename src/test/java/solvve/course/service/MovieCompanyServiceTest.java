package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.CompanyDetails;
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.MovieCompanyCreateDTO;
import solvve.course.dto.MovieCompanyPatchDTO;
import solvve.course.dto.MovieCompanyPutDTO;
import solvve.course.dto.MovieCompanyReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieCompanyRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

public class MovieCompanyServiceTest extends BaseTest {

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private MovieCompanyService movieCompanyService;

    @Test
    public void testGetMovieCompany() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyReadDTO readDTO = movieCompanyService.getMovieCompany(movieCompany.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(movieCompany, "companyDetailsId");
        Assertions.assertThat(readDTO.getCompanyDetailsId()).isEqualTo(movieCompany.getCompanyDetails().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieCompanyWrongId() {
        movieCompanyService.getMovieCompany(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieCompany() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompanyCreateDTO create = testObjectsFactory.createMovieCompanyCreateDTO(companyDetails.getId(),
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyReadDTO read = movieCompanyService.createMovieCompany(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        MovieCompany movieCompany = movieCompanyRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieCompany, "companyDetailsId");
        Assertions.assertThat(read.getCompanyDetailsId()).isEqualTo(movieCompany.getCompanyDetails().getId());
    }

    @Test
    public void testPatchMovieCompany() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyPatchDTO patch = testObjectsFactory.createMovieCompanyPatchDTO(companyDetails.getId(),
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyReadDTO read = movieCompanyService.patchMovieCompany(movieCompany.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movieCompany = movieCompanyRepository.findById(read.getId()).get();
        Assertions.assertThat(movieCompany).isEqualToIgnoringGivenFields(read,
                "movies","companyDetails");
        Assertions.assertThat(movieCompany.getCompanyDetails().getId()).isEqualTo(read.getCompanyDetailsId());
    }

    @Test
    public void testPatchMovieCompanyEmptyPatch() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyPatchDTO patch = new MovieCompanyPatchDTO();
        MovieCompanyReadDTO read = movieCompanyService.patchMovieCompany(movieCompany.getId(), patch);

        Assert.assertNotNull(read.getMovieProductionType());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getCompanyDetailsId());

        MovieCompany movieCompanyAfterUpdate = movieCompanyRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieCompanyAfterUpdate.getMovieProductionType());
        Assert.assertNotNull(movieCompanyAfterUpdate.getDescription());
        Assert.assertNotNull(movieCompanyAfterUpdate.getCompanyDetails().getId());

        Assertions.assertThat(movieCompany).isEqualToIgnoringGivenFields(movieCompanyAfterUpdate,
                "movies","companyDetails");
        Assertions.assertThat(movieCompany.getCompanyDetails().getId())
                .isEqualTo(movieCompanyAfterUpdate.getCompanyDetails().getId());
    }

    @Test
    public void testDeleteMovieCompany() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        movieCompanyService.deleteMovieCompany(movieCompany.getId());
        Assert.assertFalse(movieCompanyRepository.existsById(movieCompany.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieCompanyNotFound() {
        movieCompanyService.deleteMovieCompany(UUID.randomUUID());
    }

    @Test
    public void testPutMovieCompany() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyPutDTO put = testObjectsFactory.createMovieCompanyPutDTO(companyDetails.getId(),
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyReadDTO read = movieCompanyService.updateMovieCompany(movieCompany.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        movieCompany = movieCompanyRepository.findById(read.getId()).get();
        Assertions.assertThat(movieCompany).isEqualToIgnoringGivenFields(read,
                "movies","companyDetails");
        Assertions.assertThat(movieCompany.getCompanyDetails().getId()).isEqualTo(read.getCompanyDetailsId());
    }

    @Test
    public void testPutMovieCompanyEmptyPut() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        MovieCompany movieCompany = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyPutDTO put = new MovieCompanyPutDTO();
        MovieCompanyReadDTO read = movieCompanyService.updateMovieCompany(movieCompany.getId(), put);

        Assert.assertNotNull(read.getCompanyDetailsId());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getMovieProductionType());

        MovieCompany movieCompanyAfterUpdate = movieCompanyRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieCompanyAfterUpdate.getCompanyDetails());
        Assert.assertNull(movieCompanyAfterUpdate.getDescription());
        Assert.assertNull(movieCompanyAfterUpdate.getMovieProductionType());

        Assertions.assertThat(movieCompany).isEqualToIgnoringGivenFields(movieCompanyAfterUpdate,
                "movies","companyDetails","movieProductionType", "description", "updatedAt");
        Assertions.assertThat(movieCompany.getCompanyDetails().getId())
                .isEqualTo(movieCompanyAfterUpdate.getCompanyDetails().getId());
    }
}
