package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.CrewFilter;
import solvve.course.service.CrewService;

import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from crew",
        "delete from movie",
        "delete from crew_type",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CrewRepositoryTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSave() {
        CrewType ct = createCrewType();
        Person p = createPerson();
        Movie m = createMovie();
        Crew r = createCrew(p, ct, m, "Description Test");
        assertNotNull(r.getId());
        assertTrue(crewRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testGetCrewsByEmptyFilter() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p = createPerson();
        Movie m = createMovie();
        Crew c1 = createCrew(p, ct1, m, "Description Test");
        Crew c2 = createCrew(p, ct2, m, "Description Test");
        Crew c3 = createCrew(p, ct3, m, "Description Test");

        CrewFilter filter = new CrewFilter();
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(),c2.getId(),c3.getId());
    }

    @Test
    public void testGetCrewByPerson() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Movie m = createMovie();
        Crew c1 = createCrew(p1, ct1, m, "Description Test");
        createCrew(p2, ct2, m, "Description Test");
        createCrew(p2, ct3, m, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setPersonId(p1.getId());
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId());
    }

    @Test
    public void testGetCrewByMovie() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = createCrew(p2, ct2, m1, "Description Test");
        createCrew(p2, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setMovieId(m1.getId());
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c2.getId());
    }

    @Test
    public void testGetCrewByCrewType() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        createCrew(p1, ct1, m2, "Description Test");
        createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = createCrew(p2, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setCrewType(ct3.getId());
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c3.getId());
    }

    @Test
    public void testGetCrewByDescription() {
        String desc = "Description Test3";
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        createCrew(p1, ct1, m2, "Description Test1");
        createCrew(p2, ct2, m1, "Description Test2");
        Crew c3 = createCrew(p2, ct3, m2, desc);

        CrewFilter filter = new CrewFilter();
        filter.setDescription(desc);
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c3.getId());
    }

    @Test
    public void testGetCrewByPersons() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Person p3 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        Crew c1 = createCrew(p1, ct1, m2, "Description Test");
        createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = createCrew(p3, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setPersonIds(List.of(p1.getId(), p3.getId()));
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c3.getId());
    }

    @Test
    public void testGetCrewByMovies() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Person p3 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        Movie m3 = createMovie();
        createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = createCrew(p3, ct3, m3, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setMovieIds(List.of(m1.getId(), m3.getId()));
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c2.getId(), c3.getId());
    }

    @Test
    public void testGetCrewByCrewTypes() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Person p3 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        Movie m3 = createMovie();
        Crew c1 = createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = createCrew(p2, ct2, m1, "Description Test");
        createCrew(p3, ct3, m3, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setCrewTypesIds(List.of(ct1.getId(), ct2.getId()));
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c2.getId());
    }

    @Test
    public void testGetCrewByAllFilters() {
        CrewType ct1 = createCrewType();
        CrewType ct2 = createCrewType();
        CrewType ct3 = createCrewType();
        Person p1 = createPerson();
        Person p2 = createPerson();
        Person p3 = createPerson();
        Movie m1 = createMovie();
        Movie m2 = createMovie();
        Movie m3 = createMovie();
        Crew c1 = createCrew(p1, ct1, m2, "Description Test1");
        createCrew(p2, ct2, m1, "Description Test2");
        createCrew(p3, ct3, m3, "Description Test3");

        CrewFilter filter = new CrewFilter();
        filter.setMovieId(m2.getId());
        filter.setPersonId(p1.getId());
        filter.setCrewType(ct1.getId());
        filter.setMovieIds(List.of(m2.getId(), m1.getId(),m3.getId()));
        filter.setCrewTypesIds(List.of(ct1.getId(), ct2.getId(),ct3.getId()));
        filter.setPersonIds(List.of(p1.getId(), p3.getId()));
        filter.setDescriptions(List.of("Description Test1","Description Test2"));
        Assertions.assertThat(crewService.getCrews(filter)).extracting("Id")
                .containsExactlyInAnyOrder(c1.getId());
    }

    private CrewType createCrewType() {
        CrewType crewType = new CrewType();
        crewType.setName("Director");
        crewType = crewTypeRepository.save(crewType);
        return  crewType;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setId(UUID.randomUUID());
        person.setSurname("Surname");
        person.setName("Name");
        person.setMiddleName("MiddleName");
        person = personRepository.save(person);
        return person;
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(UUID.randomUUID());
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setSoundMix("DolbySurround");
        movie = movieRepository.save(movie);
        return movie;
    }

    private Crew createCrew(Person person, CrewType crewType, Movie movie, String description) {
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId(person);
        crew.setCrewType(crewType);
        crew.setMovieId(movie);
        crew.setDescription(description);
        return crewRepository.save(crew);
    }
}
