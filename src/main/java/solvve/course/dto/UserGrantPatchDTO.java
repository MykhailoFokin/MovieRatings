package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserPermType;

import java.util.UUID;

@Data
public class UserGrantPatchDTO {

    private UUID userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private UUID grantedById;
}
