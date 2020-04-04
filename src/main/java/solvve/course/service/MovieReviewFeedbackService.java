package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.repository.MovieReviewFeedbackRepository;

import java.util.UUID;

@Service
public class MovieReviewFeedbackService extends AbstractService {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Transactional(readOnly = true)
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(UUID id) {
        MovieReviewFeedback movieReviewFeedback = repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id);
        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = translationService.translate(create, MovieReviewFeedback.class);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(UUID id, MovieReviewFeedbackPatchDTO patch) {
        MovieReviewFeedback movieReviewFeedback = repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id);

        translationService.map(patch, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    public void deleteMovieReviewFeedback(UUID id) {
        movieReviewFeedbackRepository.delete(repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id));
    }

    public MovieReviewFeedbackReadDTO updateMovieReviewFeedback(UUID id, MovieReviewFeedbackPutDTO put) {
        MovieReviewFeedback movieReviewFeedback = repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id);

        translationService.updateEntity(put, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }
}
