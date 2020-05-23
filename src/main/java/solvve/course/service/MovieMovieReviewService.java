package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.repository.MovieReviewRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MovieMovieReviewService extends AbstractService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Transactional(readOnly = true)
    public List<MovieReviewReadDTO> getMovieReviews(UUID movieId) {
        List<MovieReview> movieReviews = movieReviewRepository
                .findByMovieIdAndModeratedStatus(movieId, UserModeratedStatusType.SUCCESS);
        return translationService.translateList(movieReviews, MovieReviewReadDTO.class);
    }

    @Transactional(readOnly = true)
    public List<MovieReviewReadDTO> getUnModeratedMovieReviews(UUID movieId) {
        List<MovieReview> movieReviews = movieReviewRepository
                .findByMovieIdAndModeratedStatus(movieId, UserModeratedStatusType.CREATED);
        return translationService.translateList(movieReviews, MovieReviewReadDTO.class);
    }
}
