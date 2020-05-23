package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.service.importer.MovieImporterService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/external-movies-import")
public class ExternalMovieImportController {

    @Autowired
    private MovieImporterService movieImporterService;

    @AdminOrContentManager
    @GetMapping("/{id}")
    public UUID importExternalMovie(@PathVariable String id) throws ImportedEntityAlreadyExistException,
            ImportAlreadyPerformedException {
        return movieImporterService.importMovie(id, null);
    }
}
