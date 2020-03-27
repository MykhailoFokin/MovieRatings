package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CountryService;

import javax.validation.Valid;
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

    @GetMapping
    public PageResult<CountryReadDTO> getCountries(CountryFilter countryFilter, Pageable pageable) {
        return countryService.getCountries(countryFilter, pageable);
    }

    @PostMapping
    public CountryReadDTO createCountries(@RequestBody @Valid CountryCreateDTO createDTO) {
        return countryService.createCountries(createDTO);
    }

    @PatchMapping("/{id}")
    public CountryReadDTO patchCountries(@PathVariable UUID id, @RequestBody @Valid CountryPatchDTO patch) {
        return countryService.patchCountries(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCountries(@PathVariable UUID id) {
        countryService.deleteCountries(id);
    }

    @PutMapping("/{id}")
    public CountryReadDTO putCountries(@PathVariable UUID id, @RequestBody @Valid CountryPutDTO put) {
        return countryService.updateCountries(id, put);
    }
}
