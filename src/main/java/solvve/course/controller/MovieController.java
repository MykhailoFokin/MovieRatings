package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieCreateDTO;
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

    @PostMapping
    public MovieReadDTO createMovie(@RequestBody MovieCreateDTO createDTO){
        return movieService.createMovie(createDTO);
    }
}
