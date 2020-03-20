package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Country;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public CountryReadDTO getCountries(UUID id) {
        Country country = getCountriesRequired(id);
        return translationService.translate(country, CountryReadDTO.class);
    }

    public List<CountryReadDTO> getCountries(CountryFilter countryFilter) {
        List<Country> countries = countryRepository.findByFilter(countryFilter);
        return countries.stream().map(e -> translationService.translate(e, CountryReadDTO.class))
                .collect(Collectors.toList());
    }

    public CountryReadDTO createCountries(CountryCreateDTO create) {
        Country country = translationService.translate(create, Country.class);

        country = countryRepository.save(country);
        return translationService.translate(country, CountryReadDTO.class);
    }

    public CountryReadDTO patchCountries(UUID id, CountryPatchDTO patch) {
        Country country = getCountriesRequired(id);

        translationService.map(patch, country);

        country = countryRepository.save(country);
        return translationService.translate(country, CountryReadDTO.class);
    }

    public void deleteCountries(UUID id) {
        countryRepository.delete(getCountriesRequired(id));
    }

    public CountryReadDTO updateCountries(UUID id, CountryPutDTO put) {
        Country country = getCountriesRequired(id);

        translationService.updateEntity(put, country);

        country = countryRepository.save(country);
        return translationService.translate(country, CountryReadDTO.class);
    }

    private Country getCountriesRequired(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Country.class, id);
        });
    }
}
