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
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsCreateDTO;
import solvve.course.dto.CompanyDetailsPatchDTO;
import solvve.course.dto.CompanyDetailsPutDTO;
import solvve.course.dto.CompanyDetailsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CompanyDetailsRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from company_details", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CompanyDetailsServiceTest {

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private CompanyDetailsService companyDetailsService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

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

        Assert.assertNull(read.getName());
        Assert.assertNull(read.getOverview());
        Assert.assertNull(read.getYearOfFoundation());

        testObjectsFactory.inTransaction(() -> {
            CompanyDetails companyDetailsAfterUpdate = companyDetailsRepository.findById(read.getId()).get();

            Assert.assertNull(companyDetailsAfterUpdate.getName());
            Assert.assertNull(companyDetailsAfterUpdate.getOverview());
            Assert.assertNull(companyDetailsAfterUpdate.getYearOfFoundation());
        });
    }
}
