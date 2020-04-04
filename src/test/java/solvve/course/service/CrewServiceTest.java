package solvve.course.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.assertj.core.api.Assertions;
import java.util.UUID;

import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.BaseTest;
import solvve.course.domain.Crew;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewRepository;
import solvve.course.repository.CrewTypeRepository;
import solvve.course.utils.TestObjectsFactory;

public class CrewServiceTest extends BaseTest {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewService crewService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Test
    public void testGetCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewReadExtendedDTO readDTO = crewService.getCrew(crew.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(crew,
                "movieId", "personId", "crewTypeId","person", "movie", "crewType");
        Assertions.assertThat(readDTO.getMovie().getId()).isEqualToIgnoringGivenFields(movie.getId());
        Assertions.assertThat(readDTO.getPerson().getId()).isEqualToIgnoringGivenFields(person.getId());
        Assertions.assertThat(readDTO.getCrewType().getId()).isEqualToIgnoringGivenFields(crewType.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCrewWrongId() {
        crewService.getCrew(UUID.randomUUID());
    }

    @Test
    public void testCreateCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewCreateDTO create = testObjectsFactory.createCrewCreateDTO();
        create.setPersonId(person.getId());
        create.setCrewTypeId(crewType.getId());
        create.setMovieId(movie.getId());
        CrewReadDTO read = crewService.createCrew(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        testObjectsFactory.inTransaction(() -> {
            Crew crew = crewRepository.findById(read.getId()).get();
            Assertions.assertThat(read).isEqualToIgnoringGivenFields(crew,
                    "movieId", "personId", "crewTypeId");
            Assertions.assertThat(read.getMovieId()).isEqualToIgnoringGivenFields(crew.getMovie().getId());
            Assertions.assertThat(read.getPersonId()).isEqualToIgnoringGivenFields(crew.getPerson().getId());
            Assertions.assertThat(read.getCrewTypeId()).isEqualToIgnoringGivenFields(crew.getCrewType().getId());
        });
    }

    @Test
    public void testPatchCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPatchDTO patch = testObjectsFactory.createCrewPatchDTO();
        patch.setPersonId(person.getId());
        patch.setCrewTypeId(crewType.getId());
        patch.setMovieId(movie.getId());
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        testObjectsFactory.inTransaction(() -> {
            Crew crew1 = crewRepository.findById(read.getId()).get();
            Assertions.assertThat(crew1).isEqualToIgnoringGivenFields(read,
                    "movie", "person", "crewType");
            Assertions.assertThat(crew1.getMovie().getId()).isEqualToIgnoringGivenFields(read.getMovieId());
            Assertions.assertThat(crew1.getPerson().getId()).isEqualToIgnoringGivenFields(read.getPersonId());
            Assertions.assertThat(crew1.getCrewType().getId()).isEqualToIgnoringGivenFields(read.getCrewTypeId());
        });
    }

    @Test
    public void testPatchCrewEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPatchDTO patch = new CrewPatchDTO();
        CrewReadDTO read = crewService.patchCrew(crew.getId(), patch);

        Assert.assertNotNull(read.getPersonId());
        Assert.assertNotNull(read.getCrewTypeId());
        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getDescription());

        testObjectsFactory.inTransaction(() -> {
            Crew crewAfterUpdate = crewRepository.findById(read.getId()).get();

            Assert.assertNotNull(crewAfterUpdate.getPerson());
            Assert.assertNotNull(crewAfterUpdate.getCrewType());
            Assert.assertNotNull(crewAfterUpdate.getMovie());
            Assert.assertNotNull(crewAfterUpdate.getDescription());

            Assertions.assertThat(crew).isEqualToIgnoringGivenFields(crewAfterUpdate,
                    "person", "movie", "crewType");
            Assertions.assertThat(crew.getMovie()).isEqualToIgnoringGivenFields(crewAfterUpdate.getMovie(),
                    "genres","movieProdCountries","movieProdCompanies","movieProdLanguages",
                    "crews","movieReview","movieReviewCompliants","movieReviewFeedbacks","movieVotes","releaseDetails",
                    "userTypoRequests");
            Assertions.assertThat(crew.getCrewType()).isEqualToIgnoringGivenFields(crewAfterUpdate.getCrewType(),
                    "crew");
            Assertions.assertThat(crew.getPerson()).isEqualToIgnoringGivenFields(crewAfterUpdate.getPerson(),
                    "crews");
        });
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

    @Test
    public void testPutCrew() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPutDTO put = testObjectsFactory.createCrewPutDTO();
        put.setPersonId(person.getId());
        put.setCrewTypeId(crewType.getId());
        put.setMovieId(movie.getId());
        CrewReadDTO read = crewService.updateCrew(crew.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        testObjectsFactory.inTransaction(() -> {
            Crew crew1 = crewRepository.findById(read.getId()).get();
            Assertions.assertThat(crew1).isEqualToIgnoringGivenFields(read,
                    "movie", "person", "crewType");
            Assertions.assertThat(crew1.getMovie().getId()).isEqualToIgnoringGivenFields(read.getMovieId());
            Assertions.assertThat(crew1.getPerson().getId()).isEqualToIgnoringGivenFields(read.getPersonId());
            Assertions.assertThat(crew1.getCrewType().getId()).isEqualToIgnoringGivenFields(read.getCrewTypeId());
        });
    }

    @Test
    public void testPutCrewEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        CrewType crewType = testObjectsFactory.createCrewType();
        Crew crew = testObjectsFactory.createCrew(person, crewType, movie);

        CrewPutDTO put = new CrewPutDTO();
        crewType.setCrew(null);
        crewTypeRepository.save(crewType);
        CrewReadDTO read = crewService.updateCrew(crew.getId(), put);

        Assert.assertNotNull(read.getMovieId());
        Assert.assertNotNull(read.getPersonId());
        Assert.assertNotNull(read.getCrewTypeId());
        Assert.assertNull(read.getDescription());

        testObjectsFactory.inTransaction(() -> {
            Crew crewAfterUpdate = crewRepository.findById(crew.getId()).get();

            Assert.assertNotNull(crewAfterUpdate.getMovie());
            Assert.assertNotNull(crewAfterUpdate.getPerson());
            Assert.assertNotNull(crewAfterUpdate.getCrewType());
            Assert.assertNull(crewAfterUpdate.getDescription());

            Assertions.assertThat(crew).isEqualToIgnoringGivenFields(crewAfterUpdate,
                    "crewType","description","person", "movie", "updatedAt");
            Assertions.assertThat(crew.getPerson().getId()).isEqualTo(crewAfterUpdate.getPerson().getId());
            Assertions.assertThat(crew.getMovie().getId()).isEqualTo(crewAfterUpdate.getMovie().getId());
        });
    }
}
