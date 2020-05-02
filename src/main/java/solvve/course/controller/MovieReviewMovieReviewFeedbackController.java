package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.service.MovieReviewMovieReviewFeedbackService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks")
public class MovieReviewMovieReviewFeedbackController {

    @Autowired
    private MovieReviewMovieReviewFeedbackService movieReviewFeedbackService;

    @AdminOrModerator
    @GetMapping
    public List<MovieReviewFeedbackReadDTO> getMovieReviewSpoilerData(
            @PathVariable UUID movieReviewId) {
        return movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(movieReviewId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                                @RequestBody
                                                                @Valid MovieReviewFeedbackCreateDTO createDTO) {
        return movieReviewFeedbackService.createMovieReviewMovieReviewFeedback(movieReviewId, createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                               @PathVariable (value = "id") UUID id,
                                                               @RequestBody MovieReviewFeedbackPatchDTO patch) {
        return movieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReviewId, id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                          @PathVariable (value = "id") UUID id) {
        movieReviewFeedbackService.deleteMovieReviewMovieReviewFeedback(movieReviewId, id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieReviewFeedbackReadDTO putMovieReviewFeedback(@PathVariable UUID movieReviewId,
                                                             @PathVariable (value = "id") UUID id,
                                                             @RequestBody @Valid MovieReviewFeedbackPutDTO put) {
        return movieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReviewId, id, put);
    }
}
