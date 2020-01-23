package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
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
        Movie movie = getMovieRequired(id);
        return toRead(movie);
    }

    private MovieReadDTO toRead(Movie movie) {
        MovieReadDTO dto = new MovieReadDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setGenres(movie.getGenres());
        dto.setDescription(movie.getDescription());
        dto.setCompanies(movie.getCompanies());
        dto.setSoundMix(movie.getSoundMix());
        dto.setColour(movie.getColour());
        dto.setAspectRatio(movie.getAspectRatio());
        dto.setCamera(movie.getCamera());
        dto.setLaboratory(movie.getLaboratory());
        dto.setLanguages(movie.getLanguages());
        dto.setFilmingLocations(movie.getFilmingLocations());
        dto.setCritique(movie.getCritique());
        dto.setIsPublished(movie.getIsPublished());
        dto.setMovieProdCountries(movie.getMovieProdCountries());
        return dto;
    }

    public MovieReadDTO createMovie(MovieCreateDTO create) {
        Movie movie = new Movie();
        movie.setTitle(create.getTitle());
        movie.setYear(create.getYear());
        movie.setGenres(create.getGenres());
        movie.setDescription(create.getDescription());
        movie.setCompanies(create.getCompanies());
        movie.setSoundMix(create.getSoundMix());
        movie.setColour(create.getColour());
        movie.setAspectRatio(create.getAspectRatio());
        movie.setCamera(create.getCamera());
        movie.setLaboratory(create.getLaboratory());
        movie.setLanguages(create.getLanguages());
        movie.setFilmingLocations(create.getFilmingLocations());
        movie.setCritique(create.getCritique());
        movie.setIsPublished(create.getIsPublished());
        movie.setMovieProdCountries(create.getMovieProdCountries());

        movie = movieRepository.save(movie);
        return toRead(movie);
    }

    public MovieReadDTO patchMovie(UUID id, MoviePatchDTO patch) {
        Movie movie = getMovieRequired(id);

        if (patch.getTitle()!=null) {
            movie.setTitle(patch.getTitle());
        }
        if (patch.getYear()!=null) {
            movie.setYear(patch.getYear());
        }
        if (patch.getGenres()!=null) {
            movie.setGenres(patch.getGenres());
        }
        if (patch.getDescription()!=null) {
            movie.setDescription(patch.getDescription());
        }
        if (patch.getCompanies()!=null) {
            movie.setCompanies(patch.getCompanies());
        }
        if (patch.getSoundMix()!=null) {
            movie.setSoundMix(patch.getSoundMix());
        }
        if (patch.getColour()!=null) {
            movie.setColour(patch.getColour());
        }
        if (patch.getAspectRatio()!=null) {
            movie.setAspectRatio(patch.getAspectRatio());
        }
        if (patch.getCamera()!=null) {
            movie.setCamera(patch.getCamera());
        }
        if (patch.getLaboratory()!=null) {
            movie.setLaboratory(patch.getLaboratory());
        }
        if (patch.getLanguages()!=null) {
            movie.setLanguages(patch.getLanguages());
        }
        if (patch.getFilmingLocations()!=null) {
            movie.setFilmingLocations(patch.getFilmingLocations());
        }
        if (patch.getCritique()!=null) {
            movie.setCritique(patch.getCritique());
        }
        if (patch.getIsPublished()!=null) {
            movie.setIsPublished(patch.getIsPublished());
        }
        if (patch.getMovieProdCountries()!=null) {
            movie.setMovieProdCountries(patch.getMovieProdCountries());
        }
        movie = movieRepository.save(movie);
        return toRead(movie);
    }

    private Movie getMovieRequired(UUID id) {
        return movieRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Movie.class, id);
        });
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(getMovieRequired(id));
    }
}