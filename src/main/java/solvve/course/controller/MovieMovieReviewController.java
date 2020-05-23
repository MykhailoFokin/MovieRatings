package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.service.MovieMovieReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies/{movieId}/movie-reviews")
public class MovieMovieReviewController {

    @Autowired
    private MovieMovieReviewService movieMovieReviewService;

    @PublicAccess
    @GetMapping
    public List<MovieReviewReadDTO> getMovieReviews(@PathVariable UUID movieId) {
        return movieMovieReviewService.getMovieReviews(movieId);
    }

    @AdminOrModerator
    @GetMapping("/unmoderated")
    public List<MovieReviewReadDTO> getUnModeratedMovieReviews(@PathVariable UUID movieId) {
        return movieMovieReviewService.getUnModeratedMovieReviews(movieId);
    }
}
