package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.service.MovieReviewMovieReviewCompliantService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants")
public class MovieReviewMovieReviewCompliantController {

    @Autowired
    private MovieReviewMovieReviewCompliantService movieReviewCompliantService;

    @AdminOrModerator
    @GetMapping
    public List<MovieReviewCompliantReadDTO> getMovieReviewCompliants(
            @PathVariable UUID movieReviewId) {
        return movieReviewCompliantService.getMovieReviewMovieReviewCompliant(movieReviewId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieReviewCompliantReadDTO createMovieReviewCompliant(
            @PathVariable UUID movieReviewId,
            @RequestBody @Valid MovieReviewCompliantCreateDTO createDTO) {
        return movieReviewCompliantService.createMovieReviewMovieReviewCompliant(movieReviewId, createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieReviewCompliantReadDTO patchMovieReviewCompliant(
            @PathVariable UUID movieReviewId,
            @PathVariable (value = "id") UUID id,
            @RequestBody @Valid MovieReviewCompliantPatchDTO patch) {
        return movieReviewCompliantService.patchMovieReviewMovieReviewCompliant(movieReviewId, id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieReviewCompliant(@PathVariable UUID movieReviewId,
                                           @PathVariable (value = "id") UUID id) {
        movieReviewCompliantService.deleteMovieReviewMovieReviewCompliant(movieReviewId, id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieReviewCompliantReadDTO putMovieReviewCompliant(
            @PathVariable UUID movieReviewId,
            @PathVariable (value = "id") UUID id,
            @RequestBody @Valid MovieReviewCompliantPutDTO put) {
        return movieReviewCompliantService.updateMovieReviewMovieReviewCompliant(movieReviewId, id, put);
    }
}
