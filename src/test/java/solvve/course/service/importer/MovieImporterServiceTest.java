package solvve.course.service.importer;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import solvve.course.BaseTest;
import solvve.course.client.themoviedb.TheMovieDbClient;
import solvve.course.client.themoviedb.dto.MovieCreditsCastReadDTO;
import solvve.course.client.themoviedb.dto.MovieCreditsCrewReadDTO;
import solvve.course.client.themoviedb.dto.MovieCreditsReadDTO;
import solvve.course.client.themoviedb.dto.MovieReadDTO;
import solvve.course.domain.Movie;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;

public class MovieImporterServiceTest extends BaseTest {

    @MockBean
    private TheMovieDbClient movieDbClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImporterService movieImporterService;

    @Test
    public void testMovieImporter() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        String movieExternalId = "id1";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate("2020-01-01");
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        UUID movieId = movieImporterService.importMovie(movieExternalId, null);
        Movie movie = movieRepository.findById(movieId).get();

        Assert.assertEquals(read.getTitle(), movie.getTitle());
    }

    @Test
    public void testMovieImportAlreadyExist() {
        String movieExternalId = "id2";

        Movie existingMovie = testObjectsFactory.generateFlatEntityWithoutId(Movie.class);
        existingMovie = movieRepository.save(existingMovie);

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setTitle(existingMovie.getTitle());
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        ImportedEntityAlreadyExistException ex =
                Assertions.catchThrowableOfType(() -> movieImporterService.importMovie(movieExternalId, null),
                        ImportedEntityAlreadyExistException.class);
        Assert.assertEquals(Movie.class, ex.getEntityClass());
        Assert.assertEquals(existingMovie.getId(), ex.getEntityId());
    }

    @Test
    public void testNoCallToClientOnDuplicateImport() throws ImportedEntityAlreadyExistException,
            ImportAlreadyPerformedException {

        String movieExternalId = "id3";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate("2020-01-01");
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        movieImporterService.importMovie(movieExternalId, null);
        Mockito.verify(movieDbClient).getMovie(movieExternalId, null);
        Mockito.reset(movieDbClient);

        Assertions.assertThatThrownBy(() -> movieImporterService.importMovie(movieExternalId, null))
                .isInstanceOf(ImportAlreadyPerformedException.class);

        Mockito.verifyNoInteractions(movieDbClient);
    }

    @Test
    public void testMovieImporterCast() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        String movieExternalId = "id4";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate("2020-01-01");
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        UUID movieId = movieImporterService.importMovie(movieExternalId, null);
        Movie movie = movieRepository.findById(movieId).get();

        Assert.assertEquals(read.getTitle(), movie.getTitle());
    }
}
