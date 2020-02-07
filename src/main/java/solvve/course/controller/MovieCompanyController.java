package solvve.course.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.MovieCompanyService;

import java.util.List;
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
    public MovieCompanyReadDTO createMovieCompany(@RequestBody MovieCompanyCreateDTO createDTO) {
        return movieCompanyService.createMovieCompany(createDTO);
    }

    @PatchMapping("/{id}")
    public MovieCompanyReadDTO patchMovieCompany(@PathVariable UUID id, @RequestBody MovieCompanyPatchDTO patch) {
        return movieCompanyService.patchMovieCompany(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieCompany(@PathVariable UUID id) {
        movieCompanyService.deleteMovieCompany(id);
    }

    @PutMapping("/{id}")
    public MovieCompanyReadDTO putMovieCompany(@PathVariable UUID id, @RequestBody MovieCompanyPutDTO put) {
        return movieCompanyService.putMovieCompany(id, put);
    }

    @GetMapping
    public List<MovieCompanyReadDTO> getMovieCompanies(MovieCompanyFilter filter) {
        return movieCompanyService.getMovieCompanies(filter);
    }
}
