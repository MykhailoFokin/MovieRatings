package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.CountryRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieCountryService extends AbstractService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieCountryReadDTO> getMovieCountries(UUID movieId) {
        List<Country> countries = getMovieCountriesByMovieIdRequired(movieId);
        return translationService.translateList(countries, MovieCountryReadDTO.class);
    }

    @Transactional
    public List<MovieCountryReadDTO> addCountryToMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        Country country = repositoryHelper.getByIdRequired(Country.class, id);

        if (movie.getMovieProdCountries().stream().anyMatch(ml -> ml.getId().equals(id))) {
            throw new LinkDuplicatedException(String.format("Movie %s already has country %s", movieId, id));
        }

        movie.getMovieProdCountries().add(country);
        movie = movieRepository.save(movie);

        return movie.getMovieProdCountries().stream()
                .map(ur -> translationService.translate(ur, MovieCountryReadDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieCountryReadDTO> removeCountryFromMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        boolean removed = movie.getMovieProdCountries().removeIf(l -> l.getId().equals(id));
        if (!removed) {
            throw new EntityNotFoundException("Movie " + movieId + " has no country " + id);
        }

        movie = movieRepository.save(movie);

        return translationService.translateList(movie.getMovieProdCountries(), MovieCountryReadDTO.class);
    }

    private List<Country> getMovieCountriesByMovieIdRequired(UUID movieId) {
        return countryRepository.findCountriesByMovieId(movieId).orElseThrow(() -> {
            throw new EntityNotFoundException("Movie " + movieId + " has no countries");
        });
    }
}
