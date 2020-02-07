package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;
import solvve.course.service.CompanyDetailsService;
import solvve.course.utils.TestObjectsFactory;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from company_details"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CompanyDetailsRepositoryTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private CompanyDetailsService companyDetailsService;

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
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter)).extracting("Id")
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
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter)).extracting("Id")
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
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c2.getId(), c3.getId());
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
        Assertions.assertThat(companyDetailsService.getCompanyDetails(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c2.getId());
    }
}
