package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MovieReviewService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MovieReviewReadDTO getMovieReview(UUID id) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReview.class, id);
        });
        return translationService.toRead(movieReview);
    }

    public MovieReviewReadDTO createMovieReview(MovieReviewCreateDTO create) {
        MovieReview movieReview = translationService.toEntity(create);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.toRead(movieReview);
    }

    public MovieReviewReadDTO patchMovieReview(UUID id, MovieReviewPatchDTO patch) {
        MovieReview movieReview = getMovieReviewRequired(id);

        translationService.patchEntity(patch, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.toRead(movieReview);
    }

    public void deleteMovieReview(UUID id) {
        movieReviewRepository.delete(getMovieReviewRequired(id));
    }

    public MovieReviewReadDTO updateMovieReview(UUID id, MovieReviewPutDTO put) {
        MovieReview movieReview = getMovieReviewRequired(id);

        translationService.updateEntity(put, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.toRead(movieReview);
    }

    private MovieReview getMovieReviewRequired(UUID id) {
        return movieReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReview.class, id);
        });
    }
}
