package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Grants;
import solvve.course.domain.UserGroupType;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserTypesPatchDTO implements Serializable {

    private UserGroupType userGroup;

    private Set<Grants> grants;
}
