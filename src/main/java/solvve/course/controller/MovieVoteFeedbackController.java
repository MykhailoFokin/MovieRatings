package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieVoteFeedbackCreateDTO;
import solvve.course.dto.MovieVoteFeedbackReadDTO;
import solvve.course.service.MovieVoteFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movievotefeedback")
public class MovieVoteFeedbackController {

    @Autowired
    private MovieVoteFeedbackService movieVoteFeedbackService;

    @GetMapping("/{id}")
    public MovieVoteFeedbackReadDTO getMovieVoteFeedback(@PathVariable UUID id) {
        return movieVoteFeedbackService.getMovieVoteFeedback(id);
    }

    @PostMapping
    public MovieVoteFeedbackReadDTO createMovieVoteFeedback(@RequestBody MovieVoteFeedbackCreateDTO createDTO){
        return movieVoteFeedbackService.createMovieVoteFeedback(createDTO);
    }
}
