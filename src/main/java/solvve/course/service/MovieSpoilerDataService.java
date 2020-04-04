package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieSpoilerDataRepository;

import java.util.UUID;

@Service
public class MovieSpoilerDataService extends AbstractService {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Transactional(readOnly = true)
    public MovieSpoilerDataReadDTO getMovieSpoilerData(UUID id) {
        MovieSpoilerData movieSpoilerData = repositoryHelper.getByIdRequired(MovieSpoilerData.class, id);
        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }

    public MovieSpoilerDataReadDTO createMovieSpoilerData(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = translationService.translate(create, MovieSpoilerData.class);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }

    public MovieSpoilerDataReadDTO patchMovieSpoilerData(UUID id, MovieSpoilerDataPatchDTO patch) {
        MovieSpoilerData movieSpoilerData = repositoryHelper.getByIdRequired(MovieSpoilerData.class, id);

        translationService.map(patch, movieSpoilerData);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }

    public void deleteMovieSpoilerData(UUID id) {
        movieSpoilerDataRepository.delete(repositoryHelper.getByIdRequired(MovieSpoilerData.class, id));
    }

    public MovieSpoilerDataReadDTO updateMovieSpoilerData(UUID id, MovieSpoilerDataPutDTO put) {
        MovieSpoilerData movieSpoilerData = repositoryHelper.getByIdRequired(MovieSpoilerData.class, id);

        translationService.updateEntity(put, movieSpoilerData);

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return translationService.translate(movieSpoilerData, MovieSpoilerDataReadDTO.class);
    }
}
