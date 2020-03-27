package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserPermType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class UserGrantPutDTO {

    @NotNull
    private UUID userTypeId;

    @NotNull
    private UserPermType userPermission;

    @NotNull
    @Size(min = 1, max = 255)
    private String objectName;

    private UUID grantedById;
}
