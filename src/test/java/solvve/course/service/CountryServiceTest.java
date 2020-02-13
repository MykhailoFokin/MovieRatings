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
import solvve.course.domain.Country;
import solvve.course.dto.CountryCreateDTO;
import solvve.course.dto.CountryPatchDTO;
import solvve.course.dto.CountryPutDTO;
import solvve.course.dto.CountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from country", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CountryServiceTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetCountries() {
        Country country = testObjectsFactory.createCountry();

        CountryReadDTO readDTO = countryService.getCountries(country.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(country);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        countryService.getCountries(UUID.randomUUID());
    }

    @Test
    public void testCreateCountries() {
        CountryCreateDTO create = new CountryCreateDTO();
        create.setName("Ukraine");
        CountryReadDTO read = countryService.createCountries(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        testObjectsFactory.inTransaction(() -> {
            Country country = countryRepository.findById(read.getId()).get();
            Assertions.assertThat(read).isEqualToComparingFieldByField(country);
        });
    }

    @Test
    public void testPatchCountries() {
        Country country = testObjectsFactory.createCountry();

        CountryPatchDTO patch = new CountryPatchDTO();
        patch.setName("Ukraine");
        CountryReadDTO read = countryService.patchCountries(country.getId(), patch);

        Assertions.assertThat(patch).isEqualToIgnoringGivenFields(read,"movies",
                "releaseDetails","countryLanguages");

        testObjectsFactory.inTransaction(() -> {
            Country country1 = countryRepository.findById(read.getId()).get();
            Assertions.assertThat(country1).isEqualToIgnoringGivenFields(read, "movies",
                    "releaseDetails", "countryLanguages");
        });
    }

    @Test
    public void testPatchCountriesEmptyPatch() {
        Country country = testObjectsFactory.createCountry();

        CountryPatchDTO patch = new CountryPatchDTO();
        CountryReadDTO read = countryService.patchCountries(country.getId(), patch);

        Assert.assertNotNull(read.getName());

        testObjectsFactory.inTransaction(() -> {
            Country countryAfterUpdate = countryRepository.findById(read.getId()).get();

            Assert.assertNotNull(countryAfterUpdate.getName());

            Assertions.assertThat(country).isEqualToComparingFieldByField(countryAfterUpdate);
        });
    }

    @Test
    public void testDeleteCountries() {
        Country country = testObjectsFactory.createCountry();

        countryService.deleteCountries(country.getId());
        Assert.assertFalse(countryRepository.existsById(country.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCountriesNotFound() {
        countryService.deleteCountries(UUID.randomUUID());
    }

    @Test
    public void testPutCountries() {
        Country country = testObjectsFactory.createCountry();

        CountryPutDTO put = new CountryPutDTO();
        put.setName("Ukraine");
        CountryReadDTO read = countryService.updateCountries(country.getId(), put);

        Assertions.assertThat(put).isEqualToIgnoringGivenFields(read,"movies",
                "releaseDetails","countryLanguages");

        testObjectsFactory.inTransaction(() -> {
            Country country1 = countryRepository.findById(read.getId()).get();
            Assertions.assertThat(country1).isEqualToIgnoringGivenFields(read, "movies",
                    "releaseDetails", "countryLanguages");
        });
    }

    @Test
    public void testPutCountriesEmptyPut() {
        Country country = testObjectsFactory.createCountry();

        CountryPutDTO put = new CountryPutDTO();
        CountryReadDTO read = countryService.updateCountries(country.getId(), put);

        Assert.assertNull(read.getName());

        testObjectsFactory.inTransaction(() -> {
            Country countryAfterUpdate = countryRepository.findById(read.getId()).get();

            Assert.assertNull(countryAfterUpdate.getName());
        });
    }
}
