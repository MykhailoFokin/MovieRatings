package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieSpoilerDataRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieReviewSpoilerDataService extends AbstractService {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    public List<MovieSpoilerDataReadDTO> getMovieReviewSpoilerDatas(UUID movieReviewId) {
        List<MovieSpoilerData> movieSpoilerData =
                movieSpoilerDataRepository.findByMovieReviewIdOrderByIdAsc(movieReviewId).orElseThrow(() -> {
                    throw new EntityNotFoundException(MovieSpoilerData.class, movieReviewId);
                });

        return movieSpoilerData.stream().map(e -> translationService.translate(e, MovieSpoilerDataReadDTO.class))
                .collect(Collectors.toList());
    }

    public MovieSpoilerDataReadDTO createMovieReviewSpoilerData(UUID movieReviewId, MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = translationService.translate(create, MovieSpoilerData.class);
        movieSpoilerData.setMovieReview(repositoryHelper.getReferenceIfExists(MovieReview.class, movieReviewId));
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }

    public MovieSpoilerDataReadDTO patchMovieReviewSpoilerData(UUID movieReviewId,
                                                               UUID id,
                                                               MovieSpoilerDataPatchDTO patch) {
        MovieSpoilerData movieSpoilerData = repositoryHelper.getByIdRequired(MovieSpoilerData.class, id);
        translationService.map(patch, movieSpoilerData);
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }

    public void deleteMovieReviewSpoilerData(UUID movieReviewId, UUID id) {
        movieSpoilerDataRepository.delete(repositoryHelper.getByIdRequired(MovieSpoilerData.class, id));
    }

    public MovieSpoilerDataReadDTO updateMovieReviewSpoilerData(UUID movieReviewId, UUID id,
                                                                MovieSpoilerDataPutDTO put) {
        MovieSpoilerData movieSpoilerData = repositoryHelper.getByIdRequired(MovieSpoilerData.class, id);
        translationService.updateEntity(put, movieSpoilerData);
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }
}
