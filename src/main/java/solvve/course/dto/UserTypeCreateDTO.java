package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserGroupType;

import java.util.UUID;

@Data
public class UserTypeCreateDTO {

    private UserGroupType userGroup;

    private UUID portalUserId;
}
