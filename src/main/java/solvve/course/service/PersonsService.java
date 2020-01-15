package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Persons;
import solvve.course.dto.PersonsCreateDTO;
import solvve.course.dto.PersonsPatchDTO;
import solvve.course.dto.PersonsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonsRepository;

import java.util.UUID;

@Service
public class PersonsService {

    @Autowired
    private PersonsRepository personsRepository;

    @Transactional(readOnly = true)
    public PersonsReadDTO getPersons(UUID id) {
        Persons persons = getPersonsRequired(id);
        return toRead(persons);
    }

    private PersonsReadDTO toRead(Persons persons) {
        PersonsReadDTO dto = new PersonsReadDTO();
        dto.setId(persons.getId());
        dto.setSurname(persons.getSurname());
        dto.setName(persons.getName());
        dto.setMiddleName(persons.getMiddleName());
        return dto;
    }

    public PersonsReadDTO createPersons(PersonsCreateDTO create) {
        Persons persons = new Persons();
        persons.setSurname(create.getSurname());
        persons.setName(create.getName());
        persons.setMiddleName(create.getMiddleName());

        persons = personsRepository.save(persons);
        return toRead(persons);
    }

    public PersonsReadDTO patchPersons(UUID id, PersonsPatchDTO patch) {
        Persons persons = getPersonsRequired(id);

        if (patch.getName()!=null) {
            persons.setName(patch.getName());
        }
        if (patch.getMiddleName()!=null) {
            persons.setMiddleName(patch.getMiddleName());
        }
        if (patch.getSurname()!=null) {
            persons.setSurname(patch.getSurname());
        }
        persons = personsRepository.save(persons);
        return toRead(persons);
    }

    private Persons getPersonsRequired(UUID id) {
        return personsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Persons.class, id);
        });
    }

    public void deletePersons(UUID id) {
        personsRepository.delete(getPersonsRequired(id));
    }
}
