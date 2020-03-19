package solvve.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(Class entityClass, UUID id) {

        super(String.format("Entity %s with id=%s cannot be fixed! Please reload and take a look on the data!",
                entityClass.getSimpleName(),
                id));
    }

}
