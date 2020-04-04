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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieReviewMovieReviewCompliantService extends AbstractService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Transactional(readOnly = true)
    public List<MovieReviewCompliantReadDTO> getMovieReviewMovieReviewCompliant(UUID movieReviewId) {
        List<MovieReviewCompliant> movieReviewCompliants =
                getMovieReviewMovieReviewCompliantsRequired(movieReviewId);
        return movieReviewCompliants.stream().map(e ->
                translationService.translate(e, MovieReviewCompliantReadDTO.class)).collect(Collectors.toList());
    }

    public MovieReviewCompliantReadDTO createMovieReviewMovieReviewCompliant(UUID movieReviewId,
                                                                             MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = translationService.translate(create, MovieReviewCompliant.class);
        movieReviewCompliant.setMovieReview(repositoryHelper.getReferenceIfExists(MovieReview.class, movieReviewId));
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.translate(movieReviewCompliant, MovieReviewCompliantReadDTO.class);
    }

    public MovieReviewCompliantReadDTO patchMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id,
                                                                            MovieReviewCompliantPatchDTO patch) {
        MovieReviewCompliant movieReviewCompliant = repositoryHelper.getByIdRequired(MovieReviewCompliant.class, id);

        translationService.map(patch, movieReviewCompliant);
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.translate(movieReviewCompliant, MovieReviewCompliantReadDTO.class);
    }

    public void deleteMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id) {
        movieReviewCompliantRepository.delete(repositoryHelper.getByIdRequired(MovieReviewCompliant.class, id));
    }

    public MovieReviewCompliantReadDTO updateMovieReviewMovieReviewCompliant(UUID movieReviewId, UUID id,
                                                                             MovieReviewCompliantPutDTO put) {
        MovieReviewCompliant movieReviewCompliant = repositoryHelper.getByIdRequired(MovieReviewCompliant.class, id);
        translationService.updateEntity(put, movieReviewCompliant);
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.translate(movieReviewCompliant, MovieReviewCompliantReadDTO.class);
    }

    private List<MovieReviewCompliant> getMovieReviewMovieReviewCompliantsRequired(UUID movieReviewId) {
        return movieReviewCompliantRepository.findByMovieReviewIdOrderById(movieReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, movieReviewId);
        });
    }
}
