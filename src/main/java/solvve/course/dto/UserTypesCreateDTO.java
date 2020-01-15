package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Grants;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
public class UserTypesCreateDTO implements Serializable {

    private UserGroupType userGroup;

    private Set<Grants> grants;
}
