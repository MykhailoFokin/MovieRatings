package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewFeedbackRepository;

import java.util.UUID;

@Service
public class MovieReviewFeedbackService {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Transactional(readOnly = true)
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(UUID id) {
        MovieReviewFeedback movieReviewFeedback = movieReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, id);
        });
        return toRead(movieReviewFeedback);
    }

    private MovieReviewFeedbackReadDTO toRead(MovieReviewFeedback movieReviewFeedback) {
        MovieReviewFeedbackReadDTO dto = new MovieReviewFeedbackReadDTO();
        dto.setId(movieReviewFeedback.getId());
        dto.setUserId(movieReviewFeedback.getUserId());
        dto.setMovieId(movieReviewFeedback.getMovieId());
        dto.setMovieReviewId(movieReviewFeedback.getMovieReviewId());
        dto.setIsLiked(movieReviewFeedback.getIsLiked());
        return dto;
    }

    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(create.getUserId());
        movieReviewFeedback.setMovieId(create.getMovieId());
        movieReviewFeedback.setMovieReviewId(create.getMovieReviewId());
        movieReviewFeedback.setIsLiked(create.getIsLiked());

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return toRead(movieReviewFeedback);
    }
}
