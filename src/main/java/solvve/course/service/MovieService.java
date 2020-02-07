package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

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

    public MovieReadDTO putMovie(UUID id, MoviePutDTO put) {
        Movie movie = getMovieRequired(id);

        translationService.putEntity(put, movie);

        movie = movieRepository.save(movie);
        return translationService.toRead(movie);
    }

    public List<MovieReadExtendedDTO> getMovies(MovieFilter filter) {
        List<Movie> movieList = movieRepository.findByFilter(filter);
        return movieList.stream().map(translationService::toReadExtended).collect(Collectors.toList());
    }

    private Movie getMovieRequired(UUID id) {
        return movieRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Movie.class, id);
        });
    }
}