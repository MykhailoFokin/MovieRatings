package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.service.MovieReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereview")
public class MovieReviewController {

    @Autowired
    private MovieReviewService movieReviewService;

    @GetMapping("/{id}")
    public MovieReviewReadDTO getMovieReview(@PathVariable UUID id) {
        return movieReviewService.getMovieReview(id);
    }

    @PostMapping
    public MovieReviewReadDTO createMovieReview(@RequestBody MovieReviewCreateDTO createDTO){
        return movieReviewService.createMovieReview(createDTO);
    }
}
