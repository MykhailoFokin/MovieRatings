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
import solvve.course.repository.RepositoryHelper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieReviewSpoilerDataService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public List<MovieSpoilerDataReadDTO> getMovieReviewSpoilerDatas(UUID movieReviewId) {
        List<MovieSpoilerData> movieSpoilerDataList =
                movieSpoilerDataRepository.findByMovieReviewIdOrderByIdAsc(movieReviewId).orElseThrow(() -> {
                    throw new EntityNotFoundException(MovieSpoilerData.class, movieReviewId);
                });

        return movieSpoilerDataList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public MovieSpoilerDataReadDTO createMovieReviewSpoilerData(UUID movieReviewId, MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = translationService.toEntity(create);
        movieSpoilerData.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, movieReviewId));
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.toRead(movieSpoilerData);
    }

    public MovieSpoilerDataReadDTO patchMovieReviewSpoilerData(UUID movieReviewId,
                                                               UUID id,
                                                               MovieSpoilerDataPatchDTO patch) {
        MovieSpoilerData movieSpoilerData = getMovieReviewSpoilerDataRequired(movieReviewId, id);
        translationService.patchEntity(patch, movieSpoilerData);
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.toRead(movieSpoilerData);
    }

    public void deleteMovieReviewSpoilerData(UUID movieReviewId, UUID id) {
        movieSpoilerDataRepository.delete(getMovieReviewSpoilerDataRequired(movieReviewId, id));
    }

    public MovieSpoilerDataReadDTO updateMovieReviewSpoilerData(UUID movieReviewId, UUID id,
                                                                MovieSpoilerDataPutDTO put) {
        MovieSpoilerData movieSpoilerData = getMovieReviewSpoilerDataRequired(movieReviewId, id);
        translationService.updateEntity(put, movieSpoilerData);
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);

        return translationService.toRead(movieSpoilerData);
    }

    private MovieSpoilerData getMovieReviewSpoilerDataRequired(UUID movieReviewId, UUID id) {
        return movieSpoilerDataRepository.findByMovieReviewIdAndId(movieReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieSpoilerData.class, movieReviewId, id);
        });
    }
}
