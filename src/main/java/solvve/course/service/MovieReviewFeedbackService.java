package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
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
        MovieReviewFeedback movieReviewFeedback = getMovieReviewFeedbackRequired(id);
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

    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(UUID id, MovieReviewFeedbackPatchDTO patch) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewFeedbackRequired(id);

        if (patch.getMovieId()!=null) {
            movieReviewFeedback.setMovieId(patch.getMovieId());
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewFeedback.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getUserId()!=null) {
            movieReviewFeedback.setUserId(patch.getUserId());
        }
        if (patch.getIsLiked()!=null) {
            movieReviewFeedback.setIsLiked(patch.getIsLiked());
        }
        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return toRead(movieReviewFeedback);
    }

    private MovieReviewFeedback getMovieReviewFeedbackRequired(UUID id) {
        return movieReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, id);
        });
    }

    public void deleteMovieReviewFeedback(UUID id) {
        movieReviewFeedbackRepository.delete(getMovieReviewFeedbackRequired(id));
    }
}
