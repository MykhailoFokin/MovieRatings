package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.service.MovieVoteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movievote")
public class MovieVoteController {

    @Autowired
    private MovieVoteService movieVoteService;

    @GetMapping("/{id}")
    public MovieVoteReadDTO getMovieVote(@PathVariable UUID id) {
        return movieVoteService.getMovieVote(id);
    }

    @PostMapping
    public MovieVoteReadDTO createMovieVote(@RequestBody MovieVoteCreateDTO createDTO){
        return movieVoteService.createMovieVote(createDTO);
    }
}
