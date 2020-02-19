package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewCompliantRepository;
import solvve.course.repository.RepositoryHelper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieReviewMovieReviewCompliantService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Transactional(readOnly = true)
    public List<MovieReviewCompliantReadDTO> getMovieReviewMovieReviewCompliant(UUID movieReviewId) {
        List<MovieReviewCompliant> movieReviewCompliantList =
                getMovieReviewMovieReviewCompliantsRequired(movieReviewId);
        return movieReviewCompliantList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public MovieReviewCompliantReadDTO createMovieReviewMovieReviewCompliant(UUID movieReviewId,
                                                                             MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = translationService.toEntity(create);
        movieReviewCompliant.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, movieReviewId));
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.toRead(movieReviewCompliant);
    }

    public MovieReviewCompliantReadDTO patchMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id,
                                                                            MovieReviewCompliantPatchDTO patch) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewMovieReviewCompliantRequired(movieReviewId, id);

        translationService.patchEntity(patch, movieReviewCompliant);
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.toRead(movieReviewCompliant);
    }

    public void deleteMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id) {
        movieReviewCompliantRepository.delete(getMovieReviewMovieReviewCompliantRequired(movieReviewId, id));
    }

    public MovieReviewCompliantReadDTO updateMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id,
                                                                             MovieReviewCompliantPutDTO put) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewMovieReviewCompliantRequired(movieReviewId, id);
        translationService.updateEntity(put, movieReviewCompliant);
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.toRead(movieReviewCompliant);
    }

    private MovieReviewCompliant getMovieReviewMovieReviewCompliantRequired(UUID movieReviewId, UUID id) {
        return movieReviewCompliantRepository.findByMovieReviewIdAndId(movieReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, movieReviewId, id);
        });
    }

    private List<MovieReviewCompliant> getMovieReviewMovieReviewCompliantsRequired(UUID movieReviewId) {
        return movieReviewCompliantRepository.findByMovieReviewIdOrderById(movieReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, movieReviewId);
        });
    }
}
