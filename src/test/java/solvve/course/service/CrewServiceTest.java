package solvve.course.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.assertj.core.api.Assertions;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Crew;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewRepository;
import solvve.course.utils.TestObjectsFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from crew",
        "delete from movie",
        "delete from crew_type",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewServiceTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewReadExtendedDTO readDTO = crewService.getCrew(crew.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(crew, "movieId", "personId","crewType");
        Assertions.assertThat(readDTO.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(readDTO.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(readDTO.getCrewType()).isEqualToIgnoringGivenFields(crewType);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCrewWrongId() {
        crewService.getCrew(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewCreateDTO create = new CrewCreateDTO();
        create.setPersonId(person.getId());
        create.setCrewType(crewType.getId());
        create.setMovieId(movie.getId());
        create.setDescription("Description");
        CrewReadDTO read = crewService.createCrew(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Crew crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(crew, "movieId", "personId","crewType");
        Assertions.assertThat(read.getMovieId()).isEqualToIgnoringGivenFields(crew.getMovieId().getId());
        Assertions.assertThat(read.getPersonId()).isEqualToIgnoringGivenFields(crew.getPersonId().getId());
        Assertions.assertThat(read.getCrewType()).isEqualToIgnoringGivenFields(crew.getCrewType().getId());
    }

    @Transactional
    @Test
    public void testPatchCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setPersonId(person.getId());
        patch.setCrewType(crewType.getId());
        patch.setMovieId(movie.getId());
        patch.setDescription("Description");
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(crew).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(crew.getMovieId().getId()).isEqualToIgnoringGivenFields(read.getMovieId());
        Assertions.assertThat(crew.getPersonId().getId()).isEqualToIgnoringGivenFields(read.getPersonId());
        Assertions.assertThat(crew.getCrewType().getId()).isEqualToIgnoringGivenFields(read.getCrewType());
    }

    @Transactional
    @Test
    public void testPatchCrewEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPatchDTO patch = new CrewPatchDTO();
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assert.assertNotNull(read.getPersonId());
        Assert.assertNotNull(read.getCrewType());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getDescription());

        Crew crewAfterUpdate = crewRepository.findById(read.getId()).get();

        Assert.assertNotNull(crewAfterUpdate.getPersonId());
        Assert.assertNotNull(crewAfterUpdate.getCrewType());
        Assert.assertNotNull(crewAfterUpdate.getMovieId());
        Assert.assertNotNull(crewAfterUpdate.getDescription());

        Assertions.assertThat(crew).isEqualToComparingFieldByField(crewAfterUpdate);
    }

    @Test
    public void testDeleteCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        crewService.deleteCrew(crew.getId());
        Assert.assertFalse(crewRepository.existsById(crew.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCrewNotFound() {
        crewService.deleteCrew(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPutDTO put = new CrewPutDTO();
        put.setPersonId(person.getId());
        put.setCrewType(crewType.getId());
        put.setMovieId(movie.getId());
        put.setDescription("Description");
        CrewReadDTO read = crewService.putCrew(crew.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(crew).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(crew.getMovieId().getId()).isEqualToIgnoringGivenFields(read.getMovieId());
        Assertions.assertThat(crew.getPersonId().getId()).isEqualToIgnoringGivenFields(read.getPersonId());
        Assertions.assertThat(crew.getCrewType().getId()).isEqualToIgnoringGivenFields(read.getCrewType());
    }

    @Transactional
    @Test
    public void testPutCrewEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPutDTO put = new CrewPutDTO();
        CrewReadDTO read = crewService.putCrew(crew.getId(), put);

        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getPersonId());
        Assert.assertNull(read.getCrewType());
        Assert.assertNull(read.getDescription());

        Crew crewAfterUpdate = crewRepository.findById(read.getId()).get();

        Assert.assertNotNull(crewAfterUpdate.getMovieId());
        Assert.assertNotNull(crewAfterUpdate.getPersonId());
        Assert.assertNull(crewAfterUpdate.getCrewType().getId());
        Assert.assertNull(crewAfterUpdate.getDescription());

        Assertions.assertThat(crew).isEqualToComparingFieldByField(crewAfterUpdate);
    }
}
