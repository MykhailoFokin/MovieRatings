package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewCompliantRepository;

import java.util.UUID;

@Service
public class MovieReviewCompliantService {

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Transactional(readOnly = true)
    public MovieReviewCompliantReadDTO getMovieReviewCompliant(UUID id) {
        MovieReviewCompliant movieReviewCompliant = movieReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, id);
        });
        return toRead(movieReviewCompliant);
    }

    private MovieReviewCompliantReadDTO toRead(MovieReviewCompliant movieReviewCompliant) {
        MovieReviewCompliantReadDTO dto = new MovieReviewCompliantReadDTO();
        dto.setId(movieReviewCompliant.getId());
        dto.setUserId(movieReviewCompliant.getUserId());
        dto.setMovieId(movieReviewCompliant.getMovieId());
        dto.setMovieReviewId(movieReviewCompliant.getMovieReviewId());
        dto.setDescription(movieReviewCompliant.getDescription());
        dto.setModeratedStatus(movieReviewCompliant.getModeratedStatus());
        dto.setModeratorId(movieReviewCompliant.getModeratorId());
        return dto;
    }

    public MovieReviewCompliantReadDTO createMovieReviewCompliant(MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(create.getUserId());
        movieReviewCompliant.setMovieId(create.getMovieId());
        movieReviewCompliant.setMovieReviewId(create.getMovieReviewId());
        movieReviewCompliant.setDescription(create.getDescription());
        movieReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        movieReviewCompliant.setModeratorId(create.getModeratorId());

        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);
        return toRead(movieReviewCompliant);
    }
}
