package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserGroupType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserTypeCreateDTO {

    @NotNull
    private UserGroupType userGroup;

    private UUID portalUserId;
}
