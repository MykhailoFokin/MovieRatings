package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.controller.validation.ControllerValidationUtil;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.service.MovieReviewSpoilerDataService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas")
public class MovieReviewSpoilerDataController {

    @Autowired
    private MovieReviewSpoilerDataService movieSpoilerDataService;

    @AdminOrModerator
    @GetMapping
    public List<MovieSpoilerDataReadDTO> getMovieReviewSpoilerData(
            @PathVariable UUID movieReviewId) {
        return movieSpoilerDataService.getMovieReviewSpoilerDatas(movieReviewId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieSpoilerDataReadDTO createMovieSpoilerData(@PathVariable UUID movieReviewId,
                                                          @RequestBody @Valid MovieSpoilerDataCreateDTO createDTO) {
        ControllerValidationUtil.validateLessThan(createDTO.getStartIndex(), createDTO.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.createMovieReviewSpoilerData(movieReviewId, createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieSpoilerDataReadDTO patchMovieSpoilerData(@PathVariable UUID movieReviewId,
                                                         @PathVariable (value = "id") UUID id,
                                                         @RequestBody MovieSpoilerDataPatchDTO patch) {
        ControllerValidationUtil.validateLessThan(patch.getStartIndex(), patch.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.patchMovieReviewSpoilerData(movieReviewId, id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieSpoilerData(@PathVariable UUID movieReviewId,
                                       @PathVariable (value = "id") UUID id) {
        movieSpoilerDataService.deleteMovieReviewSpoilerData(movieReviewId, id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieSpoilerDataReadDTO putMovieSpoilerData(@PathVariable UUID movieReviewId,
                                                       @PathVariable (value = "id") UUID id,
                                                       @RequestBody @Valid MovieSpoilerDataPutDTO put) {
        ControllerValidationUtil.validateLessThan(put.getStartIndex(), put.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.updateMovieReviewSpoilerData(movieReviewId, id, put);
    }
}
