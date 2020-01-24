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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Country;
import solvve.course.dto.CountryCreateDTO;
import solvve.course.dto.CountryPatchDTO;
import solvve.course.dto.CountryPutDTO;
import solvve.course.dto.CountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;

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

    private Country createCountries() {
        Country country = new Country();
        country.setId(UUID.randomUUID());
        country.setName("Laplandia");
        return countryRepository.save(country);
    }

    @Test
    public void testGetCountries() {
        Country country = createCountries();

        CountryReadDTO readDTO = countryService.getCountries(country.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(country);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        countryService.getCountries(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateCountries() {
        CountryCreateDTO create = new CountryCreateDTO();
        create.setName("Laplandia");
        CountryReadDTO read = countryService.createCountries(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Country country = countryRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(country);
    }

    @Transactional
    @Test
    public void testPatchCountries() {
        Country country = createCountries();

        CountryPatchDTO patch = new CountryPatchDTO();
        patch.setName("Laplandia");
        CountryReadDTO read = countryService.patchCountries(country.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        country = countryRepository.findById(read.getId()).get();
        Assertions.assertThat(country).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchCountriesEmptyPatch() {
        Country country = createCountries();

        CountryPatchDTO patch = new CountryPatchDTO();
        CountryReadDTO read = countryService.patchCountries(country.getId(), patch);

        Assert.assertNotNull(read.getName());

        Country countryAfterUpdate = countryRepository.findById(read.getId()).get();

        Assert.assertNotNull(countryAfterUpdate.getName());

        Assertions.assertThat(country).isEqualToComparingFieldByField(countryAfterUpdate);
    }

    @Test
    public void testDeleteCountries() {
        Country country = createCountries();

        countryService.deleteCountries(country.getId());
        Assert.assertFalse(countryRepository.existsById(country.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCountriesNotFound() {
        countryService.deleteCountries(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutCountries() {
        Country country = createCountries();

        CountryPutDTO put = new CountryPutDTO();
        put.setName("Laplandia");
        CountryReadDTO read = countryService.putCountries(country.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        country = countryRepository.findById(read.getId()).get();
        Assertions.assertThat(country).isEqualToComparingFieldByField(read);
    }
}
