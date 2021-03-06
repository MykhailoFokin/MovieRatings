package solvve.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class entityClass, UUID id) {

        super(String.format("Entity %s with id=%s is not found!", entityClass.getSimpleName(), id));
    }

    public EntityNotFoundException(Class entityClass, UUID parentId, UUID id) {

        super(String.format("Entity %s with parentID=%s and id=%s is not found!",
                entityClass.getSimpleName(), parentId, id));
    }

    public EntityNotFoundException(Class entityClass, UUID parentId, UUID linkedId, UUID id) {

        super(String.format("Entity %s with parentID=%s and portalUserId=%s and id=%s is not found!",
                entityClass.getSimpleName(), parentId, linkedId, id));
    }

    public EntityNotFoundException(String errorMessage) {

        super(errorMessage);
    }
}
