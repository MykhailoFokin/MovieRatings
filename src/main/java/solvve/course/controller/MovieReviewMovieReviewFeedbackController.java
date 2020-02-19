package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.service.MovieReviewMovieReviewFeedbackService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks")
public class MovieReviewMovieReviewFeedbackController {

    @Autowired
    private MovieReviewMovieReviewFeedbackService movieReviewFeedbackService;

    @GetMapping
    public List<MovieReviewFeedbackReadDTO> getMovieReviewSpoilerData(
            @PathVariable UUID movieReviewId) {
        return movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(movieReviewId);
    }

    @PostMapping
    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                                @RequestBody MovieReviewFeedbackCreateDTO createDTO) {
        return movieReviewFeedbackService.createMovieReviewMovieReviewFeedback(movieReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                               @PathVariable (value = "id") UUID id,
                                                               @RequestBody MovieReviewFeedbackPatchDTO patch) {
        return movieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                          @PathVariable (value = "id") UUID id) {
        movieReviewFeedbackService.deleteMovieReviewMovieReviewFeedback(movieReviewId, id);
    }

    @PutMapping("/{id}")
    public MovieReviewFeedbackReadDTO putMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                             @PathVariable (value = "id") UUID id,
                                                             @RequestBody MovieReviewFeedbackPutDTO put) {
        return movieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReviewId, id, put);
    }
}
