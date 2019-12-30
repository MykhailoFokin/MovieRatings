package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Role;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from role", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void testGetRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");
        role = roleRepository.save(role);

        RoleReadDTO readDTO = roleService.getRole(role.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(role);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleWrongId() {
        roleService.getRole(UUID.randomUUID());
    }
}
