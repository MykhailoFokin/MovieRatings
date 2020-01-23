package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Country;
import solvve.course.dto.CountryCreateDTO;
import solvve.course.dto.CountryPatchDTO;
import solvve.course.dto.CountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountryRepository;

import java.util.UUID;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public CountryReadDTO getCountries(UUID id) {
        Country country = getCountriesRequired(id);
        return toRead(country);
    }

    private CountryReadDTO toRead(Country country) {
        CountryReadDTO dto = new CountryReadDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    public CountryReadDTO createCountries(CountryCreateDTO create) {
        Country country = new Country();
        country.setName(create.getName());

        country = countryRepository.save(country);
        return toRead(country);
    }

    public CountryReadDTO patchCountries(UUID id, CountryPatchDTO patch) {
        Country country = getCountriesRequired(id);

        if (patch.getName()!=null) {
            country.setName(patch.getName());
        }
        if (patch.getMovies()!=null) {
            country.setMovies(patch.getMovies());
        }
        country = countryRepository.save(country);
        return toRead(country);
    }

    private Country getCountriesRequired(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Country.class, id);
        });
    }

    public void deleteCountries(UUID id) {
        countryRepository.delete(getCountriesRequired(id));
    }
}
