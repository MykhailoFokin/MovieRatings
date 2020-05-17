package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class UserRole extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroupType;

    @ManyToMany(mappedBy = "userRoles", cascade = CascadeType.PERSIST)
    private List<PortalUser> users = new ArrayList<>();
}
