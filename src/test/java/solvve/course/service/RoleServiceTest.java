package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.domain.Role;
import solvve.course.domain.RoleType;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleRepository;
import solvve.course.utils.TestObjectsFactory;

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
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RoleReadDTO readDTO = roleService.getRole(role.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(role,"personId","movieId");
        Assertions.assertThat(readDTO.getPersonId()).isEqualTo(role.getPerson().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(role.getMovie().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleWrongId() {
        roleService.getRole(UUID.randomUUID());
    }

    @Test
    public void testCreateRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();

        RoleCreateDTO create = new RoleCreateDTO();
        create.setTitle("Actor");
        create.setRoleType(RoleType.LEAD);
        create.setDescription("Description test");
        create.setPersonId(person.getId());
        create.setMovieId(movie.getId());
        RoleReadDTO read = roleService.createRole(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Role role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(role,"personId","movieId");
        Assertions.assertThat(read.getPersonId()).isEqualTo(role.getPerson().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(role.getMovie().getId());
    }

    @Test
    public void testPatchRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);

        RolePatchDTO patch = new RolePatchDTO();
        patch.setTitle("Actor");
        patch.setRoleType(RoleType.LEAD);
        patch.setDescription("Description test");
        patch.setPersonId(person.getId());
        patch.setMovieId(movie.getId());
        RoleReadDTO read = roleService.patchRole(role.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(role).isEqualToIgnoringGivenFields(read,
                "movie","person", "roleReviews",
                "roleReviewCompliants","roleReviewFeedbacks","roleVotes");
        Assertions.assertThat(role.getPerson().getId()).isEqualTo(read.getPersonId());
    }

    @Test
    public void testPatchRoleEmptyPatch() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RolePatchDTO patch = new RolePatchDTO();
        RoleReadDTO read = roleService.patchRole(role.getId(), patch);

        Assert.assertNotNull(read.getTitle());
        Assert.assertNotNull(read.getRoleType());
        Assert.assertNotNull(read.getDescription());

        Role roleAfterUpdate = roleRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleAfterUpdate.getTitle());
        Assert.assertNotNull(roleAfterUpdate.getRoleType());
        Assert.assertNotNull(roleAfterUpdate.getDescription());

        Assertions.assertThat(role).isEqualToIgnoringGivenFields(roleAfterUpdate,
                "movie","person", "roleReviews",
                "roleReviewCompliants","roleReviewFeedbacks","roleVotes");
    }

    @Test
    public void testDeleteRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);

        roleService.deleteRole(role.getId());
        Assert.assertFalse(roleRepository.existsById(role.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleNotFound() {
        roleService.deleteRole(UUID.randomUUID());
    }

    @Test
    public void testPutRole() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);

        RolePutDTO put = new RolePutDTO();
        put.setTitle("Actor");
        put.setRoleType(RoleType.LEAD);
        put.setDescription("Description test");
        put.setPersonId(person.getId());
        put.setMovieId(movie.getId());
        RoleReadDTO read = roleService.updateRole(role.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        role = roleRepository.findById(read.getId()).get();
        Assertions.assertThat(role).isEqualToIgnoringGivenFields(read,
                "movie","person", "roleReviews",
                "roleReviewCompliants","roleReviewFeedbacks","roleVotes");
        Assertions.assertThat(role.getPerson().getId()).isEqualTo(read.getPersonId());
    }

    @Test
    public void testPutRoleEmptyPut() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);

        RolePutDTO put = new RolePutDTO();
        RoleReadDTO read = roleService.updateRole(role.getId(), put);

        Assert.assertNull(read.getTitle());
        Assert.assertNull(read.getRoleType());
        Assert.assertNull(read.getDescription());

        testObjectsFactory.inTransaction(() -> {
            Role roleAfterUpdate = roleRepository.findById(read.getId()).get();

            Assert.assertNull(roleAfterUpdate.getTitle());
            Assert.assertNull(roleAfterUpdate.getRoleType());
            Assert.assertNull(roleAfterUpdate.getDescription());

            Assertions.assertThat(role).isEqualToComparingOnlyGivenFields(roleAfterUpdate,"id");
        });
    }
}
