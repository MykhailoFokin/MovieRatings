package solvve.course.job.oneoff;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import solvve.course.BaseTest;
import solvve.course.client.themoviedb.TheMovieDbClient;
import solvve.course.client.themoviedb.dto.MovieReadShortDTO;
import solvve.course.client.themoviedb.dto.MoviesPageDTO;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.service.importer.MovieImporterService;

public class TheMovieDbImportOneOffJobTest extends BaseTest {

    @Autowired
    private TheMovieDbImportOneOffJob job;

    @MockBean
    private TheMovieDbClient client;

    @MockBean
    private MovieImporterService movieImporterService;

    @Test
    public void testDoImport() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        MoviesPageDTO page = generatePageWith2Results();
        Mockito.when(client.getTopRatedMovies()).thenReturn(page);

        job.doImport();

        for (MovieReadShortDTO m : page.getResults()) {
            Mockito.verify(movieImporterService).importMovie(m.getId(), null);
        }
    }

    @Test
    public void testDoImportNoExceptionIfGetPageFailed() {
        Mockito.when(client.getTopRatedMovies()).thenThrow(RuntimeException.class);

        job.doImport();

        Mockito.verifyNoInteractions(movieImporterService);
    }

    @Test
    public void testDoImportFirstFailedAndSecondSuccess()
            throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        MoviesPageDTO page = generatePageWith2Results();
        Mockito.when(client.getTopRatedMovies()).thenReturn(page);
        Mockito.when(movieImporterService.importMovie(page.getResults().get(0).getId(), null))
                .thenThrow(RuntimeException.class);

        job.doImport();

        for (MovieReadShortDTO m : page.getResults()) {
            Mockito.verify(movieImporterService).importMovie(m.getId(), null);
        }
    }

    @Test
    public void testDoImportImportedEntityAlreadyExistException()
            throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        MoviesPageDTO page = generatePageWith2Results();
        Mockito.when(client.getTopRatedMovies()).thenReturn(page);
        Mockito.when(movieImporterService.importMovie(page.getResults().get(0).getId(), null))
                .thenThrow(ImportedEntityAlreadyExistException.class);

        job.doImport();

        for (MovieReadShortDTO m : page.getResults()) {
            Mockito.verify(movieImporterService).importMovie(m.getId(), null);
        }
    }

    @Test
    public void testDoImportImportAlreadyPerformedException()
            throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        MoviesPageDTO page = generatePageWith2Results();
        Mockito.when(client.getTopRatedMovies()).thenReturn(page);
        Mockito.when(movieImporterService.importMovie(page.getResults().get(0).getId(), null))
                .thenThrow(ImportAlreadyPerformedException.class);

        job.doImport();

        for (MovieReadShortDTO m : page.getResults()) {
            Mockito.verify(movieImporterService).importMovie(m.getId(), null);
        }
    }

    @Test
    public void testDoImportWithSetAsyncToDisabled() throws ImportedEntityAlreadyExistException,
            ImportAlreadyPerformedException {
        ReflectionTestUtils.setField(job, "enabled", true);

        MoviesPageDTO page = generatePageWith2Results();
        Mockito.when(client.getTopRatedMovies()).thenReturn(page);

        job.doImport();

        for (MovieReadShortDTO m : page.getResults()) {
            Mockito.verify(movieImporterService).importMovie(m.getId(), null);
        }
    }

    private MoviesPageDTO generatePageWith2Results() {
        MoviesPageDTO page = testObjectsFactory.generateObject(MoviesPageDTO.class);
        page.getResults().add(testObjectsFactory.generateObject(MovieReadShortDTO.class));
        Assert.assertEquals(2, page.getResults().size());

        return page;
    }
}
