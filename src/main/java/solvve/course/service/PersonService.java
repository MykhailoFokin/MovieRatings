package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PersonRepository;

import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public PersonReadDTO getPersons(UUID id) {
        Person person = getPersonsRequired(id);
        return translationService.toRead(person);
    }

    public PersonReadDTO createPersons(PersonCreateDTO create) {
        Person person = translationService.toEntity(create);

        person = personRepository.save(person);
        return translationService.toRead(person);
    }

    public PersonReadDTO patchPersons(UUID id, PersonPatchDTO patch) {
        Person person = getPersonsRequired(id);

        translationService.patchEntity(patch, person);

        person = personRepository.save(person);
        return translationService.toRead(person);
    }

    public void deletePersons(UUID id) {
        personRepository.delete(getPersonsRequired(id));
    }

    public PersonReadDTO putPersons(UUID id, PersonPutDTO put) {
        Person person = getPersonsRequired(id);

        translationService.putEntity(put, person);

        person = personRepository.save(person);
        return translationService.toRead(person);
    }

    private Person getPersonsRequired(UUID id) {
        return personRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Person.class, id);
        });
    }
}
