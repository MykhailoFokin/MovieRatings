package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Language;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieLanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.LanguageRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieLanguageService extends AbstractService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieLanguageReadDTO> getMovieLanguages(UUID movieId) {
        List<Language> languages = getMovieLanguagesByMovieIdRequired(movieId);
        return translationService.translateList(languages, MovieLanguageReadDTO.class);
    }

    @Transactional
    public List<MovieLanguageReadDTO> addLanguageToMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        Language language = repositoryHelper.getByIdRequired(Language.class, id);

        if (movie.getMovieProdLanguages().stream().anyMatch(ml -> ml.getId().equals(id))) {
            throw new LinkDuplicatedException(String.format("Movie %s already has language %s", movieId, id));
        }

        movie.getMovieProdLanguages().add(language);
        movie = movieRepository.save(movie);

        return movie.getMovieProdLanguages().stream()
                .map(ur -> translationService.translate(ur, MovieLanguageReadDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieLanguageReadDTO> removeLanguageFromMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        boolean removed = movie.getMovieProdLanguages().removeIf(l -> l.getId().equals(id));
        if (!removed) {
            throw new EntityNotFoundException("Movie " + movieId + " has no language " + id);
        }

        movie = movieRepository.save(movie);

        return translationService.translateList(movie.getMovieProdLanguages(), MovieLanguageReadDTO.class);
    }

    private List<Language> getMovieLanguagesByMovieIdRequired(UUID movieId) {
        return languageRepository.findLanguagesByMovieId(movieId).orElseThrow(() -> {
            throw new EntityNotFoundException("Movie " + movieId + " has no languages");
        });
    }
}
