package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.CountriesCreateDTO;
import solvve.course.dto.CountriesReadDTO;
import solvve.course.service.CountriesService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/countries")
public class CountriesController {

    @Autowired
    private CountriesService countriesService;

    @GetMapping("/{id}")
    public CountriesReadDTO getCountries(@PathVariable UUID id) {
        return countriesService.getCountries(id);
    }

    @PostMapping
    public CountriesReadDTO createCountries(@RequestBody CountriesCreateDTO createDTO){
        return countriesService.createCountries(createDTO);
    }
}
