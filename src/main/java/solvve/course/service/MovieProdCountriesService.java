package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieProdCountries;
import solvve.course.dto.MovieProdCountriesCreateDTO;
import solvve.course.dto.MovieProdCountriesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieProdCountriesRepository;

import java.util.UUID;

@Service
public class MovieProdCountriesService {

    @Autowired
    private MovieProdCountriesRepository movieProdCountriesRepository;

    @Transactional(readOnly = true)
    public MovieProdCountriesReadDTO getMovieProdCountries(UUID id) {
        MovieProdCountries movieProdCountries = movieProdCountriesRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieProdCountries.class, id);
        });
        return toRead(movieProdCountries);
    }

    private MovieProdCountriesReadDTO toRead(MovieProdCountries movieProdCountries) {
        MovieProdCountriesReadDTO dto = new MovieProdCountriesReadDTO();
        dto.setMovieId(movieProdCountries.getMovieId());
        dto.setCountryId(movieProdCountries.getCountryId());
        return dto;
    }

    public MovieProdCountriesReadDTO createMovieProdCountries(MovieProdCountriesCreateDTO create) {
        MovieProdCountries movieProdCountries = new MovieProdCountries();
        movieProdCountries.setCountryId(create.getCountryId());
        movieProdCountries.setMovieId(create.getMovieId());

        movieProdCountries = movieProdCountriesRepository.save(movieProdCountries);
        return toRead(movieProdCountries);
    }
}
