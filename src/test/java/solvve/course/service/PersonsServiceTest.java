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
import solvve.course.domain.Persons;
import solvve.course.dto.PersonsCreateDTO;
import solvve.course.dto.PersonsPatchDTO;
import solvve.course.dto.PersonsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonsRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from persons", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PersonsServiceTest {

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private PersonsService personsService;

    private Persons createPersons() {
        Persons persons = new Persons();
        persons.setSurname("Surname");
        persons.setName("Name");
        persons.setMiddleName("MiddleName");
        return personsRepository.save(persons);
    }

    @Test
    public void testGetPersons() {
        Persons persons = createPersons();

        PersonsReadDTO readDTO = personsService.getPersons(persons.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(persons);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonsWrongId() {
        personsService.getPersons(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreatePersons() {
        PersonsCreateDTO create = new PersonsCreateDTO();
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        PersonsReadDTO read = personsService.createPersons(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Persons persons = personsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(persons);
    }

    @Transactional
    @Test
    public void testPatchPersons() {
        Persons persons = createPersons();

        PersonsPatchDTO patch = new PersonsPatchDTO();
        patch.setSurname("Surname");
        patch.setName("Name");
        patch.setMiddleName("MiddleName");
        PersonsReadDTO read = personsService.patchPersons(persons.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        persons = personsRepository.findById(read.getId()).get();
        Assertions.assertThat(persons).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchPersonsEmptyPatch() {
        Persons persons = createPersons();

        PersonsPatchDTO patch = new PersonsPatchDTO();
        PersonsReadDTO read = personsService.patchPersons(persons.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getSurname());
        Assert.assertNotNull(read.getMiddleName());

        Persons personsAfterUpdate = personsRepository.findById(read.getId()).get();

        Assert.assertNotNull(personsAfterUpdate.getName());
        Assert.assertNotNull(personsAfterUpdate.getSurname());
        Assert.assertNotNull(personsAfterUpdate.getMiddleName());

        Assertions.assertThat(persons).isEqualToComparingFieldByField(personsAfterUpdate);
    }

    @Test
    public void testDeletePersons() {
        Persons persons = createPersons();

        personsService.deletePersons(persons.getId());
        Assert.assertFalse(personsRepository.existsById(persons.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePersonsNotFound() {
        personsService.deletePersons(UUID.randomUUID());
    }
}
