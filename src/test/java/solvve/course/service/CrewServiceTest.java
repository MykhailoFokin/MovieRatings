package solvve.course.service;

import org.junit.Assert;
import org.junit.Before;
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
import solvve.course.repository.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from crew; delete from movie; delete from crew_type; delete from person;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewServiceTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Crew createCrew(Person person, CrewType crewType, Movie movie) {
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId(person);
        crew.setCrewType(crewType);
        crew.setMovieId(movie);
        crew.setDescription("Description");
        return crewRepository.save(crew);
    }

    private CrewType createCrewType() {
        CrewType crewType = new CrewType();
        crewType.setName("Director");
        crewType = crewTypeRepository.save(crewType);
        return  crewType;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setSurname("Surname");
        person.setName("Name");
        person.setMiddleName("MiddleName");
        person = personRepository.save(person);
        return person;
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setGenres("Comedy");
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCompanies("Paramount");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setFilmingLocations("USA");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setLanguages("English");
        movie.setSoundMix("DolbySurround");
        movie = movieRepository.save(movie);
        return movie;
    }

    @Transactional
    @Test
    public void testGetCrew() {
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();
        Crew crew = createCrew(person, crewType, movie);

        CrewReadExtendedDTO readDTO = crewService.getCrew(crew.getId());
        //Assertions.assertThat(readDTO).isEqualToComparingFieldByField(crew);
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
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();

        CrewCreateDTO create = new CrewCreateDTO();
        create.setPersonId(person);
        create.setCrewType(crewType);
        create.setMovieId(movie);
        create.setDescription("Description");
        CrewReadExtendedDTO read = crewService.createCrew(create);
        //Assertions.assertThat(create).isEqualToComparingFieldByField(read);
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(create.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(create.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(create.getCrewType()).isEqualToIgnoringGivenFields(crewType);

        Crew crew = crewRepository.findById(read.getId()).get();
        //Assertions.assertThat(read).isEqualToComparingFieldByField(crew);
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(crew, "movieId", "personId","crewType");
        Assertions.assertThat(read.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(read.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(read.getCrewType()).isEqualToIgnoringGivenFields(crewType);
    }

    @Transactional
    @Test
    public void testPatchCrew() {
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();
        Crew crew = createCrew(person, crewType, movie);

        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setPersonId(person);
        patch.setCrewType(crewType);
        patch.setMovieId(movie);
        patch.setDescription("Description");
        CrewReadExtendedDTO read = crewService.patchCrew(crew.getId(), patch);

        //Assertions.assertThat(patch).isEqualToComparingFieldByField(read);
        Assertions.assertThat(patch).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(patch.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(patch.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(patch.getCrewType()).isEqualToIgnoringGivenFields(crewType);

        crew = crewRepository.findById(read.getId()).get();
        //Assertions.assertThat(crew).isEqualToComparingFieldByField(read);
        Assertions.assertThat(crew).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(crew.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(crew.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(crew.getCrewType()).isEqualToIgnoringGivenFields(crewType);
    }

    @Transactional
    @Test
    public void testPatchCrewEmptyPatch() {
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();
        Crew crew = createCrew(person, crewType, movie);

        CrewPatchDTO patch = new CrewPatchDTO();
        CrewReadExtendedDTO read = crewService.patchCrew(crew.getId(), patch);

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
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();
        Crew crew = createCrew(person, crewType, movie);

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
        Movie movie = createMovie();
        Person person = createPerson();
        CrewType crewType = createCrewType();
        Crew crew = createCrew(person, crewType, movie);

        CrewPutDTO put = new CrewPutDTO();
        put.setPersonId(person);
        put.setCrewType(crewType);
        put.setMovieId(movie);
        put.setDescription("Description");
        CrewReadExtendedDTO read = crewService.putCrew(crew.getId(), put);

        //Assertions.assertThat(put).isEqualToComparingFieldByField(read);
        Assertions.assertThat(put).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(put.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(put.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(put.getCrewType()).isEqualToIgnoringGivenFields(crewType);

        crew = crewRepository.findById(read.getId()).get();
        //Assertions.assertThat(crew).isEqualToComparingFieldByField(read);
        Assertions.assertThat(crew).isEqualToIgnoringGivenFields(read, "movieId", "personId","crewType");
        Assertions.assertThat(crew.getMovieId()).isEqualToIgnoringGivenFields(movie);
        Assertions.assertThat(crew.getPersonId()).isEqualToIgnoringGivenFields(person);
        Assertions.assertThat(crew.getCrewType()).isEqualToIgnoringGivenFields(crewType);
    }
}
