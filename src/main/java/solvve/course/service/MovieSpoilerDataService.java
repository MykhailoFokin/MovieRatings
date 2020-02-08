package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class MovieSpoilerDataService {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieReviewService movieReviewService;

    @Transactional(readOnly = true)
    public MovieSpoilerDataReadDTO getMovieSpoilerData(UUID id) {
        MovieSpoilerData movieSpoilerData = getMovieSpoilerDataRequired(id);
        return translationService.toRead(movieSpoilerData);
    }

    public MovieSpoilerDataReadDTO createMovieSpoilerData(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = translationService.toEntity(create);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.toRead(movieSpoilerData);
    }

    public MovieSpoilerDataReadDTO patchMovieSpoilerData(UUID id, MovieSpoilerDataPatchDTO patch) {
        MovieSpoilerData movieSpoilerData = getMovieSpoilerDataRequired(id);

        translationService.patchEntity(patch, movieSpoilerData);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.toRead(movieSpoilerData);
    }

    public void deleteMovieSpoilerData(UUID id) {
        movieSpoilerDataRepository.delete(getMovieSpoilerDataRequired(id));
    }

    public MovieSpoilerDataReadDTO putMovieSpoilerData(UUID id, MovieSpoilerDataPutDTO put) {
        MovieSpoilerData movieSpoilerData = getMovieSpoilerDataRequired(id);

        translationService.putEntity(put, movieSpoilerData);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.toRead(movieSpoilerData);
    }

    public List<MovieSpoilerDataReadDTO> getMovieReviewSpoilerDatas(UUID movieReviewId) {
        movieReviewService.getMovieReview(movieReviewId);
        List<MovieSpoilerData> movieSpoilerDataList =
                movieSpoilerDataRepository.findByMovieReviewIdOrderByIdAsc(movieReviewId);
        return movieSpoilerDataList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public MovieSpoilerDataReadDTO createMovieReviewSpoilerData(UUID movieReviewId, MovieSpoilerDataCreateDTO create) {

        MovieReview movieReview = translationService.ReadDTOtoEntity(movieReviewService.getMovieReview(movieReviewId));

        MovieSpoilerData movieSpoilerData = translationService.toEntity(create);
        movieSpoilerData.setMovieReviewId(movieReview);

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

    public MovieSpoilerDataReadDTO putMovieReviewSpoilerData(UUID movieReviewId, UUID id, MovieSpoilerDataPutDTO put) {

        MovieSpoilerData movieSpoilerData = getMovieReviewSpoilerDataRequired(movieReviewId, id);

        translationService.putEntity(put, movieSpoilerData);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.toRead(movieSpoilerData);
    }

    private MovieSpoilerData getMovieSpoilerDataRequired(UUID id) {
        return movieSpoilerDataRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieSpoilerData.class, id);
        });
    }

    private MovieSpoilerData getMovieReviewSpoilerDataRequired(UUID moveReviewId, UUID id) {
        return movieSpoilerDataRepository.findByMovieReviewIdAndId(moveReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieSpoilerData.class, moveReviewId, id);
        });
    }
}
