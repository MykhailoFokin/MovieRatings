package solvve.course.controller.validation;

import lombok.experimental.UtilityClass;
import solvve.course.exception.ControllerValidationException;

@UtilityClass
public class ControllerValidationUtil {

    public static <T extends Comparable<T>> void validateLessThan(T value1, T value2,
                                                                   String field1Name, String field2Name) {
        if (value1.compareTo(value2) >= 0) {
            throw new ControllerValidationException(String.format("Field %s=%s should be less than %s=%s",
                    field1Name, value1, field2Name, value2));
        }
    }
}
