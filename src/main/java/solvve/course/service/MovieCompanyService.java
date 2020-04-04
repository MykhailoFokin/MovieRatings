package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.*;
import solvve.course.repository.MovieCompanyRepository;

import java.util.UUID;

@Service
public class MovieCompanyService extends AbstractService {

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Transactional(readOnly = true)
    public MovieCompanyReadDTO getMovieCompany(UUID id) {
        MovieCompany movieCompany = repositoryHelper.getByIdRequired(MovieCompany.class, id);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public MovieCompanyReadDTO createMovieCompany(MovieCompanyCreateDTO create) {
        MovieCompany movieCompany = translationService.translate(create, MovieCompany.class);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public MovieCompanyReadDTO patchMovieCompany(UUID id, MovieCompanyPatchDTO patch) {
        MovieCompany movieCompany = repositoryHelper.getByIdRequired(MovieCompany.class, id);

        translationService.map(patch, movieCompany);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public void deleteMovieCompany(UUID id) {
        movieCompanyRepository.delete(repositoryHelper.getByIdRequired(MovieCompany.class, id));
    }

    public MovieCompanyReadDTO updateMovieCompany(UUID id, MovieCompanyPutDTO put) {
        MovieCompany movieCompany = repositoryHelper.getByIdRequired(MovieCompany.class, id);

        translationService.updateEntity(put, movieCompany);

        movieCompany = movieCompanyRepository.save(movieCompany);
        return translationService.translate(movieCompany, MovieCompanyReadDTO.class);
    }

    public PageResult<MovieCompanyReadDTO> getMovieCompanies(MovieCompanyFilter filter, Pageable pageable) {
        Page<MovieCompany> movieCompanies = movieCompanyRepository.findByFilter(filter, pageable);
        return translationService.toPageResult(movieCompanies, MovieCompanyReadDTO.class);
    }
}
