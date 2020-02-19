package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Person;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from person", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Person r = new Person();
        r = personRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(personRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Person entity = testObjectsFactory.createPerson();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = personRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        Person entity = testObjectsFactory.createPerson();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = personRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        Person entity = testObjectsFactory.createPerson();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setName("NewNameTest");
        personRepository.save(entity);
        entity = personRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
