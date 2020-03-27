package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import solvve.course.BaseTest;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CompanyDetailsRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class CompanyDetailsServiceTest extends BaseTest {

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private CompanyDetailsService companyDetailsService;

    @Test
    public void testGetCompanyDetails() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        CompanyDetailsReadDTO readDTO = companyDetailsService.getCompanyDetails(companyDetails.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(companyDetails);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCompanyDetailsWrongId() {
        companyDetailsService.getCompanyDetails(UUID.randomUUID());
    }

    @Test
    public void testCreateCompanyDetails() {
        CompanyDetailsCreateDTO create = testObjectsFactory.createCompanyDetailsCreateDTO();

        CompanyDetailsReadDTO read = companyDetailsService.createCompanyDetails(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        CompanyDetails companyDetails = companyDetailsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(companyDetails);
    }

    @Test
    public void testPatchCompanyDetails() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        CompanyDetailsPatchDTO patch = testObjectsFactory.createCompanyDetailsPatchDTO();

        CompanyDetailsReadDTO read = companyDetailsService.patchCompanyDetails(companyDetails.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        companyDetails = companyDetailsRepository.findById(read.getId()).get();
        Assertions.assertThat(companyDetails).isEqualToIgnoringGivenFields(read,
                "movieProdTypeCompanies");
    }

    @Test
    public void testPatchCompanyDetailsEmptyPatch() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        CompanyDetailsPatchDTO patch = new CompanyDetailsPatchDTO();
        CompanyDetailsReadDTO read = companyDetailsService.patchCompanyDetails(companyDetails.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getOverview());
        Assert.assertNotNull(read.getYearOfFoundation());

        testObjectsFactory.inTransaction(() -> {
            CompanyDetails companyDetailsAfterUpdate = companyDetailsRepository.findById(read.getId()).get();

            Assert.assertNotNull(companyDetailsAfterUpdate.getName());
            Assert.assertNotNull(companyDetailsAfterUpdate.getOverview());
            Assert.assertNotNull(companyDetailsAfterUpdate.getYearOfFoundation());

            Assertions.assertThat(companyDetails).isEqualToComparingFieldByField(companyDetailsAfterUpdate);
        });
    }

    @Test
    public void testDeleteCompanyDetails() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        companyDetailsService.deleteCompanyDetails(companyDetails.getId());
        Assert.assertFalse(companyDetailsRepository.existsById(companyDetails.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCompanyDetailsNotFound() {
        companyDetailsService.deleteCompanyDetails(UUID.randomUUID());
    }

    @Test
    public void testPutCompanyDetails() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        CompanyDetailsPutDTO put = testObjectsFactory.createCompanyDetailsPutDTO();

        CompanyDetailsReadDTO read = companyDetailsService.updateCompanyDetails(companyDetails.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        companyDetails = companyDetailsRepository.findById(read.getId()).get();
        Assertions.assertThat(companyDetails).isEqualToIgnoringGivenFields(read,
                "movieProdTypeCompanies");
    }

    @Test
    public void testPutCompanyDetailsEmptyPut() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();

        CompanyDetailsPutDTO put = new CompanyDetailsPutDTO();
        CompanyDetailsReadDTO read = companyDetailsService.updateCompanyDetails(companyDetails.getId(), put);

        Assert.assertNotNull(read.getName());
        Assert.assertNull(read.getOverview());
        Assert.assertNull(read.getYearOfFoundation());

        testObjectsFactory.inTransaction(() -> {
            CompanyDetails companyDetailsAfterUpdate = companyDetailsRepository.findById(read.getId()).get();

            Assert.assertNotNull(companyDetailsAfterUpdate.getName());
            Assert.assertNull(companyDetailsAfterUpdate.getOverview());
            Assert.assertNull(companyDetailsAfterUpdate.getYearOfFoundation());
        });
    }

    @Test
    public void testGetVisitsWithEmptyFilterWithPagingAndSorting() {
        testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2011, 12, 31));
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2010, 12, 31));
        CompanyDetails c4 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        PageRequest pageRequest = PageRequest.of(1,2, Sort.by(Sort.Direction.DESC, "yearOfFoundation"));
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, pageRequest).getData()).extracting("id")
                .isEqualTo((Arrays.asList(c4.getId(), c2.getId())));
    }
}
