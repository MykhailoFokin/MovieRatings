package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonRepository;

import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public PersonReadDTO getPersons(UUID id) {
        Person person = getPersonsRequired(id);
        return toRead(person);
    }

    private PersonReadDTO toRead(Person person) {
        PersonReadDTO dto = new PersonReadDTO();
        dto.setId(person.getId());
        dto.setSurname(person.getSurname());
        dto.setName(person.getName());
        dto.setMiddleName(person.getMiddleName());
        return dto;
    }

    public PersonReadDTO createPersons(PersonCreateDTO create) {
        Person person = new Person();
        person.setSurname(create.getSurname());
        person.setName(create.getName());
        person.setMiddleName(create.getMiddleName());

        person = personRepository.save(person);
        return toRead(person);
    }

    public PersonReadDTO patchPersons(UUID id, PersonPatchDTO patch) {
        Person person = getPersonsRequired(id);

        if (patch.getName()!=null) {
            person.setName(patch.getName());
        }
        if (patch.getMiddleName()!=null) {
            person.setMiddleName(patch.getMiddleName());
        }
        if (patch.getSurname()!=null) {
            person.setSurname(patch.getSurname());
        }
        person = personRepository.save(person);
        return toRead(person);
    }

    private Person getPersonsRequired(UUID id) {
        return personRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Person.class, id);
        });
    }

    public void deletePersons(UUID id) {
        personRepository.delete(getPersonsRequired(id));
    }
}
