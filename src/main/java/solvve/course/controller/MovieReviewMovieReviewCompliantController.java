package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.service.MovieReviewCompliantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviews/{moviereviewid}/moviereviewcompliants")
public class MovieReviewMovieReviewCompliantController {

    @Autowired
    private MovieReviewCompliantService movieReviewCompliantService;

    @GetMapping
    public List<MovieReviewCompliantReadDTO> getMovieReviewSpoilerData(
            @PathVariable(value = "moviereviewid") UUID movieReviewId) {
        return movieReviewCompliantService.getMovieReviewMovieReviewCompliant(movieReviewId);
    }

    @PostMapping
    public MovieReviewCompliantReadDTO createMovieReviewCompliant(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                                  @RequestBody MovieReviewCompliantCreateDTO createDTO) {
        return movieReviewCompliantService.createMovieReviewMovieReviewCompliant(movieReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public MovieReviewCompliantReadDTO patchMovieReviewCompliant(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                                 @PathVariable (value = "id") UUID id,
                                                                 @RequestBody MovieReviewCompliantPatchDTO patch) {
        return movieReviewCompliantService.patchMovieReviewMovieReviewCompliant(movieReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieReviewCompliant(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                           @PathVariable (value = "id") UUID id) {
        movieReviewCompliantService.deleteMovieReviewMovieReviewCompliant(movieReviewId, id);
    }

    @PutMapping("/{id}")
    public MovieReviewCompliantReadDTO putMovieReviewCompliant(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                               @PathVariable (value = "id") UUID id,
                                                               @RequestBody MovieReviewCompliantPutDTO put) {
        return movieReviewCompliantService.putMovieReviewMovieReviewCompliant(movieReviewId, id, put);
    }
}
