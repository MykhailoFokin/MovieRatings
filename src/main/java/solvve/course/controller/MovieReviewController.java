package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.service.MovieReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviews")
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

    @PatchMapping("/{id}")
    public MovieReviewReadDTO patchMovieReview(@PathVariable UUID id, @RequestBody MovieReviewPatchDTO patch){
        return movieReviewService.patchMovieReview(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieReview(@PathVariable UUID id){
        movieReviewService.deleteMovieReview(id);
    }

    @PutMapping("/{id}")
    public MovieReviewReadDTO putMovieReview(@PathVariable UUID id, @RequestBody MovieReviewPutDTO put){
        return movieReviewService.putMovieReview(id, put);
    }
}
