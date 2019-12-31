package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieVoteCompliantCreateDTO;
import solvve.course.dto.MovieVoteCompliantReadDTO;
import solvve.course.service.MovieVoteCompliantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movievotecompliant")
public class MovieVoteCompliantController {

    @Autowired
    private MovieVoteCompliantService movieVoteCompliantService;

    @GetMapping("/{id}")
    public MovieVoteCompliantReadDTO getMovieVoteCompliant(@PathVariable UUID id) {
        return movieVoteCompliantService.getMovieVoteCompliant(id);
    }

    @PostMapping
    public MovieVoteCompliantReadDTO createMovieVoteCompliant(@RequestBody MovieVoteCompliantCreateDTO createDTO){
        return movieVoteCompliantService.createMovieVoteCompliant(createDTO);
    }
}
