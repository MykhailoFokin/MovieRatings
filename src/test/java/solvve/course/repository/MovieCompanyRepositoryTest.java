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
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.MovieCompanyFilter;
import solvve.course.service.MovieCompanyService;
import solvve.course.utils.TestObjectsFactory;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_company"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieCompanyRepositoryTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private MovieCompanyService movieCompanyService;

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
        filter.setCompanyId(c2.getId());
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
        filter.setCompanyId(c1.getId());
        Assertions.assertThat(movieCompanyService.getMovieCompanies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId());
    }
}
