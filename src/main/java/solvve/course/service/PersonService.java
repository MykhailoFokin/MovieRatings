package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.repository.PersonRepository;

import java.util.UUID;

@Service
public class PersonService extends AbstractService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public PersonReadDTO getPersons(UUID id) {
        Person person = repositoryHelper.getByIdRequired(Person.class, id);
        return translationService.translate(person, PersonReadDTO.class);
    }

    public PersonReadDTO createPersons(PersonCreateDTO create) {
        Person person = translationService.translate(create, Person.class);

        person = personRepository.save(person);
        return translationService.translate(person, PersonReadDTO.class);
    }

    public PersonReadDTO patchPersons(UUID id, PersonPatchDTO patch) {
        Person person = repositoryHelper.getByIdRequired(Person.class, id);

        translationService.map(patch, person);

        person = personRepository.save(person);
        return translationService.translate(person, PersonReadDTO.class);
    }

    public void deletePersons(UUID id) {
        personRepository.delete(repositoryHelper.getByIdRequired(Person.class, id));
    }

    public PersonReadDTO updatePersons(UUID id, PersonPutDTO put) {
        Person person = repositoryHelper.getByIdRequired(Person.class, id);

        translationService.updateEntity(put, person);

        person = personRepository.save(person);
        return translationService.translate(person, PersonReadDTO.class);
    }
}
