package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Grant;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGroupType;

import java.util.Set;

@Data
public class UserTypeCreateDTO {

    private UserGroupType userGroup;

    private Set<Grant> grants;

    private PortalUser portalUser;
}
