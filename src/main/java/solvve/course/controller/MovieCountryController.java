package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieCountryReadDTO;
import solvve.course.service.MovieCountryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies/{movieId}/countries")
public class MovieCountryController {

    @Autowired
    private MovieCountryService movieCountryService;

    @GetMapping
    public List<MovieCountryReadDTO> getMovieCountries(@PathVariable UUID movieId) {
        return movieCountryService.getMovieCountries(movieId);
    }

    @PostMapping("/{id}")
    public List<MovieCountryReadDTO> addCountryToMovie(@PathVariable UUID movieId,
                                                         @PathVariable UUID id) {
        return movieCountryService.addCountryToMovie(movieId, id);
    }

    @DeleteMapping("/{id}")
    public List<MovieCountryReadDTO> deleteMovie(@PathVariable UUID movieId, @PathVariable UUID id) {
        return movieCountryService.removeCountryFromMovie(movieId, id);
    }
}
