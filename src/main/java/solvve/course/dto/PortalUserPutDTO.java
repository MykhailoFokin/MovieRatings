package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserConfidenceType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class PortalUserPutDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    @Size(min = 1, max = 255)
    private String surname;

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String middleName;

    private UUID userTypeId;

    private UserConfidenceType userConfidence;

    private String email;

    private String encodedPassword;
}
