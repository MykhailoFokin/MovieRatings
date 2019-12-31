package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieVoteCompliant;
import solvve.course.dto.MovieVoteCompliantCreateDTO;
import solvve.course.dto.MovieVoteCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteCompliantRepository;

import java.util.UUID;

@Service
public class MovieVoteCompliantService {

    @Autowired
    private MovieVoteCompliantRepository movieVoteCompliantRepository;

    @Transactional(readOnly = true)
    public MovieVoteCompliantReadDTO getMovieVoteCompliant(UUID id) {
        MovieVoteCompliant movieVoteCompliant = movieVoteCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieVoteCompliant.class, id);
        });
        return toRead(movieVoteCompliant);
    }

    private MovieVoteCompliantReadDTO toRead(MovieVoteCompliant movieVoteCompliant) {
        MovieVoteCompliantReadDTO dto = new MovieVoteCompliantReadDTO();
        dto.setId(movieVoteCompliant.getId());
        dto.setUserId(movieVoteCompliant.getUserId());
        dto.setMovieId(movieVoteCompliant.getMovieId());
        dto.setMovieVoteId(movieVoteCompliant.getMovieVoteId());
        dto.setDescription(movieVoteCompliant.getDescription());
        dto.setModeratedStatus(movieVoteCompliant.getModeratedStatus());
        dto.setModeratorId(movieVoteCompliant.getModeratorId());
        return dto;
    }

    public MovieVoteCompliantReadDTO createMovieVoteCompliant(MovieVoteCompliantCreateDTO create) {
        MovieVoteCompliant movieVoteCompliant = new MovieVoteCompliant();
        movieVoteCompliant.setUserId(create.getUserId());
        movieVoteCompliant.setMovieId(create.getMovieId());
        movieVoteCompliant.setMovieVoteId(create.getMovieVoteId());
        movieVoteCompliant.setDescription(create.getDescription());
        movieVoteCompliant.setModeratedStatus(create.getModeratedStatus());
        movieVoteCompliant.setModeratorId(create.getModeratorId());

        movieVoteCompliant = movieVoteCompliantRepository.save(movieVoteCompliant);
        return toRead(movieVoteCompliant);
    }
}
