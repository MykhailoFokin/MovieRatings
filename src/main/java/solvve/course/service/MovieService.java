package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieReadDTO;
import solvve.course.repository.MovieRepository;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public MovieReadDTO getMovie(UUID id) {
        Movie movie = movieRepository.findById(id).get();
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
        dto.setFilminglocations(movie.getFilmingLocations());
        dto.setCritique(movie.getCritique());
        return dto;
    }
}
