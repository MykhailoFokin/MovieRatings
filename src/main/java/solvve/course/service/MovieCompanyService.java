package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieCompanyRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieCompanyService {

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MovieCompanyReadDTO getMovieCompany(UUID id) {
        MovieCompany movieCompany = getMovieCompanyRequired(id);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public MovieCompanyReadDTO createMovieCompany(MovieCompanyCreateDTO create) {
        MovieCompany movieCompany = translationService.translate(create, MovieCompany.class);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public MovieCompanyReadDTO patchMovieCompany(UUID id, MovieCompanyPatchDTO patch) {
        MovieCompany movieCompany = getMovieCompanyRequired(id);

        translationService.map(patch, movieCompany);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public void deleteMovieCompany(UUID id) {
        movieCompanyRepository.delete(getMovieCompanyRequired(id));
    }

    public MovieCompanyReadDTO updateMovieCompany(UUID id, MovieCompanyPutDTO put) {
        MovieCompany movieCompany = getMovieCompanyRequired(id);

        translationService.updateEntity(put, movieCompany);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public List<MovieCompanyReadDTO> getMovieCompanies(MovieCompanyFilter filter) {
        List<MovieCompany> movieCompanies = movieCompanyRepository.findByFilter(filter);
        return movieCompanies.stream().map(e -> translationService.translate(e, MovieCompanyReadDTO.class))
                .collect(Collectors.toList());
    }

    private MovieCompany getMovieCompanyRequired(UUID id) {
        return movieCompanyRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieCompany.class, id);
        });
    }
}
