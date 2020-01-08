package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieVote;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteRepository;
import java.util.UUID;

@Service
public class MovieVoteService {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Transactional(readOnly = true)
    public MovieVoteReadDTO getMovieVote(UUID id) {
        MovieVote movieVote = movieVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieVote.class, id);
        });
        return toRead(movieVote);
    }

    private MovieVoteReadDTO toRead(MovieVote movieVote) {
        MovieVoteReadDTO dto = new MovieVoteReadDTO();
        dto.setId(movieVote.getId());
        dto.setUserId(movieVote.getUserId());
        dto.setMovieId(movieVote.getMovieId());
        dto.setRating(movieVote.getRating());
        return dto;
    }

    public MovieVoteReadDTO createMovieVote(MovieVoteCreateDTO create) {
        MovieVote movieVote = new MovieVote();
        movieVote.setUserId(create.getUserId());
        movieVote.setMovieId(create.getMovieId());
        movieVote.setRating(create.getRating());

        movieVote = movieVoteRepository.save(movieVote);
        return toRead(movieVote);
    }
}
