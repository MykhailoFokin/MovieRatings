package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Crew;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.dto.CrewTypeFilter;
import solvve.course.service.CrewTypeService;

import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from crew","delete from movie","delete from crew_type","delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CrewTypeRepositoryTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    @Transactional
    @Test
    public void testSave() {
        CrewType r = createCrewType("Type");
        Person p = createPerson();
        Movie m = createMovie();
        Crew c = createCrew(p, m, r);

        assertNotNull(r.getId());
        assertTrue(crewTypeRepository.findById(r.getId()).isPresent());
    }

    @Transactional
    @Test
    public void testGetCrewTypesWithEmptyFilter() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type1");
        CrewType ct3 = createCrewType("Type2");
        Person p = createPerson();
        Movie m = createMovie();
        createCrew(p, m, ct1);
        createCrew(p, m, ct2);
        createCrew(p, m, ct2);

        CrewTypeFilter filter = new CrewTypeFilter();
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId(), ct3.getId());
    }

    @Transactional
    @Test
    public void testGetCrewTypesByName() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type1");
        createCrewType("Type2");
        Person p = createPerson();
        Movie m = createMovie();
        createCrew(p, m, ct1);
        createCrew(p, m, ct2);
        createCrew(p, m, ct2);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setName("Type1");
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId());
    }

    @Transactional
    @Test
    public void testGetCrewTypesByCrew() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type1");
        createCrewType("Type2");
        Person p = createPerson();
        Movie m = createMovie();
        createCrew(p, m, ct1);
        Crew c2 = createCrew(p, m, ct2);
        createCrew(p, m, ct2);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setCrew(c2.getId());
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct2.getId());
    }

    @Test
    public void testGetCrewTypesByNames() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type2");
        createCrewType("Type3");

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setNames(List.of("Type1", "Type2"));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId());
    }

    @Transactional
    @Test
    public void testGetCrewTypesByCrews() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type1");
        CrewType ct3 = createCrewType("Type2");
        Person p = createPerson();
        Movie m = createMovie();
        createCrew(p, m, ct1);
        Crew c2 = createCrew(p, m, ct2);
        Crew c3 = createCrew(p, m, ct3);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setCrewIds(List.of(c2.getId(), c3.getId()));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct3.getId(), ct2.getId());
    }

    @Transactional
    @Test
    public void testGetCrewTypesByAllFilters() {
        CrewType ct1 = createCrewType("Type1");
        CrewType ct2 = createCrewType("Type1");
        CrewType ct3 = createCrewType("Type2");
        Person p = createPerson();
        Movie m = createMovie();
        Crew c1 = createCrew(p, m, ct1);
        Crew c2 = createCrew(p, m, ct2);
        createCrew(p, m, ct3);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setName("Type1");
        filter.setCrew(c2.getId());
        filter.setCrewIds(List.of(c2.getId(), c1.getId()));
        filter.setNames(List.of("Type1", "Test", "Type2"));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct2.getId());
    }

    private CrewType createCrewType(String typeName) {
        CrewType crewType = new CrewType();
        crewType.setId(UUID.randomUUID());
        crewType.setName(typeName);
        crewType = crewTypeRepository.save(crewType);
        return crewType;
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

    private Crew createCrew(Person person, Movie movie, CrewType crewType) {
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId(person);
        crew.setCrewType(crewType);
        crew.setMovieId(movie);
        crew.setDescription("Description");
        return crewRepository.save(crew);
    }
}
