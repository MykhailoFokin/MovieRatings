package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserGroupType;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypeReadDTO {

    private UUID id;

    private UserGroupType userGroup;

    private UUID portalUserId;

    private Instant createdAt;

    private Instant updatedAt;
}
