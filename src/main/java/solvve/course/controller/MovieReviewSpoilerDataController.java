package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.service.MovieReviewSpoilerDataService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas")
public class MovieReviewSpoilerDataController {

    @Autowired
    private MovieReviewSpoilerDataService movieSpoilerDataService;

    @GetMapping
    public List<MovieSpoilerDataReadDTO> getMovieReviewSpoilerData(
            @PathVariable (value = "moviereviewid") UUID movieReviewId) {
        return movieSpoilerDataService.getMovieReviewSpoilerDatas(movieReviewId);
    }

    @PostMapping
    public MovieSpoilerDataReadDTO createMovieSpoilerData(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                          @RequestBody MovieSpoilerDataCreateDTO createDTO) {
        return movieSpoilerDataService.createMovieReviewSpoilerData(movieReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public MovieSpoilerDataReadDTO patchMovieSpoilerData(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                         @PathVariable (value = "id") UUID id,
                                                         @RequestBody MovieSpoilerDataPatchDTO patch) {
        return movieSpoilerDataService.patchMovieReviewSpoilerData(movieReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieSpoilerData(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                       @PathVariable (value = "id") UUID id) {
        movieSpoilerDataService.deleteMovieReviewSpoilerData(movieReviewId, id);
    }

    @PutMapping("/{id}")
    public MovieSpoilerDataReadDTO putMovieSpoilerData(@PathVariable (value = "moviereviewid") UUID movieReviewId,
                                                       @PathVariable (value = "id") UUID id,
                                                       @RequestBody MovieSpoilerDataPutDTO put) {
        return movieSpoilerDataService.updateMovieReviewSpoilerData(movieReviewId, id, put);
    }
}
