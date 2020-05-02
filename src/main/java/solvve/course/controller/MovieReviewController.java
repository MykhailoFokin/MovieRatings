package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.service.MovieReviewService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviews")
public class MovieReviewController {

    @Autowired
    private MovieReviewService movieReviewService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public MovieReviewReadDTO getMovieReview(@PathVariable UUID id) {
        return movieReviewService.getMovieReview(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieReviewReadDTO createMovieReview(@RequestBody @Valid MovieReviewCreateDTO createDTO) {
        return movieReviewService.createMovieReview(createDTO);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN')")
    @PatchMapping("/{id}")
    public MovieReviewReadDTO patchMovieReview(@PathVariable UUID id, @RequestBody @Valid MovieReviewPatchDTO patch) {
        return movieReviewService.patchMovieReview(id, patch);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteMovieReview(@PathVariable UUID id) {
        movieReviewService.deleteMovieReview(id);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN')")
    @PutMapping("/{id}")
    public MovieReviewReadDTO putMovieReview(@PathVariable UUID id, @RequestBody @Valid MovieReviewPutDTO put) {
        return movieReviewService.updateMovieReview(id, put);
    }
}
