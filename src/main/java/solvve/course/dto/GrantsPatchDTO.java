package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserPermType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.UUID;

@Data
public class GrantsPatchDTO implements Serializable {

    private UserTypes userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private PortalUser grantedBy;
}
