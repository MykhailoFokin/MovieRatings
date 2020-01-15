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
@Sql(statements = "delete from crew", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewServiceTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private PersonsService personsService;

    private PersonsReadDTO personsReadDTO;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    private CrewTypeReadDTO crewTypeReadDTO;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    private MovieReadDTO movieReadDTO;

    private Crew createCrew() {
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId(personsReadDTO.getId());
        crew.setCrewType(crewTypeReadDTO.getId());
        crew.setMovieId(movieReadDTO.getId());
        crew.setDescription("Description");
        return crewRepository.save(crew);
    }

    @Before
    public void setup() {
        if (personsReadDTO==null) {
            Persons persons = new Persons();
            persons.setSurname("Surname");
            persons.setName("Name");
            persons.setMiddleName("MiddleName");
            persons = personsRepository.save(persons);

            personsReadDTO = personsService.getPersons(persons.getId());
        }

        if (crewTypeReadDTO==null) {
            CrewType crewType = new CrewType();
            crewType.setName("Director");
            crewType = crewTypeRepository.save(crewType);

            crewTypeReadDTO = crewTypeService.getCrewType(crewType.getId());
        }

        if (movieReadDTO==null) {
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

            movieReadDTO = movieService.getMovie(movie.getId());
        }
    }

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

    @Test
    public void testCreateCrew() {
        CrewCreateDTO create = new CrewCreateDTO();
        create.setPersonId(personsReadDTO.getId());
        create.setCrewType(crewTypeReadDTO.getId());
        create.setMovieId(movieReadDTO.getId());
        create.setDescription("Description");
        CrewReadDTO read = crewService.createCrew(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Crew crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(crew);
    }

    @Test
    public void testPatchCrew() {
        Crew crew = createCrew();

        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setPersonId(personsReadDTO.getId());
        patch.setCrewType(crewTypeReadDTO.getId());
        patch.setMovieId(movieReadDTO.getId());
        patch.setDescription("Description");
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(crew).isEqualToComparingFieldByField(read);
    }

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
