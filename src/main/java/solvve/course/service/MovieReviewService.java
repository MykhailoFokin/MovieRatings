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

import java.util.UUID;

@Service
public class MovieReviewService extends AbstractService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Transactional(readOnly = true)
    public MovieReviewReadDTO getMovieReview(UUID id) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReview.class, id);
        });
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    public MovieReviewReadDTO createMovieReview(MovieReviewCreateDTO create) {
        MovieReview movieReview = translationService.translate(create, MovieReview.class);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    public MovieReviewReadDTO patchMovieReview(UUID id, MovieReviewPatchDTO patch) {
        MovieReview movieReview = repositoryHelper.getByIdRequired(MovieReview.class, id);

        translationService.map(patch, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    public void deleteMovieReview(UUID id) {
        movieReviewRepository.delete(repositoryHelper.getByIdRequired(MovieReview.class, id));
    }

    public MovieReviewReadDTO updateMovieReview(UUID id, MovieReviewPutDTO put) {
        MovieReview movieReview = repositoryHelper.getByIdRequired(MovieReview.class, id);

        translationService.updateEntity(put, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }
}
