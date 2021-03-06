package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;
import solvve.course.service.CompanyDetailsService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CompanyDetailsRepositoryTest extends BaseTest {

    @Autowired
    private CompanyDetailsService companyDetailsService;

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Test
    public void testGetCompanyDetailByEmptyFilter() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2001, 12, 31));
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2002, 12, 31));
        CompanyDetails c4 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c2.getId(), c3.getId(), c4.getId());
    }

    @Test
    public void testGetCompanyDetailByName() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2001, 12, 31));
        testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2002, 12, 31));
        testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        filter.setName("Paramaount");
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c3.getId());
    }

    @Test
    public void testGetCompanyDetailYearOfFoundation() {
        testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2001, 12, 31));
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2002, 12, 31));
        testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        filter.setYearOfFoundation(LocalDate.of(2002, 12, 31));
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c2.getId(), c3.getId());
    }

    @Test
    public void testGetCompanyDetailYearsOfFoundation() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2001, 12, 31));
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2002, 12, 31));
        testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        filter.setYearsOfFoundation(List.of(LocalDate.of(2002, 12, 31),
                LocalDate.of(2001, 12, 31)));
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c2.getId(), c3.getId());
    }

    @Test
    public void testGetCompanyDetailByAllFilters() {
        testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview1", LocalDate.of(2001, 12, 31));
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview1", LocalDate.of(2002, 12, 31));
        testObjectsFactory.createCompanyDetails("Paramaount",
                "Overview2", LocalDate.of(2002, 12, 31));
        CompanyDetails c4 = testObjectsFactory.createCompanyDetails("20CenturyFox",
                "Overview3", LocalDate.of(2004, 12, 31));

        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        filter.setName("20CenturyFox");
        filter.setYearOfFoundation(LocalDate.of(2002, 12, 31));
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c2.getId());
    }

    @Test
    public void testCreatedAtIsSet() {
        CompanyDetails company = testObjectsFactory.createCompanyDetails();

        Instant createdAtBeforeReload = company.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        company = companyDetailsRepository.findById(company.getId()).get();

        Instant createdAtAfterReload = company.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        CompanyDetails company = testObjectsFactory.createCompanyDetails();

        Instant updatedAtBeforeReload = company.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        company = companyDetailsRepository.findById(company.getId()).get();

        Instant updatedAtAfterReload = company.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        CompanyDetails company = testObjectsFactory.createCompanyDetails();

        Instant updatedAtBeforeReload = company.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        company.setName("NewNameTest");
        company = companyDetailsRepository.save(company);
        UUID companyId = company.getId();

        testObjectsFactory.inTransaction(() -> {
            CompanyDetails companyAfterReload = companyDetailsRepository.findById(companyId).get();

            Instant updatedAtAfterReload = companyAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveCompanyDetailsValidation() {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetailsRepository.save(companyDetails);
    }
}
