package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.MovieService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PublicAccess
    @GetMapping("/{id}")
    public MovieReadDTO getMovie(@PathVariable UUID id) {
        return movieService.getMovie(id);
    }

    @AdminOrContentManager
    @PostMapping
    public MovieReadDTO createMovie(@RequestBody @Valid MovieCreateDTO createDTO) {
        return movieService.createMovie(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public MovieReadDTO patchMovie(@PathVariable UUID id, @RequestBody @Valid MoviePatchDTO patch) {
        return movieService.patchMovie(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public MovieReadDTO putMovie(@PathVariable UUID id, @RequestBody @Valid MoviePutDTO put) {
        return movieService.updateMovie(id, put);
    }

    @PublicAccess
    @GetMapping("/leader-board")
    public List<MovieInLeaderBoardReadDTO> getMovieLeaderBoard() {
        return movieService.getMoviesLeaderBoard();
    }
}
