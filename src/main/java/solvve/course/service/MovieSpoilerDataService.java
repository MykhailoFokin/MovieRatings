package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieSpoilerDataRepository;

import java.util.UUID;

@Service
public class MovieSpoilerDataService {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Transactional(readOnly = true)
    public MovieSpoilerDataReadDTO getMovieSpoilerData(UUID id) {
        MovieSpoilerData movieSpoilerData = getMovieSpoilerDataRequired(id);
        return toRead(movieSpoilerData);
    }

    private MovieSpoilerDataReadDTO toRead(MovieSpoilerData movieSpoilerData) {
        MovieSpoilerDataReadDTO dto = new MovieSpoilerDataReadDTO();
        dto.setId(movieSpoilerData.getId());
        dto.setMovieReviewId(movieSpoilerData.getMovieReviewId());
        dto.setStartIndex(movieSpoilerData.getStartIndex());
        dto.setEndIndex(movieSpoilerData.getEndIndex());
        return dto;
    }

    public MovieSpoilerDataReadDTO createMovieSpoilerData(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(create.getMovieReviewId());
        movieSpoilerData.setStartIndex(create.getStartIndex());
        movieSpoilerData.setEndIndex(create.getEndIndex());

        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return toRead(movieSpoilerData);
    }

    public MovieSpoilerDataReadDTO patchMovieSpoilerData(UUID id, MovieSpoilerDataPatchDTO patch) {
        MovieSpoilerData movieSpoilerData = getMovieSpoilerDataRequired(id);

        if (patch.getMovieReviewId()!=null) {
            movieSpoilerData.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getStartIndex()!=null) {
            movieSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex()!=null) {
            movieSpoilerData.setEndIndex(patch.getEndIndex());
        }
        movieSpoilerData = movieSpoilerDataRepository.save(movieSpoilerData);
        return toRead(movieSpoilerData);
    }

    private MovieSpoilerData getMovieSpoilerDataRequired(UUID id) {
        return movieSpoilerDataRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieSpoilerData.class, id);
        });
    }

    public void deleteMovieSpoilerData(UUID id) {
        movieSpoilerDataRepository.delete(getMovieSpoilerDataRequired(id));
    }
}
