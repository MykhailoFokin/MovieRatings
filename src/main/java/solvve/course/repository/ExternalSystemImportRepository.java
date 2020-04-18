package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.ExternalSystemImport;
import solvve.course.domain.ImportedEntityType;

import java.util.UUID;

@Repository
public interface ExternalSystemImportRepository extends CrudRepository<ExternalSystemImport, UUID> {

    ExternalSystemImport findByIdInExternalSystemAndEntityType(String idInExternalSystem,
                                                               ImportedEntityType entityType);
}
