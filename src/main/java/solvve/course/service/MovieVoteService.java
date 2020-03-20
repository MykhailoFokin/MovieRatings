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
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    public MovieVoteReadDTO createMovieVote(MovieVoteCreateDTO create) {
        MovieVote movieVote = translationService.translate(create, MovieVote.class);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    public MovieVoteReadDTO patchMovieVote(UUID id, MovieVotePatchDTO patch) {
        MovieVote movieVote = getMovieVoteRequired(id);

        translationService.map(patch, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    private MovieVote getMovieVoteRequired(UUID id) {
        return movieVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieVote.class, id);
        });
    }

    public void deleteMovieVote(UUID id) {
        movieVoteRepository.delete(getMovieVoteRequired(id));
    }

    public MovieVoteReadDTO updateMovieVote(UUID id, MovieVotePutDTO put) {
        MovieVote movieVote = getMovieVoteRequired(id);

        translationService.updateEntity(put, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }
}
