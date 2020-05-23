package solvve.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieVoteRepository;
import solvve.course.repository.PersonRepository;
import solvve.course.repository.RoleVoteRepository;

import java.util.UUID;

@Slf4j
@Service
public class PersonService extends AbstractService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Transactional(readOnly = true)
    public PersonReadDTO getPersons(UUID id) {
        Person person = repositoryHelper.getByIdRequired(Person.class, id);
        return translationService.translate(person, PersonReadDTO.class);
    }

    public PersonReadDTO createPersons(PersonCreateDTO create) {
        Person person = translationService.translate(create, Person.class);

        person.setAverageMovieRating(0.0);
        person.setAverageRoleRating(0.0);

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfMovieForPerson(UUID personId) {
        Double averageRating = movieVoteRepository.calcAverageMarkOfMovieForPerson(personId);
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new EntityNotFoundException(Person.class, personId));

        log.info("Setting new average rating of movies for Person: {}. Old value: {}, new value: {}", personId,
                person.getAverageMovieRating(), averageRating);
        person.setAverageMovieRating(averageRating);
        personRepository.save(person);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfRoleForPerson(UUID personId) {
        Double averageRating = roleVoteRepository.calcAverageMarkOfRoleForPerson(personId);
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new EntityNotFoundException(Person.class, personId));

        log.info("Setting new average rating of roles for Person: {}. Old value: {}, new value: {}", personId,
                person.getAverageRoleRating(), averageRating);
        person.setAverageRoleRating(averageRating);
        personRepository.save(person);
    }
}
