package solvve.course.repository;

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
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from role","delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role r = testObjectsFactory.createRole(person,movie);
        assertNotNull(r.getId());
        assertTrue(roleRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = roleRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role entity = testObjectsFactory.createRole(person,movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        roleRepository.save(entity);
        entity = roleRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
