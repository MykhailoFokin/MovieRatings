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
import solvve.course.service.MovieSpoilerDataService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviespoilerdata")
public class MovieSpoilerDataController {

    @Autowired
    private MovieSpoilerDataService movieSpoilerDataService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public MovieSpoilerDataReadDTO getMovieSpoilerData(@PathVariable UUID id) {
        return movieSpoilerDataService.getMovieSpoilerData(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieSpoilerDataReadDTO createMovieSpoilerData(@RequestBody @Valid MovieSpoilerDataCreateDTO createDTO) {
        ControllerValidationUtil.validateLessThan(createDTO.getStartIndex(), createDTO.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.createMovieSpoilerData(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieSpoilerDataReadDTO patchMovieSpoilerData(@PathVariable UUID id,
                                                         @RequestBody MovieSpoilerDataPatchDTO patch) {
        ControllerValidationUtil.validateLessThan(patch.getStartIndex(), patch.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.patchMovieSpoilerData(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieSpoilerData(@PathVariable UUID id) {
        movieSpoilerDataService.deleteMovieSpoilerData(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieSpoilerDataReadDTO putMovieSpoilerData(@PathVariable UUID id,
                                                       @RequestBody @Valid MovieSpoilerDataPutDTO put) {
        ControllerValidationUtil.validateLessThan(put.getStartIndex(), put.getEndIndex(),
                "startIndex", "endIndex");
        return movieSpoilerDataService.updateMovieSpoilerData(id, put);
    }
}
