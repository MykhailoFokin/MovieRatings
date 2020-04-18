package solvve.course.service.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.*;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.repository.ExternalSystemImportRepository;

import java.util.UUID;

@Service
public class ExternalSystemImportService {

    @Autowired
    private ExternalSystemImportRepository externalSystemImportRepository;

    public void validateNotImported(Class<? extends AbstractEntity> entityToImport,
                                    String idInExternalSystem) throws ImportAlreadyPerformedException {
        ImportedEntityType importedEntityType = getImportedEntityType(entityToImport);
        ExternalSystemImport esi =
                externalSystemImportRepository.findByIdInExternalSystemAndEntityType(idInExternalSystem,
                        importedEntityType);

        if (esi != null) {
            throw new ImportAlreadyPerformedException(esi);
        }
    }

    public <T extends AbstractEntity> UUID createExternalSystemImport(T entity, String idInExternalSystem) {

        ImportedEntityType importedEntityType = getImportedEntityType(entity.getClass());
        ExternalSystemImport esi = new ExternalSystemImport();
        esi.setEntityType(importedEntityType);
        esi.setEntityId(entity.getId());
        esi.setIdInExternalSystem(idInExternalSystem);
        esi = externalSystemImportRepository.save(esi);
        return esi.getId();
    }

    private ImportedEntityType getImportedEntityType(Class<? extends AbstractEntity> entityToImport) {

        if (Movie.class.equals(entityToImport)) {
            return ImportedEntityType.MOVIE;
        } else if (Person.class.equals(entityToImport)) {
            return ImportedEntityType.PERSON;
        } else if (Crew.class.equals(entityToImport)) {
            return ImportedEntityType.CREW;
        } else if (CrewType.class.equals(entityToImport)) {
            return ImportedEntityType.CREW_TYPE;
        } else if (Role.class.equals(entityToImport)) {
            return ImportedEntityType.ROLE;
        }

        throw new IllegalArgumentException("Importing of entities " + entityToImport + " is not supported");
    }
}
