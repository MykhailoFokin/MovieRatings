package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieVote;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVotePatchDTO;
import solvve.course.dto.MovieVotePutDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteRepository;
import java.util.UUID;

@Service
public class MovieVoteService {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MovieVoteReadDTO getMovieVote(UUID id) {
        MovieVote movieVote = getMovieVoteRequired(id);
        return translationService.toRead(movieVote);
    }

    public MovieVoteReadDTO createMovieVote(MovieVoteCreateDTO create) {
        MovieVote movieVote = translationService.toEntity(create);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.toRead(movieVote);
    }

    public MovieVoteReadDTO patchMovieVote(UUID id, MovieVotePatchDTO patch) {
        MovieVote movieVote = getMovieVoteRequired(id);

        translationService.patchEntity(patch, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.toRead(movieVote);
    }

    private MovieVote getMovieVoteRequired(UUID id) {
        return movieVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieVote.class, id);
        });
    }

    public void deleteMovieVote(UUID id) {
        movieVoteRepository.delete(getMovieVoteRequired(id));
    }

    public MovieVoteReadDTO putMovieVote(UUID id, MovieVotePutDTO put) {
        MovieVote movieVote = getMovieVoteRequired(id);

        translationService.putEntity(put, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.toRead(movieVote);
    }
}
