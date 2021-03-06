package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewFeedbackRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieReviewMovieReviewFeedbackService extends AbstractService {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Transactional(readOnly = true)
    public List<MovieReviewFeedbackReadDTO> getMovieReviewMovieReviewFeedback(UUID movieReviewId) {
        List<MovieReviewFeedback> movieReviewFeedbacks = getMovieReviewMovieReviewFeedbacksRequired(movieReviewId);
        return movieReviewFeedbacks.stream().map(e ->
                translationService.translate(e, MovieReviewFeedbackReadDTO.class)).collect(Collectors.toList());
    }

    public MovieReviewFeedbackReadDTO createMovieReviewMovieReviewFeedback(UUID movieReviewId,
                                                                           MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = translationService.translate(create, MovieReviewFeedback.class);
        movieReviewFeedback.setMovieReview(repositoryHelper.getReferenceIfExists(MovieReview.class, movieReviewId));
        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);

        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    public MovieReviewFeedbackReadDTO patchMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id,
                                                                          MovieReviewFeedbackPatchDTO patch) {
        MovieReviewFeedback movieReviewFeedback = repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id);
        translationService.map(patch, movieReviewFeedback);
        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);

        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    public void deleteMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id) {
        movieReviewFeedbackRepository.delete(repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id));
    }

    public MovieReviewFeedbackReadDTO updateMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id,
                                                                           MovieReviewFeedbackPutDTO put) {
        MovieReviewFeedback movieReviewFeedback = repositoryHelper.getByIdRequired(MovieReviewFeedback.class, id);
        translationService.updateEntity(put, movieReviewFeedback);
        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);

        return translationService.translate(movieReviewFeedback, MovieReviewFeedbackReadDTO.class);
    }

    private List<MovieReviewFeedback> getMovieReviewMovieReviewFeedbacksRequired(UUID movieReviewId) {
        return movieReviewFeedbackRepository.findByMovieReviewIdOrderById(movieReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, movieReviewId);
        });
    }
}
