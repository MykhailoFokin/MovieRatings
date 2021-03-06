package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Gender;
import solvve.course.domain.UserConfidenceType;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PortalUserPatchDTO {

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

    private List<UUID> userRoleIds = new ArrayList<>();

    private Gender gender;
}
