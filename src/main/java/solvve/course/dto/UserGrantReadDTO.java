package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserPermType;
import solvve.course.domain.UserType;

import java.util.UUID;

@Data
public class UserGrantReadDTO {

    private UUID id;

    private UserType userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private PortalUser grantedBy;
}
