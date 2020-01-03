package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Persons;
import solvve.course.dto.PersonsCreateDTO;
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

    @Test
    public void testGetPersons() {
        Persons persons = new Persons();
        persons.setId(UUID.randomUUID());
        persons.setSurname("Surname");
        persons.setName("Name");
        persons.setMiddleName("MiddleName");
        persons = personsRepository.save(persons);

        PersonsReadDTO readDTO = personsService.getPersons(persons.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(persons);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonsWrongId() {
        personsService.getPersons(UUID.randomUUID());
    }

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
}
