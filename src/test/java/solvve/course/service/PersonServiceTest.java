package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonRepository;

import java.util.UUID;

public class PersonServiceTest extends BaseTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

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

    @Test
    public void testCreatePersons() {
        PersonCreateDTO create = testObjectsFactory.createPersonCreateDTO();
        PersonReadDTO read = personService.createPersons(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Person person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(person);
    }

    @Test
    public void testPatchPersons() {
        Person person = testObjectsFactory.createPerson();

        PersonPatchDTO patch = testObjectsFactory.createPersonPatchDTO();
        PersonReadDTO read = personService.patchPersons(person.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(person).isEqualToIgnoringGivenFields(read,"crews","roles");
    }

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

        Assertions.assertThat(person).isEqualToIgnoringGivenFields(personAfterUpdate,"crews", "roles");
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

    @Test
    public void testPutPersons() {
        Person person = testObjectsFactory.createPerson();

        PersonPutDTO put = testObjectsFactory.createPersonPutDTO();
        PersonReadDTO read = personService.updatePersons(person.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        person = personRepository.findById(read.getId()).get();
        Assertions.assertThat(person).isEqualToIgnoringGivenFields(read,"crews","roles");
    }

    @Test
    public void testPutPersonsEmptyPut() {
        Person person = testObjectsFactory.createPerson();

        PersonPutDTO put = new PersonPutDTO();
        PersonReadDTO read = personService.updatePersons(person.getId(), put);

        Assert.assertNotNull(read.getName());
        Assert.assertNull(read.getSurname());
        Assert.assertNull(read.getMiddleName());

        testObjectsFactory.inTransaction(() -> {
            Person personAfterUpdate = personRepository.findById(read.getId()).get();

            Assert.assertNotNull(personAfterUpdate.getName());
            Assert.assertNull(personAfterUpdate.getSurname());
            Assert.assertNull(personAfterUpdate.getMiddleName());

            Assertions.assertThat(person).isEqualToComparingOnlyGivenFields(personAfterUpdate, "id");
        });
    }
}
