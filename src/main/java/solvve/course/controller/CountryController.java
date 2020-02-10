package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CountryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/{id}")
    public CountryReadDTO getCountries(@PathVariable UUID id) {
        return countryService.getCountries(id);
    }

    @PostMapping
    public CountryReadDTO createCountries(@RequestBody CountryCreateDTO createDTO) {
        return countryService.createCountries(createDTO);
    }

    @PatchMapping("/{id}")
    public CountryReadDTO patchCountries(@PathVariable UUID id, @RequestBody CountryPatchDTO patch) {
        return countryService.patchCountries(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCountries(@PathVariable UUID id) {
        countryService.deleteCountries(id);
    }

    @PutMapping("/{id}")
    public CountryReadDTO putCountries(@PathVariable UUID id, @RequestBody CountryPutDTO put) {
        return countryService.updateCountries(id, put);
    }

    @GetMapping
    public List<CountryReadDTO> getCountries(CountryFilter countryFilter) {
        return countryService.getCountries(countryFilter);
    }
}
