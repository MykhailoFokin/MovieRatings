package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.domain.Role;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonRepository;
import solvve.course.repository.RoleRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @Before
    public void setup() {
        if (person == null) {
            person = new Person();
            person.setName("Name");
            person = personRepository.save(person);
        }
    }

    private Role createRole() {
        Role role = new Role();
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");
        role.setPersonId(person);
        return roleRepository.save(role);
    }

    @Transactional
    @Test
    public void testGetRole() {
        Role role = createRole();

        RoleReadDTO readDTO = roleService.getRole(role.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(role,"personId");
        Assertions.assertThat(readDTO.getPersonId()).isEqualTo(role.getPersonId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleWrongId() {
        roleService.getRole(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRole() {
        RoleCreateDTO create = new RoleCreateDTO();
        create.setTitle("Actor");
        create.setRoleType("Main_Role");
        create.setDescription("Description test");
        create.setPersonId(person.getId());
        RoleReadDTO read = roleService.createRole(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Role role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(role,"personId");
        Assertions.assertThat(read.getPersonId()).isEqualTo(role.getPersonId().getId());
    }

    @Transactional
    @Test
    public void testPatchRole() {
        Role role = createRole();

        RolePatchDTO patch = new RolePatchDTO();
        patch.setTitle("Actor");
        patch.setRoleType("Main_Role");
        patch.setDescription("Description test");
        patch.setPersonId(person.getId());
        RoleReadDTO read = roleService.patchRole(role.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(role).isEqualToIgnoringGivenFields(read,
                "personId", "roleReviewSet",
                "roleReviewCompliants","roleReviewFeedbacks","roleVotes");
        Assertions.assertThat(role.getPersonId().getId()).isEqualTo(read.getPersonId());
    }

    @Transactional
    @Test
    public void testPatchRoleEmptyPatch() {
        Role role = createRole();

        RolePatchDTO patch = new RolePatchDTO();
        RoleReadDTO read = roleService.patchRole(role.getId(), patch);

        Assert.assertNotNull(read.getTitle());
        Assert.assertNotNull(read.getRoleType());
        Assert.assertNotNull(read.getDescription());

        Role roleAfterUpdate = roleRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleAfterUpdate.getTitle());
        Assert.assertNotNull(roleAfterUpdate.getRoleType());
        Assert.assertNotNull(roleAfterUpdate.getDescription());

        Assertions.assertThat(role).isEqualToComparingFieldByField(roleAfterUpdate);
    }

    @Test
    public void testDeleteRole() {
        Role role = createRole();

        roleService.deleteRole(role.getId());
        Assert.assertFalse(roleRepository.existsById(role.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleNotFound() {
        roleService.deleteRole(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRole() {
        Role role = createRole();

        RolePutDTO put = new RolePutDTO();
        put.setTitle("Actor");
        put.setRoleType("Main_Role");
        put.setDescription("Description test");
        put.setPersonId(person.getId());
        RoleReadDTO read = roleService.putRole(role.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(role).isEqualToIgnoringGivenFields(read,
                "personId", "roleReviewSet",
                "roleReviewCompliants","roleReviewFeedbacks","roleVotes");
        Assertions.assertThat(role.getPersonId().getId()).isEqualTo(read.getPersonId());
    }

    @Transactional
    @Test
    public void testPutRoleEmptyPut() {
        Role role = createRole();

        RolePutDTO put = new RolePutDTO();
        RoleReadDTO read = roleService.putRole(role.getId(), put);

        Assert.assertNull(read.getTitle());
        Assert.assertNull(read.getRoleType());
        Assert.assertNull(read.getDescription());

        Role roleAfterUpdate = roleRepository.findById(read.getId()).get();

        Assert.assertNull(roleAfterUpdate.getTitle());
        Assert.assertNull(roleAfterUpdate.getRoleType());
        Assert.assertNull(roleAfterUpdate.getDescription());

        Assertions.assertThat(role).isEqualToComparingFieldByField(roleAfterUpdate);
    }
}
