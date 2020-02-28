package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.domain.CompanyDetails;
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.MovieCompanyFilter;
import solvve.course.service.MovieCompanyService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_company","delete from company_details"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieCompanyRepositoryTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private MovieCompanyService movieCompanyService;

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Test
    public void testGetMovieCompanyByEmptyFilter() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails();
        MovieCompany m1 = testObjectsFactory.createMovieCompany(c1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany m2 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.DISTRIBUTORS);
        MovieCompany m3 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.OTHER_COMPANIES);
        MovieCompany m4 = testObjectsFactory.createMovieCompany(c3, MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyFilter filter = new MovieCompanyFilter();
        Assertions.assertThat(movieCompanyService.getMovieCompanies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m3.getId(), m4.getId());
    }

    @Test
    public void testGetMovieCompanyByMovieProductionType() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails();
        testObjectsFactory.createMovieCompany(c1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany m2 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.DISTRIBUTORS);
        MovieCompany m3 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.OTHER_COMPANIES);
        testObjectsFactory.createMovieCompany(c3, MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyFilter filter = new MovieCompanyFilter();
        filter.setMovieProductionTypes(List.of(MovieProductionType.DISTRIBUTORS,
                MovieProductionType.OTHER_COMPANIES));
        Assertions.assertThat(movieCompanyService.getMovieCompanies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieCompanyByCompanyDetail() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails();
        testObjectsFactory.createMovieCompany(c1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany m2 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.DISTRIBUTORS);
        MovieCompany m3 = testObjectsFactory.createMovieCompany(c2, MovieProductionType.OTHER_COMPANIES);
        testObjectsFactory.createMovieCompany(c3, MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyFilter filter = new MovieCompanyFilter();
        filter.setCompanyDetailsId(c2.getId());
        Assertions.assertThat(movieCompanyService.getMovieCompanies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieCompanyByAllFilters() {
        CompanyDetails c1 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c2 = testObjectsFactory.createCompanyDetails();
        CompanyDetails c3 = testObjectsFactory.createCompanyDetails();
        MovieCompany m1 = testObjectsFactory.createMovieCompany(c1, MovieProductionType.PRODUCTION_COMPANIES);
        testObjectsFactory.createMovieCompany(c2, MovieProductionType.DISTRIBUTORS);
        testObjectsFactory.createMovieCompany(c2, MovieProductionType.OTHER_COMPANIES);
        testObjectsFactory.createMovieCompany(c3, MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyFilter filter = new MovieCompanyFilter();
        filter.setMovieProductionTypes(List.of(MovieProductionType.PRODUCTION_COMPANIES,
                MovieProductionType.DISTRIBUTORS, MovieProductionType.OTHER_COMPANIES));
        filter.setCompanyDetailsId(c1.getId());
        Assertions.assertThat(movieCompanyService.getMovieCompanies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId());
    }

    @Test
    public void testCteatedAtIsSet() {
        CompanyDetails c = testObjectsFactory.createCompanyDetails();
        MovieCompany entity = testObjectsFactory.createMovieCompany(c, MovieProductionType.PRODUCTION_COMPANIES);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieCompanyRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        CompanyDetails c = testObjectsFactory.createCompanyDetails();
        MovieCompany entity = testObjectsFactory.createMovieCompany(c, MovieProductionType.PRODUCTION_COMPANIES);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = movieCompanyRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        CompanyDetails c = testObjectsFactory.createCompanyDetails();
        MovieCompany entity = testObjectsFactory.createMovieCompany(c, MovieProductionType.PRODUCTION_COMPANIES);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        movieCompanyRepository.save(entity);
        entity = movieCompanyRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
