package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieMovieCompanyReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.MovieCompanyRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieMovieCompanyService extends AbstractService {

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieMovieCompanyReadDTO> getMovieCompanies(UUID movieId) {
        List<MovieCompany> companies = getMovieCompaniesByMovieIdRequired(movieId);
        return translationService.translateList(companies, MovieMovieCompanyReadDTO.class);
    }

    @Transactional
    public List<MovieMovieCompanyReadDTO> addMovieCompanyToMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        MovieCompany movieCompany = repositoryHelper.getByIdRequired(MovieCompany.class, id);

        if (movie.getMovieProdCompanies().stream().anyMatch(ml -> ml.getId().equals(id))) {
            throw new LinkDuplicatedException(String.format("Movie %s already has movieCompany %s", movieId, id));
        }

        movie.getMovieProdCompanies().add(movieCompany);
        movie = movieRepository.save(movie);

        return movie.getMovieProdCompanies().stream()
                .map(ur -> translationService.translate(ur, MovieMovieCompanyReadDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieMovieCompanyReadDTO> removeMovieCompanyFromMovie(UUID movieId, UUID id) {
        Movie movie = repositoryHelper.getByIdRequired(Movie.class, movieId);

        boolean removed = movie.getMovieProdCompanies().removeIf(l -> l.getId().equals(id));
        if (!removed) {
            throw new EntityNotFoundException("Movie " + movieId + " has no movieCompany " + id);
        }

        movie = movieRepository.save(movie);

        return translationService.translateList(movie.getMovieProdCompanies(), MovieMovieCompanyReadDTO.class);
    }

    private List<MovieCompany> getMovieCompaniesByMovieIdRequired(UUID movieId) {
        return movieCompanyRepository.findCompaniesByMovieId(movieId).orElseThrow(() -> {
            throw new EntityNotFoundException("Movie " + movieId + " has no companies");
        });
    }
}
