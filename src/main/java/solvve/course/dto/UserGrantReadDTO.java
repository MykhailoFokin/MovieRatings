package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserPermType;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserGrantReadDTO {

    private UUID id;

    private UUID userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private UUID grantedBy;

    private Instant createdAt;

    private Instant updatedAt;
}
