package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Country;
import solvve.course.dto.CountryCreateDTO;
import solvve.course.dto.CountryPatchDTO;
import solvve.course.dto.CountryPutDTO;
import solvve.course.dto.CountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;

import java.util.UUID;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public CountryReadDTO getCountries(UUID id) {
        Country country = getCountriesRequired(id);
        return translationService.toRead(country);
    }

    public CountryReadDTO createCountries(CountryCreateDTO create) {
        Country country = translationService.toEntity(create);

        country = countryRepository.save(country);
        return translationService.toRead(country);
    }

    public CountryReadDTO patchCountries(UUID id, CountryPatchDTO patch) {
        Country country = getCountriesRequired(id);

        translationService.patchEntity(patch, country);

        country = countryRepository.save(country);
        return translationService.toRead(country);
    }

    private Country getCountriesRequired(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Country.class, id);
        });
    }

    public void deleteCountries(UUID id) {
        countryRepository.delete(getCountriesRequired(id));
    }

    public CountryReadDTO putCountries(UUID id, CountryPutDTO put) {
        Country country = getCountriesRequired(id);

        translationService.putEntity(put, country);

        country = countryRepository.save(country);
        return translationService.toRead(country);
    }
}
