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
import solvve.course.domain.Countries;
import solvve.course.dto.CountriesCreateDTO;
import solvve.course.dto.CountriesPatchDTO;
import solvve.course.dto.CountriesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountriesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from countries", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CountriesServiceTest {

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private CountriesService countriesService;

    private Countries createCountries() {
        Countries countries = new Countries();
        countries.setId(UUID.randomUUID());
        countries.setName("Laplandia");
        return countriesRepository.save(countries);
    }

    @Test
    public void testGetCountries() {
        Countries countries = createCountries();

        CountriesReadDTO readDTO = countriesService.getCountries(countries.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(countries);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        countriesService.getCountries(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateCountries() {
        CountriesCreateDTO create = new CountriesCreateDTO();
        create.setName("Laplandia");
        CountriesReadDTO read = countriesService.createCountries(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Countries countries = countriesRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(countries);
    }

    @Transactional
    @Test
    public void testPatchCountries() {
        Countries countries = createCountries();

        CountriesPatchDTO patch = new CountriesPatchDTO();
        patch.setName("Laplandia");
        CountriesReadDTO read = countriesService.patchCountries(countries.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        countries = countriesRepository.findById(read.getId()).get();
        Assertions.assertThat(countries).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchCountriesEmptyPatch() {
        Countries countries = createCountries();

        CountriesPatchDTO patch = new CountriesPatchDTO();
        CountriesReadDTO read = countriesService.patchCountries(countries.getId(), patch);

        Assert.assertNotNull(read.getName());

        Countries countriesAfterUpdate = countriesRepository.findById(read.getId()).get();

        Assert.assertNotNull(countriesAfterUpdate.getName());

        Assertions.assertThat(countries).isEqualToComparingFieldByField(countriesAfterUpdate);
    }

    @Test
    public void testDeleteCountries() {
        Countries countries = createCountries();

        countriesService.deleteCountries(countries.getId());
        Assert.assertFalse(countriesRepository.existsById(countries.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCountriesNotFound() {
        countriesService.deleteCountries(UUID.randomUUID());
    }
}
