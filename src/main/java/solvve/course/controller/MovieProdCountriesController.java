package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieProdCountriesCreateDTO;
import solvve.course.dto.MovieProdCountriesReadDTO;
import solvve.course.service.MovieProdCountriesService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movieprodcountries")
public class MovieProdCountriesController {

    @Autowired
    private MovieProdCountriesService movieProdCountriesService;

    @GetMapping("/{id}")
    public MovieProdCountriesReadDTO getMovieProdCountries(@PathVariable UUID id) {
        return movieProdCountriesService.getMovieProdCountries(id);
    }

    @PostMapping
    public MovieProdCountriesReadDTO createMovieProdCountries(@RequestBody MovieProdCountriesCreateDTO createDTO){
        return movieProdCountriesService.createMovieProdCountries(createDTO);
    }
}
