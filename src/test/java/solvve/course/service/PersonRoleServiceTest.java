package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.domain.Role;
import solvve.course.dto.RoleReadDTO;

import java.util.List;

public class PersonRoleServiceTest extends BaseTest {

    @Autowired
    private PersonRoleService personRoleService;

    @Test
    public void testGetRolesByPerson() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role1 = testObjectsFactory.createRole(person, movie);
        Role role2 = testObjectsFactory.createRole(person, movie);

        List<RoleReadDTO> readDTO = personRoleService.getRolesByPerson(person.getId());
        Assertions.assertThat(readDTO).extracting("id")
                .containsExactlyInAnyOrder(role1.getId(), role2.getId());
    }
}
