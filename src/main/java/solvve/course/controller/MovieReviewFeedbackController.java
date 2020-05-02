package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.service.MovieReviewFeedbackService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviewfeedbacks")
public class MovieReviewFeedbackController {

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(@PathVariable UUID id) {
        return movieReviewFeedbackService.getMovieReviewFeedback(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(@RequestBody
                                                                    @Valid MovieReviewFeedbackCreateDTO createDTO) {
        return movieReviewFeedbackService.createMovieReviewFeedback(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(@PathVariable UUID id,
                                                               @RequestBody MovieReviewFeedbackPatchDTO patch) {
        return movieReviewFeedbackService.patchMovieReviewFeedback(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieReviewFeedback(@PathVariable UUID id) {
        movieReviewFeedbackService.deleteMovieReviewFeedback(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieReviewFeedbackReadDTO putMovieReviewFeedback(@PathVariable UUID id,
                                                             @RequestBody @Valid MovieReviewFeedbackPutDTO put) {
        return movieReviewFeedbackService.updateMovieReviewFeedback(id, put);
    }
}
