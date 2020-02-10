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
public class MovieReviewFeedbackService {

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieReviewService movieReviewService;

    @Transactional(readOnly = true)
    public MovieReviewFeedbackReadDTO getMovieReviewFeedback(UUID id) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewFeedbackRequired(id);
        return translationService.toRead(movieReviewFeedback);
    }

    public MovieReviewFeedbackReadDTO createMovieReviewFeedback(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = translationService.toEntity(create);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    public MovieReviewFeedbackReadDTO patchMovieReviewFeedback(UUID id, MovieReviewFeedbackPatchDTO patch) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewFeedbackRequired(id);

        translationService.patchEntity(patch, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    public void deleteMovieReviewFeedback(UUID id) {
        movieReviewFeedbackRepository.delete(getMovieReviewFeedbackRequired(id));
    }

    public MovieReviewFeedbackReadDTO updateMovieReviewFeedback(UUID id, MovieReviewFeedbackPutDTO put) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewFeedbackRequired(id);

        translationService.updateEntity(put, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    @Transactional(readOnly = true)
    public List<MovieReviewFeedbackReadDTO> getMovieReviewMovieReviewFeedback(UUID movieReviewId) {
        List<MovieReviewFeedback> movieReviewFeedbackList = getMovieReviewMovieReviewFeedbacksRequired(movieReviewId);
        return movieReviewFeedbackList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public MovieReviewFeedbackReadDTO createMovieReviewMovieReviewFeedback(UUID movieReviewId,
                                                                           MovieReviewFeedbackCreateDTO create) {
        MovieReview movieReview = translationService.ReadDTOtoEntity(movieReviewService.getMovieReview(movieReviewId));

        MovieReviewFeedback movieReviewFeedback = translationService.toEntity(create);
        movieReviewFeedback.setMovieReviewId(movieReview);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    public MovieReviewFeedbackReadDTO patchMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id,
                                                                          MovieReviewFeedbackPatchDTO patch) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewMovieReviewFeedbackRequired(movieReviewId, id);

        translationService.patchEntity(patch, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    public void deleteMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id) {
        movieReviewFeedbackRepository.delete(getMovieReviewMovieReviewFeedbackRequired(movieReviewId, id));
    }

    public MovieReviewFeedbackReadDTO updateMovieReviewMovieReviewFeedback(UUID movieReviewId, UUID id,
                                                                        MovieReviewFeedbackPutDTO put) {
        MovieReviewFeedback movieReviewFeedback = getMovieReviewMovieReviewFeedbackRequired(movieReviewId, id);

        translationService.updateEntity(put, movieReviewFeedback);

        movieReviewFeedback = movieReviewFeedbackRepository.save(movieReviewFeedback);
        return translationService.toRead(movieReviewFeedback);
    }

    private MovieReviewFeedback getMovieReviewFeedbackRequired(UUID id) {
        return movieReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, id);
        });
    }

    private MovieReviewFeedback getMovieReviewMovieReviewFeedbackRequired(UUID movieReviewId, UUID id) {
        return movieReviewFeedbackRepository.findByMovieReviewIdAndId(movieReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, movieReviewId, id);
        });
    }

    private List<MovieReviewFeedback> getMovieReviewMovieReviewFeedbacksRequired(UUID movieReviewId) {
        return movieReviewFeedbackRepository.findByMovieReviewIdOrderById(movieReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewFeedback.class, movieReviewId);
        });
    }
}
