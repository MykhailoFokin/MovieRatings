package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public MovieReadDTO getMovie(UUID id) {
        /*Movie movie = movieRepository.findById(id).get();*/
        Movie movie = movieRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Movie.class, id);
        });
        return toRead(movie);
    }

    private MovieReadDTO toRead(Movie movie) {
        MovieReadDTO dto = new MovieReadDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setGenres(movie.getGenres());
        dto.setReleaseDates(movie.getReleaseDates());
        dto.setDescription(movie.getDescription());
        dto.setCrew(movie.getCrew());
        dto.setCompanies(movie.getCompanies());
        dto.setSoundMix(movie.getSoundMix());
        dto.setColour(movie.getColour());
        dto.setAspectRatio(movie.getAspectRatio());
        dto.setCamera(movie.getCamera());
        dto.setLaboratory(movie.getLaboratory());
        dto.setCountries(movie.getCountries());
        dto.setLanguages(movie.getLanguages());
        dto.setFilmingLocations(movie.getFilmingLocations());
        dto.setCritique(movie.getCritique());
        return dto;
    }

    public MovieReadDTO createMovie(MovieCreateDTO create) {
        Movie movie = new Movie();
        movie.setTitle(create.getTitle());
        movie.setYear(create.getYear());
        movie.setGenres(create.getGenres());
        movie.setReleaseDates(create.getReleaseDates());
        movie.setDescription(create.getDescription());
        movie.setCrew(create.getCrew());
        movie.setCompanies(create.getCompanies());
        movie.setSoundMix(create.getSoundMix());
        movie.setColour(create.getColour());
        movie.setAspectRatio(create.getAspectRatio());
        movie.setCamera(create.getCamera());
        movie.setLaboratory(create.getLaboratory());
        movie.setCountries(create.getCountries());
        movie.setLanguages(create.getLanguages());
        movie.setFilmingLocations(create.getFilmingLocations());
        movie.setCritique(create.getCritique());

        movie = movieRepository.save(movie);
        return toRead(movie);
    }
}
