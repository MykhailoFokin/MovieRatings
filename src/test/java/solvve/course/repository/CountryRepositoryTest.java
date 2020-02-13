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
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.dto.CountryFilter;
import solvve.course.service.CountryService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_prod_countries",
        "delete from movie",
        "delete from country"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Movie m = new Movie();
        m.setTitle("New movie");
        m = movieRepository.save(m);
        Set<Movie> ms = new HashSet<>();
        ms.add(m);

        Country c = testObjectsFactory.createCountry("Ukraine");
        c.setMovies(new HashSet<Movie>(ms));
        c = countryRepository.save(c);
        assertNotNull(c.getId());
        assertTrue(countryRepository.findById(c.getId()).isPresent());
    }

    @Test
    public void testGetCountriesByEmptyFilter() {
        Country c1 = testObjectsFactory.createCountry("Ukraine");
        Country c2 = testObjectsFactory.createCountry("Germany");
        Country c3 = testObjectsFactory.createCountry("Poland");

        CountryFilter filter = new CountryFilter();
        Assertions.assertThat(countryService.getCountries(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(),c2.getId(),c3.getId());
    }

    @Test
    public void testGetCountriesByNames() {
        Country c1 = testObjectsFactory.createCountry("Ukraine");
        Country c2 = testObjectsFactory.createCountry("Germany");
        testObjectsFactory.createCountry("Poland");

        CountryFilter filter = new CountryFilter();
        filter.setNames(Set.of(c1.getName(), c2.getName()));
        Assertions.assertThat(countryService.getCountries(filter)).extracting("Name")
                .containsExactlyInAnyOrder(c1.getName(),c2.getName());
    }

    @Test
    public void testCteatedAtIsSet() {
        Country country = testObjectsFactory.createCountry();

        Instant createdAtBeforeReload = country.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        country = countryRepository.findById(country.getId()).get();

        Instant createdAtAfterReload = country.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        Country country = testObjectsFactory.createCountry();

        Instant modifiedAtBeforeReload = country.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        country = countryRepository.findById(country.getId()).get();

        Instant modifiedAtAfterReload = country.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Country country = testObjectsFactory.createCountry();

        Instant modifiedAtBeforeReload = country.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        country.setName("NewNameTest");
        countryRepository.save(country);
        country = countryRepository.findById(country.getId()).get();

        Instant modifiedAtAfterReload = country.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
