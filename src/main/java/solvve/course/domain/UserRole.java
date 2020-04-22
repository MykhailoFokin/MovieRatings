package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class UserRole extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private UserGroupType type;

    @ManyToMany(mappedBy = "userRoles")
    private List<PortalUser> users = new ArrayList<>();
}
