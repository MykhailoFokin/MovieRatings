package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.MovieMovieCompanyReadDTO;
import solvve.course.service.MovieMovieCompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MovieMovieCompanyController {

    @Autowired
    private MovieMovieCompanyService movieMovieCompanyService;

    @PublicAccess
    @GetMapping("/movies/{movieId}/movie-companies")
    public List<MovieMovieCompanyReadDTO> getMovieCompanies(@PathVariable UUID movieId) {
        return movieMovieCompanyService.getMovieCompanies(movieId);
    }

    @AdminOrContentManager
    @PostMapping("/movies/{movieId}/movie-companies/{id}")
    public List<MovieMovieCompanyReadDTO> addMovieCompanyToMovie(@PathVariable UUID movieId,
                                                       @PathVariable UUID id) {
        return movieMovieCompanyService.addMovieCompanyToMovie(movieId, id);
    }

    @AdminOrContentManager
    @DeleteMapping("/movies/{movieId}/movie-companies/{id}")
    public List<MovieMovieCompanyReadDTO> deleteMovie(@PathVariable UUID movieId, @PathVariable UUID id) {
        return movieMovieCompanyService.removeMovieCompanyFromMovie(movieId, id);
    }
}
