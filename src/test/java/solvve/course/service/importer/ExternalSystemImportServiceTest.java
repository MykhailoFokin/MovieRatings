package solvve.course.service.importer;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.repository.ExternalSystemImportRepository;

import java.util.UUID;

public class ExternalSystemImportServiceTest extends BaseTest {

    @Autowired
    private ExternalSystemImportService externalSystemImportService;

    @Autowired
    private ExternalSystemImportRepository externalSystemImportRepository;

    @Test
    public void testValidateNotImportedFromExternalSystem() throws ImportAlreadyPerformedException {
        externalSystemImportService.validateNotImported(Movie.class, "some-id");
    }

    @Test
    public void testExceptionWhenAlreadyImported() {
        ExternalSystemImport esi = testObjectsFactory.generateFlatEntityWithoutId(ExternalSystemImport.class);
        esi.setEntityType(ImportedEntityType.MOVIE);
        esi = externalSystemImportRepository.save(esi);
        String idInExternalSystem = esi.getIdInExternalSystem();

        ImportAlreadyPerformedException ex =
                Assertions.catchThrowableOfType(() -> externalSystemImportService.validateNotImported(Movie.class,
                        idInExternalSystem), ImportAlreadyPerformedException.class);
        Assertions.assertThat(ex.getExternalSystemImport()).isEqualToComparingFieldByField(esi);
    }

    @Test
    public void testNoExceptionWhenAlreadyImportedButDifferentEntityType() throws ImportAlreadyPerformedException {
        ExternalSystemImport esi = testObjectsFactory.generateFlatEntityWithoutId(ExternalSystemImport.class);
        esi.setEntityType(ImportedEntityType.PERSON);
        esi = externalSystemImportRepository.save(esi);

        externalSystemImportService.validateNotImported(Movie.class, esi.getIdInExternalSystem());
    }

    @Test
    public void testCreateExternalSystemImport() {
        Movie movie = testObjectsFactory.generateFlatEntityWithoutId(Movie.class);
        String idInExternalSystem = "id1";
        UUID importId = externalSystemImportService.createExternalSystemImport(movie, idInExternalSystem);
        Assert.assertNotNull(importId);
        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        Assert.assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        Assert.assertEquals(ImportedEntityType.MOVIE, esi.getEntityType());
        Assert.assertEquals(movie.getId(), esi.getEntityId());
    }

    @Test
    public void testCreateExternalSystemImportOfPerson() {
        Person person = testObjectsFactory.generateFlatEntityWithoutId(Person.class);
        String idInExternalSystem = "id1";
        UUID importId = externalSystemImportService.createExternalSystemImport(person, idInExternalSystem);
        Assert.assertNotNull(importId);
        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        Assert.assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        Assert.assertEquals(ImportedEntityType.PERSON, esi.getEntityType());
        Assert.assertEquals(person.getId(), esi.getEntityId());
    }

    @Test
    public void testCreateExternalSystemImportOfCrew() {
        Crew crew = testObjectsFactory.generateFlatEntityWithoutId(Crew.class);
        String idInExternalSystem = "id1";
        UUID importId = externalSystemImportService.createExternalSystemImport(crew, idInExternalSystem);
        Assert.assertNotNull(importId);
        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        Assert.assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        Assert.assertEquals(ImportedEntityType.CREW, esi.getEntityType());
        Assert.assertEquals(crew.getId(), esi.getEntityId());
    }

    @Test
    public void testCreateExternalSystemImportOfCrewType() {
        CrewType crewType = testObjectsFactory.generateFlatEntityWithoutId(CrewType.class);
        String idInExternalSystem = "id1";
        UUID importId = externalSystemImportService.createExternalSystemImport(crewType, idInExternalSystem);
        Assert.assertNotNull(importId);
        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        Assert.assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        Assert.assertEquals(ImportedEntityType.CREW_TYPE, esi.getEntityType());
        Assert.assertEquals(crewType.getId(), esi.getEntityId());
    }

    @Test
    public void testCreateExternalSystemImportOfRole() {
        Role role = testObjectsFactory.generateFlatEntityWithoutId(Role.class);
        String idInExternalSystem = "id1";
        UUID importId = externalSystemImportService.createExternalSystemImport(role, idInExternalSystem);
        Assert.assertNotNull(importId);
        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        Assert.assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        Assert.assertEquals(ImportedEntityType.ROLE, esi.getEntityType());
        Assert.assertEquals(role.getId(), esi.getEntityId());
    }

    @Test
    public void testCreateExternalSystemImportFailed() {
        News news = testObjectsFactory.generateFlatEntityWithoutId(News.class);
        String idInExternalSystem = "id1";
        Assertions.assertThatThrownBy(() ->
                externalSystemImportService.createExternalSystemImport(news, idInExternalSystem))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
