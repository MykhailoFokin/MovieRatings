package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.MovieCompanyService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviecompanies")
public class MovieCompanyController {

    @Autowired
    private MovieCompanyService movieCompanyService;

    @GetMapping("/{id}")
    public MovieCompanyReadDTO getMovieCompany(@PathVariable UUID id) {
        return movieCompanyService.getMovieCompany(id);
    }

    @PostMapping
    public MovieCompanyReadDTO createMovieCompany(@RequestBody @Valid MovieCompanyCreateDTO createDTO) {
        return movieCompanyService.createMovieCompany(createDTO);
    }

    @PatchMapping("/{id}")
    public MovieCompanyReadDTO patchMovieCompany(@PathVariable UUID id,
                                                 @RequestBody @Valid MovieCompanyPatchDTO patch) {
        return movieCompanyService.patchMovieCompany(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieCompany(@PathVariable UUID id) {
        movieCompanyService.deleteMovieCompany(id);
    }

    @PutMapping("/{id}")
    public MovieCompanyReadDTO putMovieCompany(@PathVariable UUID id, @RequestBody @Valid MovieCompanyPutDTO put) {
        return movieCompanyService.updateMovieCompany(id, put);
    }

    @GetMapping
    public PageResult<MovieCompanyReadDTO> getMovieCompanies(MovieCompanyFilter filter, Pageable pageable) {
        return movieCompanyService.getMovieCompanies(filter, pageable);
    }
}
