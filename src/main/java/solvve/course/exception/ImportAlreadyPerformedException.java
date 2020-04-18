package solvve.course.exception;

import lombok.Getter;
import solvve.course.domain.ExternalSystemImport;

@Getter
public class ImportAlreadyPerformedException extends Exception {

    private ExternalSystemImport externalSystemImport;

    public ImportAlreadyPerformedException(ExternalSystemImport esi) {
        super(String.format("Already performed import of %s with id=%s and id in external system = %s",
                esi.getEntityType(), esi.getEntityId(), esi.getIdInExternalSystem()));
        this.externalSystemImport = esi;
    }
}
