package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
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
    public MovieSpoilerDataReadDTO createMovieSpoilerData(@RequestBody MovieSpoilerDataCreateDTO createDTO) {
        return movieSpoilerDataService.createMovieSpoilerData(createDTO);
    }

    @PatchMapping("/{id}")
    public MovieSpoilerDataReadDTO patchMovieSpoilerData(@PathVariable UUID id, @RequestBody MovieSpoilerDataPatchDTO patch) {
        return movieSpoilerDataService.patchMovieSpoilerData(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieSpoilerData(@PathVariable UUID id) {
        movieSpoilerDataService.deleteMovieSpoilerData(id);
    }

    @PutMapping("/{id}")
    public MovieSpoilerDataReadDTO putMovieSpoilerData(@PathVariable UUID id, @RequestBody MovieSpoilerDataPutDTO put) {
        return movieSpoilerDataService.putMovieSpoilerData(id, put);
    }
}
