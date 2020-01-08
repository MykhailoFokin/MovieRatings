package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.service.MovieSpoilerDataService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviespoilerdata")
public class MovieSpoilerDataController {

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @GetMapping("/{id}")
    public MovieSpoilerDataReadDTO getMovieSpoilerData(@PathVariable UUID id) {
        return movieSpoilerDataService.getMovieSpoilerData(id);
    }

    @PostMapping
    public MovieSpoilerDataReadDTO createMovieSpoilerData(@RequestBody MovieSpoilerDataCreateDTO createDTO){
        return movieSpoilerDataService.createMovieSpoilerData(createDTO);
    }
}
