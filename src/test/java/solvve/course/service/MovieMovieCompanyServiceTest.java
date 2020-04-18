package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.CompanyDetails;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.MovieMovieCompanyReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.MovieCompanyRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;

public class MovieMovieCompanyServiceTest extends BaseTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private MovieMovieCompanyService movieMovieCompanyService;

    @Test
    public void testAddMovieCompanyToMovie() {
        Movie movie = testObjectsFactory.createMovie();
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId = testObjectsFactory.createMovieCompany(companyDetails, 
                MovieProductionType.PRODUCTION_COMPANIES).getId();

        List<MovieMovieCompanyReadDTO> res =
                movieMovieCompanyService.addMovieCompanyToMovie(movie.getId(), movieCompanyId);

        MovieMovieCompanyReadDTO expectedRead = new MovieMovieCompanyReadDTO();
        expectedRead.setId(movieCompanyId);
        expectedRead.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);
        expectedRead.setDescription("Desc");
        Assertions.assertThat(res).containsExactlyInAnyOrder(expectedRead);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterSave = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterSave.getMovieProdCompanies()).extracting(MovieCompany::getId)
                    .containsExactlyInAnyOrder(movieCompanyId);
        });
    }

    @Test
    public void testDuplicatedMovieCompany() {
        Movie movie = testObjectsFactory.createMovie();
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();

        movieMovieCompanyService.addMovieCompanyToMovie(movie.getId(), movieCompanyId);
        Assertions.assertThatThrownBy(() -> {
            movieMovieCompanyService.addMovieCompanyToMovie(movie.getId(), movieCompanyId);
        }).isInstanceOf(LinkDuplicatedException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongMovieId() {
        UUID wrongMovieId = UUID.randomUUID();
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        movieMovieCompanyService.addMovieCompanyToMovie(wrongMovieId, movieCompanyId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongMovieCompanyId() {
        Movie movie = testObjectsFactory.createMovie();
        UUID movieCompanyId = UUID.randomUUID();
        movieMovieCompanyService.addMovieCompanyToMovie(movie.getId(), movieCompanyId);
    }

    @Test
    public void testRemoveMovieCompanyFromMovie() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId1 = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        UUID movieCompanyId2 = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        Movie movie = testObjectsFactory.createMovieWithCompanies(List.of(movieCompanyId1, movieCompanyId2));

        movieMovieCompanyService.removeMovieCompanyFromMovie(movie.getId(), movieCompanyId1);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterRemove.getMovieProdCompanies()).extracting(MovieCompany::getId)
                    .containsExactlyInAnyOrder(movieCompanyId2);
        });
    }

    @Test
    public void testAddAndThenRemoveMovieCompanyFromMovie() {
        Movie movie = testObjectsFactory.createMovie();
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        movieMovieCompanyService.addMovieCompanyToMovie(movie.getId(), movieCompanyId);

        List<MovieMovieCompanyReadDTO> remainingCompanies =
                movieMovieCompanyService.removeMovieCompanyFromMovie(movie.getId(), movieCompanyId);
        Assert.assertTrue(remainingCompanies.isEmpty());

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assert.assertTrue(movieAfterRemove.getMovieProdCompanies().isEmpty());
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotAddedMovieCompany() {
        Movie movie = testObjectsFactory.createMovie();
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();

        movieMovieCompanyService.removeMovieCompanyFromMovie(movie.getId(), movieCompanyId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotExistedMovieCompany() {
        Movie movie = testObjectsFactory.createMovie();
        movieMovieCompanyService.removeMovieCompanyFromMovie(movie.getId(), UUID.randomUUID());
    }

    @Test
    public void testGetMovieCompanies() {
        CompanyDetails companyDetails = testObjectsFactory.createCompanyDetails();
        UUID movieCompanyId1 = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        UUID movieCompanyId2 = testObjectsFactory.createMovieCompany(companyDetails,
                MovieProductionType.PRODUCTION_COMPANIES).getId();
        Movie movie = testObjectsFactory.createMovieWithCompanies(List.of(movieCompanyId1, movieCompanyId2));

        Assertions.assertThat(movieMovieCompanyService.getMovieCompanies(movie.getId()))
                .extracting(MovieMovieCompanyReadDTO::getId)
                .containsExactlyInAnyOrder(movieCompanyId1, movieCompanyId2);
    }
}
