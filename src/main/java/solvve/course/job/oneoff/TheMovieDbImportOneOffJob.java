package solvve.course.job.oneoff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import solvve.course.client.themoviedb.TheMovieDbClient;
import solvve.course.client.themoviedb.dto.MovieReadShortDTO;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.service.AsyncService;
import solvve.course.service.importer.MovieImporterService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class TheMovieDbImportOneOffJob {

    @Value("${themoviedb.import.job.enabled}")
    private boolean enabled;

    @Autowired
    private TheMovieDbClient client;

    @Autowired
    private MovieImporterService movieImporterService;

    @Autowired
    private AsyncService asyncService;

    @PostConstruct
    void executeJob() {
        if (!enabled) {
            log.info("Import is disabled");
            return;
        }

        asyncService.executeAsync(this::doImport);
    }

    public void doImport() {
        log.info("Starting import");

        try {
            List<MovieReadShortDTO> moviesToImport = client.getTopRatedMovies().getResults();
            int successfullyImported = 0;
            int skipped = 0;
            int failed = 0;

            for (MovieReadShortDTO m : client.getTopRatedMovies().getResults()) {
                try {
                    movieImporterService.importMovie(m.getId(), null);
                    successfullyImported++;
                } catch (ImportedEntityAlreadyExistException | ImportAlreadyPerformedException e) {
                    log.info("Can't import the movie id={}, title={}: {}, it will be skipped", m.getId(), m.getTitle(),
                            e.getMessage());
                    skipped++;
                } catch (Exception e) {
                    log.warn("Failed to import movie id={}, title={}", m.getId(), m.getTitle(), e);
                    failed++;
                }
            }
            log.info("Total movies to import: {}, successfully imported: {}, skipped: {}, failed: {}",
                    moviesToImport.size(), successfullyImported, skipped, failed);
        }
        catch (Exception e) {
            log.warn("Failed to perform import", e);
        }

        log.info("Import finished");
    }
}
