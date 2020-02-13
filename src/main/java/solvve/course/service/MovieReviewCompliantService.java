package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewCompliantRepository;

import java.util.UUID;

@Service
public class MovieReviewCompliantService {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MovieReviewCompliantReadDTO getMovieReviewCompliant(UUID id) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewCompliantRequired(id);
        return translationService.toRead(movieReviewCompliant);
    }

    public MovieReviewCompliantReadDTO createMovieReviewCompliant(MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = translationService.toEntity(create);

        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);
        return translationService.toRead(movieReviewCompliant);
    }

    public MovieReviewCompliantReadDTO patchMovieReviewCompliant(UUID id, MovieReviewCompliantPatchDTO patch) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewCompliantRequired(id);

        translationService.patchEntity(patch, movieReviewCompliant);

        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);
        return translationService.toRead(movieReviewCompliant);
    }

    public void deleteMovieReviewCompliant(UUID id) {
        movieReviewCompliantRepository.delete(getMovieReviewCompliantRequired(id));
    }

    public MovieReviewCompliantReadDTO updateMovieReviewCompliant(UUID id, MovieReviewCompliantPutDTO put) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewCompliantRequired(id);

        translationService.updateEntity(put, movieReviewCompliant);

        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);
        return translationService.toRead(movieReviewCompliant);
    }

    private MovieReviewCompliant getMovieReviewCompliantRequired(UUID id) {
        return movieReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, id);
        });
    }
}
