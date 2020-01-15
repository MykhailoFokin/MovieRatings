package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
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
        MovieReviewCompliant movieReviewCompliant = getMovieReviewCompliantRequired(id);
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

    public MovieReviewCompliantReadDTO patchMovieReviewCompliant(UUID id, MovieReviewCompliantPatchDTO patch) {
        MovieReviewCompliant movieReviewCompliant = getMovieReviewCompliantRequired(id);

        if (patch.getUserId()!=null) {
            movieReviewCompliant.setUserId(patch.getUserId());
        }
        if (patch.getMovieId()!=null) {
            movieReviewCompliant.setMovieId(patch.getMovieId());
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewCompliant.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getDescription()!=null) {
            movieReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            movieReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            movieReviewCompliant.setModeratorId(patch.getModeratorId());
        }
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);
        return toRead(movieReviewCompliant);
    }

    private MovieReviewCompliant getMovieReviewCompliantRequired(UUID id) {
        return movieReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReviewCompliant.class, id);
        });
    }

    public void deleteMovieReviewCompliant(UUID id) {
        movieReviewCompliantRepository.delete(getMovieReviewCompliantRequired(id));
    }
}
