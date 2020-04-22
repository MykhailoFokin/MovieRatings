package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.dto.CountryFilter;
import solvve.course.service.CountryService;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryRepositoryTest extends BaseTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryService countryService;

    @Test
    public void testSave() {
        Movie m = testObjectsFactory.createMovie();
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
        Assertions.assertThat(countryService.getCountries(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(),c2.getId(),c3.getId());
    }

    @Test
    public void testGetCountriesByNames() {
        Country c1 = testObjectsFactory.createCountry("Ukraine");
        Country c2 = testObjectsFactory.createCountry("Germany");
        testObjectsFactory.createCountry("Poland");

        CountryFilter filter = new CountryFilter();
        filter.setNames(Set.of(c1.getName(), c2.getName()));
        Assertions.assertThat(countryService.getCountries(filter, Pageable.unpaged()).getData())
                .extracting("Name")
                .containsExactlyInAnyOrder(c1.getName(),c2.getName());
    }

    @Test
    public void testCreatedAtIsSet() {
        Country country = testObjectsFactory.createCountry();

        Instant createdAtBeforeReload = country.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        country = countryRepository.findById(country.getId()).get();

        Instant createdAtAfterReload = country.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Country country = testObjectsFactory.createCountry();

        Instant updatedAtBeforeReload = country.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        country = countryRepository.findById(country.getId()).get();

        Instant updatedAtAfterReload = country.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        Country country = testObjectsFactory.createCountry();

        Instant updatedAtBeforeReload = country.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        country.setName("NewNameTest");
        countryRepository.save(country);

        testObjectsFactory.inTransaction(() -> {
            Country countryAtAfterReload = countryRepository.findById(country.getId()).get();

            Instant updatedAtAfterReload = countryAtAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveCountryValidation() {
        Country entity = new Country();
        countryRepository.save(entity);
    }
}
