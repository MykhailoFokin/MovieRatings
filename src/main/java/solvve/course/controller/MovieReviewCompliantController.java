package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.service.MovieReviewCompliantService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviewcompliants")
public class MovieReviewCompliantController {

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public MovieReviewCompliantReadDTO getMovieReviewCompliant(@PathVariable UUID id) {
        return movieReviewCompliantService.getMovieReviewCompliant(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping
    public MovieReviewCompliantReadDTO createMovieReviewCompliant(
            @RequestBody @Valid MovieReviewCompliantCreateDTO createDTO) {
        return movieReviewCompliantService.createMovieReviewCompliant(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieReviewCompliantReadDTO patchMovieReviewCompliant(@PathVariable UUID id,
                                                                 @RequestBody
                                                                 @Valid MovieReviewCompliantPatchDTO patch) {
        return movieReviewCompliantService.patchMovieReviewCompliant(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieReviewCompliant(@PathVariable UUID id) {
        movieReviewCompliantService.deleteMovieReviewCompliant(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieReviewCompliantReadDTO putMovieReviewCompliant(@PathVariable UUID id,
                                                               @RequestBody @Valid MovieReviewCompliantPutDTO put) {
        return movieReviewCompliantService.updateMovieReviewCompliant(id, put);
    }
}
