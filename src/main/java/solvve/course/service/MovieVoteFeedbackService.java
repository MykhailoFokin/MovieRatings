package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieVoteFeedback;
import solvve.course.dto.MovieVoteFeedbackCreateDTO;
import solvve.course.dto.MovieVoteFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteFeedbackRepository;

import java.util.UUID;

@Service
public class MovieVoteFeedbackService {

    @Autowired
    private MovieVoteFeedbackRepository movieVoteFeedbackRepository;

    @Transactional(readOnly = true)
    public MovieVoteFeedbackReadDTO getMovieVoteFeedback(UUID id) {
        MovieVoteFeedback movieVoteFeedback = movieVoteFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieVoteFeedback.class, id);
        });
        return toRead(movieVoteFeedback);
    }

    private MovieVoteFeedbackReadDTO toRead(MovieVoteFeedback movieVoteFeedback) {
        MovieVoteFeedbackReadDTO dto = new MovieVoteFeedbackReadDTO();
        dto.setId(movieVoteFeedback.getId());
        dto.setUserId(movieVoteFeedback.getUserId());
        dto.setMovieId(movieVoteFeedback.getMovieId());
        dto.setMovieVoteId(movieVoteFeedback.getMovieVoteId());
        dto.setIsLiked(movieVoteFeedback.getIsLiked());
        return dto;
    }

    public MovieVoteFeedbackReadDTO createMovieVoteFeedback(MovieVoteFeedbackCreateDTO create) {
        MovieVoteFeedback movieVoteFeedback = new MovieVoteFeedback();
        movieVoteFeedback.setUserId(create.getUserId());
        movieVoteFeedback.setMovieId(create.getMovieId());
        movieVoteFeedback.setMovieVoteId(create.getMovieVoteId());
        movieVoteFeedback.setIsLiked(create.getIsLiked());

        movieVoteFeedback = movieVoteFeedbackRepository.save(movieVoteFeedback);
        return toRead(movieVoteFeedback);
    }
}
