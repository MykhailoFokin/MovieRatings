package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserPermType;
import solvve.course.domain.UserType;

@Data
public class GrantPatchDTO {

    private UserType userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private PortalUser grantedBy;
}
