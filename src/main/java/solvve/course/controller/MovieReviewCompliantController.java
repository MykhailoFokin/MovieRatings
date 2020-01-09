package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.service.MovieReviewCompliantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviewcompliant")
public class MovieReviewCompliantController {

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

    @GetMapping("/{id}")
    public MovieReviewCompliantReadDTO getMovieReviewCompliant(@PathVariable UUID id) {
        return movieReviewCompliantService.getMovieReviewCompliant(id);
    }

    @PostMapping
    public MovieReviewCompliantReadDTO createMovieReviewCompliant(@RequestBody MovieReviewCompliantCreateDTO createDTO){
        return movieReviewCompliantService.createMovieReviewCompliant(createDTO);
    }
}