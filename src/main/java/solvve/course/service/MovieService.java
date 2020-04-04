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
public class MovieService extends AbstractService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Transactional(readOnly = true)
    public MovieReadDTO getMovie(UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, id);
        return translationService.translate(movie, MovieReadDTO.class);
    }

    public MovieReadDTO createMovie(MovieCreateDTO create) {
        Movie movie = translationService.translate(create, Movie.class);

        movie = movieRepository.save(movie);
        return translationService.translate(movie, MovieReadDTO.class);
    }

    public MovieReadDTO patchMovie(UUID id, MoviePatchDTO patch) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, id);

        translationService.map(patch, movie);

        movie = movieRepository.save(movie);
        return translationService.translate(movie, MovieReadDTO.class);
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(repositoryHelper.getByIdRequired(Movie.class, id));
    }

    public MovieReadDTO updateMovie(UUID id, MoviePutDTO put) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, id);

        translationService.updateEntity(put, movie);

        movie = movieRepository.save(movie);
        return translationService.translate(movie, MovieReadDTO.class);
    }

    public List<MovieReadDTO> getMovies(MovieFilter filter) {
        List<Movie> movies = movieRepository.findByFilter(filter);
        return movies.stream().map(e -> translationService.translate(e, MovieReadDTO.class))
                .collect(Collectors.toList());
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
}