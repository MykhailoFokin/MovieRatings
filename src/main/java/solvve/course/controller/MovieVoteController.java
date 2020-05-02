package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVotePatchDTO;
import solvve.course.dto.MovieVotePutDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.service.MovieVoteService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movievotes")
public class MovieVoteController {

    @Autowired
    private MovieVoteService movieVoteService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public MovieVoteReadDTO getMovieVote(@PathVariable UUID id) {
        return movieVoteService.getMovieVote(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public MovieVoteReadDTO createMovieVote(@RequestBody @Valid MovieVoteCreateDTO createDTO) {
        return movieVoteService.createMovieVote(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieVoteReadDTO patchMovieVote(@PathVariable UUID id, @RequestBody MovieVotePatchDTO patch) {
        return movieVoteService.patchMovieVote(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieVote(@PathVariable UUID id) {
        movieVoteService.deleteMovieVote(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public MovieVoteReadDTO putMovieVote(@PathVariable UUID id, @RequestBody @Valid MovieVotePutDTO put) {
        return movieVoteService.updateMovieVote(id, put);
    }
}
