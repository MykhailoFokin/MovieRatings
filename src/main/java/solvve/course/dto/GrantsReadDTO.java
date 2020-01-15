package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Grants;
import solvve.course.domain.UserPermType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.UUID;

@Data
public class GrantsReadDTO implements Serializable {

    private UUID id;

    private UserTypes userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private UUID grantedBy;
}
