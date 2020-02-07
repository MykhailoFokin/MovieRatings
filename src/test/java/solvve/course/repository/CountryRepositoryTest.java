package solvve.course.repository;

import org.assertj.core.api.Assertions;
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

    @Test
    public void testSave() {
        Movie m = new Movie();
        m.setTitle("New movie");
        m = movieRepository.save(m);
        Set<Movie> ms = new HashSet<>();
        ms.add(m);

        Country c = createCountry("Ukraine");
        c.setMovies(new HashSet<Movie>(ms));
        c = countryRepository.save(c);
        assertNotNull(c.getId());
        assertTrue(countryRepository.findById(c.getId()).isPresent());
    }

    @Test
    public void testGetCountriesByEmptyFilter() {
        Country c1 = createCountry("Ukraine");
        Country c2 = createCountry("Germany");
        Country c3 = createCountry("Poland");

        CountryFilter filter = new CountryFilter();
        Assertions.assertThat(countryService.getCountries(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(),c2.getId(),c3.getId());
    }

    @Test
    public void testGetCountriesByNames() {
        Country c1 = createCountry("Ukraine");
        Country c2 = createCountry("Germany");
        createCountry("Poland");

        CountryFilter filter = new CountryFilter();
        filter.setNames(Set.of(c1.getName(), c2.getName()));
        Assertions.assertThat(countryService.getCountries(filter)).extracting("Name")
                .containsExactlyInAnyOrder(c1.getName(),c2.getName());
    }

    private Country createCountry(String countryName) {
        Country country = new Country();
        country.setId(UUID.randomUUID());
        country.setName(countryName);
        country = countryRepository.save(country);
        return country;
    }
}
