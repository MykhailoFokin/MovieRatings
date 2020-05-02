package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.MovieLanguageReadDTO;
import solvve.course.service.MovieLanguageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MovieLanguageController {

    @Autowired
    private MovieLanguageService movieLanguageService;

    @PublicAccess
    @GetMapping("/movies/{movieId}/languages")
    public List<MovieLanguageReadDTO> getMovieLanguages(@PathVariable UUID movieId) {
        return movieLanguageService.getMovieLanguages(movieId);
    }

    @AdminOrContentManager
    @PostMapping("/movies/{movieId}/languages/{id}")
    public List<MovieLanguageReadDTO> addLanguageToMovie(@PathVariable UUID movieId,
                                                         @PathVariable UUID id) {
        return movieLanguageService.addLanguageToMovie(movieId, id);
    }

    @AdminOrContentManager
    @DeleteMapping("/movies/{movieId}/languages/{id}")
    public List<MovieLanguageReadDTO> deleteMovie(@PathVariable UUID movieId, @PathVariable UUID id) {
        return movieLanguageService.removeLanguageFromMovie(movieId, id);
    }
}
