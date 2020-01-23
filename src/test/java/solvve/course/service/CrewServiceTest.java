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
import solvve.course.domain.Persons;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from crew; delete from movie; delete from crew_type; delete from persons;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewServiceTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private PersonsService personsService;

    private Persons persons;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    private CrewType crewType;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    private Movie movie;

    private Crew createCrew() {
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId(persons);
        crew.setCrewType(crewType);
        crew.setMovieId(movie);
        crew.setDescription("Description");
        return crewRepository.save(crew);
    }

    @Before
    public void setup() {
        if (persons==null) {
            persons = new Persons();
            persons.setSurname("Surname");
            persons.setName("Name");
            persons.setMiddleName("MiddleName");
            persons = personsRepository.save(persons);
        }

        if (crewType==null) {
            crewType = new CrewType();
            crewType.setName("Director");
            crewType = crewTypeRepository.save(crewType);
        }

        if (movie==null) {
            movie = new Movie();
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
        }
    }

    @Transactional
    @Test
    public void testGetCrew() {
        Crew crew = createCrew();

        CrewReadDTO readDTO = crewService.getCrew(crew.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(crew);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCrewWrongId() {
        crewService.getCrew(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateCrew() {
        CrewCreateDTO create = new CrewCreateDTO();
        create.setPersonId(persons);
        create.setCrewType(crewType);
        create.setMovieId(movie);
        create.setDescription("Description");
        CrewReadDTO read = crewService.createCrew(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Crew crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(crew);
    }

    @Transactional
    @Test
    public void testPatchCrew() {
        Crew crew = createCrew();

        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setPersonId(persons);
        patch.setCrewType(crewType);
        patch.setMovieId(movie);
        patch.setDescription("Description");
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(crew).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchCrewEmptyPatch() {
        Crew crew = createCrew();

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
        Crew crew = createCrew();

        crewService.deleteCrew(crew.getId());
        Assert.assertFalse(crewRepository.existsById(crew.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCrewNotFound() {
        crewService.deleteCrew(UUID.randomUUID());
    }
}
