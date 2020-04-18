package solvve.course.exception;

import lombok.Getter;
import solvve.course.domain.AbstractEntity;

import java.util.UUID;

@Getter
public class ImportedEntityAlreadyExistException extends Exception {

    private Class<? extends AbstractEntity> entityClass;

    private UUID entityId;

    public ImportedEntityAlreadyExistException(Class<? extends AbstractEntity> entityClass, UUID entityId,
                                               String message) {
        super(message);
        this.entityClass = entityClass;
        this.entityId = entityId;
    }
}
