package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Persons;
import solvve.course.dto.PersonsCreateDTO;
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
        Persons persons = personsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Persons.class, id);
        });
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
}
