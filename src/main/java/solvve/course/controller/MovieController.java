package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.dto.MovieReadDTO;
import solvve.course.service.MovieService;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public MovieReadDTO getMovie(@PathVariable UUID id) {
        return movieService.getMovie(id);
    }
}
