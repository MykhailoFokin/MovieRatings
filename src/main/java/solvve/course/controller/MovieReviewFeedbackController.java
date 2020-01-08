package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.service.MovieReviewFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moviereviewfeedback")
public class MovieReviewFeedbackController {

    @Autowired
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @GetMapping("/{id}")
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(@PathVariable UUID id) {
        return movieReviewFeedbackService.getMovieReviewFeedback(id);
    }

    @PostMapping
    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(@RequestBody MovieReviewFeedbackCreateDTO createDTO){
        return movieReviewFeedbackService.createMovieReviewFeedback(createDTO);
    }
}
