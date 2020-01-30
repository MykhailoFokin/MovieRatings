package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;
import java.util.UUID;

@Data
public class PortalUserReadDTO {

    private UUID id;

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UUID userType;

    private UserConfidenceType userConfidence;
}
