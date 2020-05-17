package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Gender;
import solvve.course.domain.UserConfidenceType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class PortalUserReadDTO {

    private UUID id;

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UUID userTypeId;

    private UserConfidenceType userConfidence;

    private Instant createdAt;

    private Instant updatedAt;

    private String email;

    private String encodedPassword;

    private Gender gender;

    private List<UUID> userRoleIds;
}
