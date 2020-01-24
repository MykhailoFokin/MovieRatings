package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserGrant;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGroupType;

import java.util.Set;

@Data
public class UserTypePatchDTO {

    private UserGroupType userGroup;

    private Set<UserGrant> userGrants;

    private PortalUser portalUser;
}
