package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.MovieCompanyService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviecompanies")
public class MovieCompanyController {

    @Autowired
    private MovieCompanyService movieCompanyService;

    @PublicAccess
    @GetMapping("/{id}")
    public MovieCompanyReadDTO getMovieCompany(@PathVariable UUID id) {
        return movieCompanyService.getMovieCompany(id);
    }

    @AdminOrContentManager
    @PostMapping
    public MovieCompanyReadDTO createMovieCompany(@RequestBody @Valid MovieCompanyCreateDTO createDTO) {
        return movieCompanyService.createMovieCompany(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public MovieCompanyReadDTO patchMovieCompany(@PathVariable UUID id,
                                                 @RequestBody @Valid MovieCompanyPatchDTO patch) {
        return movieCompanyService.patchMovieCompany(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteMovieCompany(@PathVariable UUID id) {
        movieCompanyService.deleteMovieCompany(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public MovieCompanyReadDTO putMovieCompany(@PathVariable UUID id, @RequestBody @Valid MovieCompanyPutDTO put) {
        return movieCompanyService.updateMovieCompany(id, put);
    }

    @ApiPageable
    @PublicAccess
    @GetMapping
    public PageResult<MovieCompanyReadDTO> getMovieCompanies(MovieCompanyFilter filter, @ApiIgnore Pageable pageable) {
        return movieCompanyService.getMovieCompanies(filter, pageable);
    }
}
