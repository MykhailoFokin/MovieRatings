package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.service.MovieReviewFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviewfeedbacks")
public class MovieReviewFeedbackController {

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @GetMapping("/{id}")
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(@PathVariable UUID id) {
        return movieReviewFeedbackService.getMovieReviewFeedback(id);
    }

    @PostMapping
    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(@RequestBody MovieReviewFeedbackCreateDTO createDTO) {
        return movieReviewFeedbackService.createMovieReviewFeedback(createDTO);
    }

    @PatchMapping("/{id}")
    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(@PathVariable UUID id,
                                                               @RequestBody MovieReviewFeedbackPatchDTO patch) {
        return movieReviewFeedbackService.patchMovieReviewFeedback(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieReviewFeedback(@PathVariable UUID id) {
        movieReviewFeedbackService.deleteMovieReviewFeedback(id);
    }

    @PutMapping("/{id}")
    public MovieReviewFeedbackReadDTO putMovieReviewFeedback(@PathVariable UUID id,
                                                             @RequestBody MovieReviewFeedbackPutDTO put) {
        return movieReviewFeedbackService.updateMovieReviewFeedback(id, put);
    }
}
