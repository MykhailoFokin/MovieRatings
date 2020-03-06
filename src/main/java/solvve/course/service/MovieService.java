package solvve.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;
import solvve.course.repository.MovieVoteRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MovieReadDTO getMovie(UUID id) {
        Movie movie = getMovieRequired(id);
        return translationService.toRead(movie);
    }

    public MovieReadDTO createMovie(MovieCreateDTO create) {
        Movie movie = translationService.toEntity(create);

        movie = movieRepository.save(movie);
        return translationService.toRead(movie);
    }

    public MovieReadDTO patchMovie(UUID id, MoviePatchDTO patch) {
        Movie movie = getMovieRequired(id);

        translationService.patchEntity(patch, movie);

        movie = movieRepository.save(movie);
        return translationService.toRead(movie);
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(getMovieRequired(id));
    }

    public MovieReadDTO updateMovie(UUID id, MoviePutDTO put) {
        Movie movie = getMovieRequired(id);

        translationService.updateEntity(put, movie);

        movie = movieRepository.save(movie);
        return translationService.toRead(movie);
    }

    public List<MovieReadExtendedDTO> getMovies(MovieFilter filter) {
        List<Movie> movies = movieRepository.findByFilter(filter);
        return movies.stream().map(translationService::toReadExtended).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfMovie(UUID movieId) {
        Double averageRating = movieVoteRepository.calcAverageMarkOfMovie(movieId);
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new EntityNotFoundException(Movie.class, movieId));

        log.info("Setting new average rating of movie: {}. Old value: {}, new value: {}", movieId,
                movie.getAverageRating(), averageRating);
        movie.setAverageRating(averageRating);
        movieRepository.save(movie);
    }

    private Movie getMovieRequired(UUID id) {
        return movieRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Movie.class, id);
        });
    }
}