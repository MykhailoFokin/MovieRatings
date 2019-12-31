package solvve.course.service;

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
import solvve.course.domain.Persons;
import solvve.course.dto.CrewCreateDTO;
import solvve.course.dto.CrewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from crew", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewServiceTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Test
    public void testGetCrew() {
        Persons persons = new Persons();
        Crew crew = new Crew();
        crew.setId(UUID.randomUUID());
        crew.setPersonId("Crew Test");
        crew.setCrewType(UUID);
        crew.setDescription("Description");
        crew = crewRepository.save(crew);

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
        create.setTitle("Crew Test");
        create.setYear((short) 2019);
        create.setGenres("Comedy");
        create.setAspectRatio("1:10");
        create.setCamera("Panasonic");
        create.setColour("Black");
        create.setCompanies("Paramount");
        create.setCountries("USA");
        create.setCrew("Crew");
        create.setCritique("123");
        create.setDescription("Description");
        create.setFilmingLocations("USA");
        create.setLaboratory("CaliforniaDreaming");
        create.setLanguages("English");
        create.setReleaseDates("30.12.2019");
        create.setSoundMix("DolbySurround");
        CrewReadDTO read = crewService.createCrew(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Crew crew = crewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(crew);
    }
}
