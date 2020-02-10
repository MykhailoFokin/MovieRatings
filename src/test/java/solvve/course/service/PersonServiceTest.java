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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from person", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PersonServiceTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetPersons() {
        Person person = testObjectsFactory.createPerson();

        PersonReadDTO readDTO = personService.getPersons(person.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(person);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonsWrongId() {
        personService.getPersons(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreatePersons() {
        PersonCreateDTO create = new PersonCreateDTO();
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        PersonReadDTO read = personService.createPersons(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Person person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(person);
    }

    @Transactional
    @Test
    public void testPatchPersons() {
        Person person = testObjectsFactory.createPerson();

        PersonPatchDTO patch = new PersonPatchDTO();
        patch.setSurname("Surname");
        patch.setName("Name");
        patch.setMiddleName("MiddleName");
        PersonReadDTO read = personService.patchPersons(person.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(person).isEqualToIgnoringGivenFields(read,"crews","role");
    }

    @Transactional
    @Test
    public void testPatchPersonsEmptyPatch() {
        Person person = testObjectsFactory.createPerson();

        PersonPatchDTO patch = new PersonPatchDTO();
        PersonReadDTO read = personService.patchPersons(person.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getSurname());
        Assert.assertNotNull(read.getMiddleName());

        Person personAfterUpdate = personRepository.findById(read.getId()).get();

        Assert.assertNotNull(personAfterUpdate.getName());
        Assert.assertNotNull(personAfterUpdate.getSurname());
        Assert.assertNotNull(personAfterUpdate.getMiddleName());

        Assertions.assertThat(person).isEqualToComparingFieldByField(personAfterUpdate);
    }

    @Test
    public void testDeletePersons() {
        Person person = testObjectsFactory.createPerson();

        personService.deletePersons(person.getId());
        Assert.assertFalse(personRepository.existsById(person.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePersonsNotFound() {
        personService.deletePersons(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutPersons() {
        Person person = testObjectsFactory.createPerson();

        PersonPutDTO put = new PersonPutDTO();
        put.setSurname("Surname");
        put.setName("Name");
        put.setMiddleName("MiddleName");
        PersonReadDTO read = personService.updatePersons(person.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(person).isEqualToIgnoringGivenFields(read,"crews","role");
    }

    @Transactional
    @Test
    public void testPutPersonsEmptyPut() {
        Person person = testObjectsFactory.createPerson();

        PersonPutDTO put = new PersonPutDTO();
        PersonReadDTO read = personService.updatePersons(person.getId(), put);

        Assert.assertNull(read.getName());
        Assert.assertNull(read.getSurname());
        Assert.assertNull(read.getMiddleName());

        Person personAfterUpdate = personRepository.findById(read.getId()).get();

        Assert.assertNull(personAfterUpdate.getName());
        Assert.assertNull(personAfterUpdate.getSurname());
        Assert.assertNull(personAfterUpdate.getMiddleName());

        Assertions.assertThat(person).isEqualToComparingFieldByField(personAfterUpdate);
    }
}
