package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.Crew;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.dto.CrewTypeFilter;
import solvve.course.service.CrewTypeService;

import java.time.Instant;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrewTypeRepositoryTest extends BaseTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

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
        Assertions.assertThat(crewTypeService.getCrewTypes(filter, Pageable.unpaged()).getData())
                .extracting("Id")
            .containsExactlyInAnyOrder(ct1.getId(), ct2.getId(), ct3.getId());
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
        Assertions.assertThat(crewTypeService.getCrewTypes(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId());
    }

    @Test
    public void testGetCrewTypesByNames() {
        CrewType ct1 = testObjectsFactory.createCrewType("Type1");
        CrewType ct2 = testObjectsFactory.createCrewType("Type2");
        testObjectsFactory.createCrewType("Type3");

        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setNames(List.of("Type1", "Type2"));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(ct1.getId(), ct2.getId());
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
        filter.setNames(List.of("Type1", "Test", "Type2"));
        Assertions.assertThat(crewTypeService.getCrewTypes(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(ct2.getId(), ct1.getId());
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

        CrewType entityAfterReload = crewTypeRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entityAfterReload.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveCrewTypeValidation() {
        CrewType entity = new CrewType();
        crewTypeRepository.save(entity);
    }
}
