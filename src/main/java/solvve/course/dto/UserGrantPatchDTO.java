package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserPermType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class UserGrantPatchDTO {

    private UUID userTypeId;

    private UserPermType userPermission;

    @Size(min = 1, max = 255)
    private String objectName;

    private UUID grantedById;
}
