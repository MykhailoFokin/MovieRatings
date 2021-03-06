package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserGroupType;

import java.util.UUID;

@Data
public class UserRoleReadDTO {

    private UUID id;

    private UserGroupType userGroupType;
}
