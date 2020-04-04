package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieVote;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVotePatchDTO;
import solvve.course.dto.MovieVotePutDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.repository.MovieVoteRepository;
import java.util.UUID;

@Service
public class MovieVoteService extends AbstractService {

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Transactional(readOnly = true)
    public MovieVoteReadDTO getMovieVote(UUID id) {
        MovieVote movieVote = repositoryHelper.getByIdRequired(MovieVote.class, id);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    public MovieVoteReadDTO createMovieVote(MovieVoteCreateDTO create) {
        MovieVote movieVote = translationService.translate(create, MovieVote.class);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    public MovieVoteReadDTO patchMovieVote(UUID id, MovieVotePatchDTO patch) {
        MovieVote movieVote = repositoryHelper.getByIdRequired(MovieVote.class, id);

        translationService.map(patch, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }

    public void deleteMovieVote(UUID id) {
        movieVoteRepository.delete(repositoryHelper.getByIdRequired(MovieVote.class, id));
    }

    public MovieVoteReadDTO updateMovieVote(UUID id, MovieVotePutDTO put) {
        MovieVote movieVote = repositoryHelper.getByIdRequired(MovieVote.class, id);

        translationService.updateEntity(put, movieVote);

        movieVote = movieVoteRepository.save(movieVote);
        return translationService.translate(movieVote, MovieVoteReadDTO.class);
    }
}
