package solvve.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class LinkageCorruptedEntityException extends RuntimeException {

    public LinkageCorruptedEntityException(Class entityClass, UUID id) {

        super(String.format("Entity %s with id=%s cannot be fixed! It has corrupted linkage to other entities!",
                entityClass.getSimpleName(),
                id));
    }

}
