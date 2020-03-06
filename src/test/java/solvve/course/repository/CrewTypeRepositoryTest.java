package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from crew",
        "delete from movie",
        "delete from crew_type",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CrewTypeRepositoryTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        CrewType r = testObjectsFactory.createCrewType("Type");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew c = testObjectsFactory.createCrew(p, r, m);

        assertNotNull(r.getId());
        testObjectsFactory.inTransaction(() -> {
            assertTrue(crewTypeRepository.findById(r.getId()).isPresent());
        });
    }

    @Test
    public void testGetCrewTypesWithEmptyFilter() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type1");
        CrewType ct3 = testObjectsFactory.createCrewType("Type2");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p, ct1, m);
        testObjectsFactory.createCrew(p, ct2, m);
        testObjectsFactory.createCrew(p, ct3, m);

        CrewTypeFilter filter = new CrewTypeFilter();
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId(), ct3.getId());
        });
    }

    @Test
    public void testGetCrewTypesByName() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type2");
        testObjectsFactory.createCrewType("Type3");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p, ct1, m);
        testObjectsFactory.createCrew(p, ct2, m);
        testObjectsFactory.createCrew(p, ct2, m);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setName("Type1");
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                    .containsExactlyInAnyOrder(ct1.getId());
        });
    }

    @Test
    public void testGetCrewTypesByCrew() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type1");
        CrewType ct3 = testObjectsFactory.createCrewType("Type2");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p, ct1, m);
        Crew c2 = testObjectsFactory.createCrew(p, ct2, m);
        Crew c3 = testObjectsFactory.createCrew(p, ct3, m);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setCrewId(c2.getId());
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                    .containsExactlyInAnyOrder(ct2.getId());
        });
    }

    @Test
    public void testGetCrewTypesByNames() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type2");
        testObjectsFactory.createCrewType("Type3");

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setNames(List.of("Type1", "Type2"));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId());
    }

    @Test
    public void testGetCrewTypesByCrews() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type1");
        CrewType ct3 = testObjectsFactory.createCrewType("Type2");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p, ct1, m);
        Crew c2 = testObjectsFactory.createCrew(p, ct2, m);
        Crew c3 = testObjectsFactory.createCrew(p, ct3, m);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setCrewIds(List.of(c2.getId(), c3.getId()));
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                    .containsExactlyInAnyOrder(ct3.getId(), ct2.getId());
        });
    }

    @Test
    public void testGetCrewTypesByAllFilters() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type1");
        CrewType ct3 = testObjectsFactory.createCrewType("Type2");
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p, ct1, m);
        Crew c2 = testObjectsFactory.createCrew(p, ct2, m);
        testObjectsFactory.createCrew(p, ct3, m);

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setName("Type1");
        filter.setCrewId(c2.getId());
        filter.setCrewIds(List.of(c2.getId(), c1.getId()));
        filter.setNames(List.of("Type1", "Test", "Type2"));
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(crewTypeService.getCrewTypes(filter)).extracting("Id")
                    .containsExactlyInAnyOrder(ct2.getId());
        });
    }

    @Test
    public void testCreatedAtIsSet() {
        CrewType entity = testObjectsFactory.createCrewType();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = crewTypeRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        CrewType entity = testObjectsFactory.createCrewType();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = crewTypeRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        CrewType entity = testObjectsFactory.createCrewType();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setName("NewNameTest");
        crewTypeRepository.save(entity);
        entity = crewTypeRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }
}
