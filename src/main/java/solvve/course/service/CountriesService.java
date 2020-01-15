package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Countries;
import solvve.course.dto.CountriesCreateDTO;
import solvve.course.dto.CountriesPatchDTO;
import solvve.course.dto.CountriesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CountriesRepository;

import java.util.UUID;

@Service
public class CountriesService {

    @Autowired
    private CountriesRepository countriesRepository;

    @Transactional(readOnly = true)
    public CountriesReadDTO getCountries(UUID id) {
        Countries countries = getCountriesRequired(id);
        return toRead(countries);
    }

    private CountriesReadDTO toRead(Countries countries) {
        CountriesReadDTO dto = new CountriesReadDTO();
        dto.setId(countries.getId());
        dto.setName(countries.getName());
        return dto;
    }

    public CountriesReadDTO createCountries(CountriesCreateDTO create) {
        Countries countries = new Countries();
        countries.setName(create.getName());

        countries = countriesRepository.save(countries);
        return toRead(countries);
    }

    public CountriesReadDTO patchCountries(UUID id, CountriesPatchDTO patch) {
        Countries countries = getCountriesRequired(id);

        if (patch.getName()!=null) {
            countries.setName(patch.getName());
        }
        if (patch.getMovies()!=null) {
            countries.setMovies(patch.getMovies());
        }
        countries = countriesRepository.save(countries);
        return toRead(countries);
    }

    private Countries getCountriesRequired(UUID id) {
        return countriesRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Countries.class, id);
        });
    }

    public void deleteCountries(UUID id) {
        countriesRepository.delete(getCountriesRequired(id));
    }
}
