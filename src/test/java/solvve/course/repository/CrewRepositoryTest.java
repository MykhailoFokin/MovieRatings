package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.CrewFilter;
import solvve.course.service.CrewService;

import java.time.Instant;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CrewRepositoryTest extends BaseTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Test
    public void testSave() {
        CrewType ct = testObjectsFactory.createCrewType();
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew r = testObjectsFactory.createCrew(p, ct, m, "Description Test");
        assertNotNull(r.getId());
        assertTrue(crewRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testGetCrewsByEmptyFilter() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p, ct1, m, "Description Test");
        Crew c2 = testObjectsFactory.createCrew(p, ct2, m, "Description Test");
        Crew c3 = testObjectsFactory.createCrew(p, ct3, m, "Description Test");

        CrewFilter filter = new CrewFilter();
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(),c2.getId(),c3.getId());
    }

    @Test
    public void testGetCrewByPerson() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p1, ct1, m, "Description Test");
        testObjectsFactory.createCrew(p2, ct2, m, "Description Test");
        testObjectsFactory.createCrew(p2, ct3, m, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setPersonId(p1.getId());
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId());
    }

    @Test
    public void testGetCrewByMovie() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = testObjectsFactory.createCrew(p2, ct2, m1, "Description Test");
        testObjectsFactory.createCrew(p2, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setMovieId(m1.getId());
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c2.getId());
    }

    @Test
    public void testGetCrewByCrewType() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p1, ct1, m2, "Description Test");
        testObjectsFactory.createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = testObjectsFactory.createCrew(p2, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setCrewTypeId(ct3.getId());
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c3.getId());
    }

    @Test
    public void testGetCrewByDescription() {
        String desc = "Description Test3";
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p1, ct1, m2, "Description Test1");
        testObjectsFactory.createCrew(p2, ct2, m1, "Description Test2");
        Crew c3 = testObjectsFactory.createCrew(p2, ct3, m2, desc);

        CrewFilter filter = new CrewFilter();
        filter.setDescription(desc);
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c3.getId());
    }

    @Test
    public void testGetCrewByPersons() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Person p3 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p1, ct1, m2, "Description Test");
        testObjectsFactory.createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = testObjectsFactory.createCrew(p3, ct3, m2, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setPersonIds(List.of(p1.getId(), p3.getId()));
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c3.getId());
    }

    @Test
    public void testGetCrewByMovies() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Person p3 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        testObjectsFactory.createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = testObjectsFactory.createCrew(p2, ct2, m1, "Description Test");
        Crew c3 = testObjectsFactory.createCrew(p3, ct3, m3, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setMovieIds(List.of(m1.getId(), m3.getId()));
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c2.getId(), c3.getId());
    }

    @Test
    public void testGetCrewByCrewTypes() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Person p3 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p1, ct1, m2, "Description Test");
        Crew c2 = testObjectsFactory.createCrew(p2, ct2, m1, "Description Test");
        testObjectsFactory.createCrew(p3, ct3, m3, "Description Test");

        CrewFilter filter = new CrewFilter();
        filter.setCrewTypesIds(List.of(ct1.getId(), ct2.getId()));
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId(), c2.getId());
    }

    @Test
    public void testGetCrewByAllFilters() {
        CrewType ct1 = testObjectsFactory.createCrewType();
        CrewType ct2 = testObjectsFactory.createCrewType();
        CrewType ct3 = testObjectsFactory.createCrewType();
        Person p1 = testObjectsFactory.createPerson();
        Person p2 = testObjectsFactory.createPerson();
        Person p3 = testObjectsFactory.createPerson();
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Crew c1 = testObjectsFactory.createCrew(p1, ct1, m2, "Description Test1");
        testObjectsFactory.createCrew(p2, ct2, m1, "Description Test2");
        testObjectsFactory.createCrew(p3, ct3, m3, "Description Test3");

        CrewFilter filter = new CrewFilter();
        filter.setMovieId(m2.getId());
        filter.setPersonId(p1.getId());
        filter.setCrewTypeId(ct1.getId());
        filter.setMovieIds(List.of(m2.getId(), m1.getId(),m3.getId()));
        filter.setCrewTypesIds(List.of(ct1.getId(), ct2.getId(),ct3.getId()));
        filter.setPersonIds(List.of(p1.getId(), p3.getId()));
        filter.setDescriptions(List.of("Description Test1","Description Test2"));
        Assertions.assertThat(crewService.getCrews(filter, Pageable.unpaged()).getData())
                .extracting("Id")
                .containsExactlyInAnyOrder(c1.getId());
    }

    @Test
    public void testCreatedAtIsSet() {
        CrewType ct = testObjectsFactory.createCrewType();
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew crew = testObjectsFactory.createCrew(p, ct, m, "Desc");

        Instant createdAtBeforeReload = crew.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        crew = crewRepository.findById(crew.getId()).get();

        Instant createdAtAfterReload = crew.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        CrewType ct = testObjectsFactory.createCrewType();
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew crew = testObjectsFactory.createCrew(p, ct, m, "Desc");

        Instant updatedAtBeforeReload = crew.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        crew = crewRepository.findById(crew.getId()).get();

        Instant updatedAtAfterReload = crew.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        CrewType ct = testObjectsFactory.createCrewType();
        Person p = testObjectsFactory.createPerson();
        Movie m = testObjectsFactory.createMovie();
        Crew crew = testObjectsFactory.createCrew(p, ct, m, "Desc");

        Instant updatedAtBeforeReload = crew.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        crew.setDescription("NewTest");
        crewRepository.save(crew);

        testObjectsFactory.inTransaction(() -> {
            Crew crewAfterReload = crewRepository.findById(crew.getId()).get();

            Instant updatedAtAfterReload = crewAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveCrewValidation() {
        Crew entity = new Crew();
        crewRepository.save(entity);
    }
}
