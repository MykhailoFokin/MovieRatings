package solvve.course.service.importer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.client.themoviedb.TheMovieDbClient;
import solvve.course.client.themoviedb.dto.MovieCreditsReadDTO;
import solvve.course.client.themoviedb.dto.MovieReadDTO;
import solvve.course.client.themoviedb.dto.PersonReadDTO;
import solvve.course.domain.*;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.repository.*;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class MovieImporterService {

    @Autowired
    private TheMovieDbClient movieDbClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ExternalSystemImportService externalSystemImportService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Transactional
    public UUID importMovie(String movieExternalId, String languageIsoCode)
            throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        log.info("Importing movie with external id={}", movieExternalId);

        externalSystemImportService.validateNotImported(Movie.class, movieExternalId);
        MovieReadDTO read = movieDbClient.getMovie(movieExternalId, languageIsoCode);
        Movie existingMovie = movieRepository.findByTitle(read.getTitle());
        if (existingMovie != null) {
            throw new ImportedEntityAlreadyExistException(Movie.class, existingMovie.getId(),
                    "Movie with name=" + read.getTitle() + " already exist");
        }

        Movie movie = new Movie();
        movie.setTitle(read.getTitle());
        movie.setYear((short) read.getReleaseDate().getYear());
        movie.setAdult(read.getAdult());
        movie.setOriginalTitle(read.getOriginalTitle());
        movie.setOriginalLanguage(read.getOriginalLanguage());
        movie.setDescription(read.getOverview());
        movie.setBudget(read.getBudget());
        movie.setHomepage(read.getHomepage());
        movie.setImdbId(read.getImdbId());
        movie.setRevenue(read.getRevenue());
        movie.setRuntime(read.getRuntime());
        movie.setTagline(read.getTagline());

        Movie internalMovie = movieRepository.save(movie);

        externalSystemImportService.createExternalSystemImport(movie, movieExternalId);

        MovieCreditsReadDTO movieCredits = movieDbClient.getMovieCredits(movieExternalId, languageIsoCode);

        movieCredits.getCast().stream().map(castPerson -> {
            Person internalPerson = personRepository.findByName(castPerson.getName());
            if (internalPerson != null) { // create new Person if not exists
                PersonReadDTO externalPerson = movieDbClient.getPerson(castPerson.getId());
                internalPerson = createPerson(externalPerson);

                externalSystemImportService.createExternalSystemImport(internalPerson, externalPerson.getId());

                log.info("Person from crew with external id={} and name={} imported",
                        externalPerson.getId(), externalPerson.getName());
            } else {
                log.info("Current person from cast with name={} already exists. Skipped!", castPerson.getName());
            }

            Role internalRole = roleRepository.findByTitle(castPerson.getCharacter());
            if (internalRole != null) {
                Role role = new Role();
                role.setTitle(castPerson.getCharacter());
                role.setRoleType(RoleType.NOT_DEFINED);
                role.setPerson(internalPerson);
                role.setMovie(internalMovie);
                roleRepository.save(role);

                externalSystemImportService.createExternalSystemImport(role, castPerson.getId());

                log.info("Role with external id={} and name={} imported",
                        castPerson.getId(), castPerson.getName());
            } else {
                log.info("Current role with name={} already exists. Skipped!", castPerson.getName());
            }
            return castPerson.getCharacter();
        });

        movieCredits.getCrew().stream().map(crewPerson -> {
            CrewType internalCrewType = crewTypeRepository.findByName(crewPerson.getDepartment());
            if (internalCrewType != null) {
                CrewType crewType = new CrewType();
                crewType.setName(crewPerson.getDepartment());
                internalCrewType = crewTypeRepository.save(internalCrewType);

                externalSystemImportService.createExternalSystemImport(crewType, crewPerson.getCreditId());

                log.info("Crew type with name={} imported", crewPerson.getDepartment());
            } else {
                log.info("Current crew type with name={} already exists. Skipped!", crewPerson.getDepartment());
            }

            Person internalPerson = personRepository.findByName(crewPerson.getName());
            if (internalPerson != null) { // create new Person if not exists
                PersonReadDTO externalPerson = movieDbClient.getPerson(crewPerson.getId());
                internalPerson = createPerson(externalPerson);

                externalSystemImportService.createExternalSystemImport(internalPerson, externalPerson.getId());

                log.info("Person from crew with external id={} and name={} imported",
                        externalPerson.getId(), externalPerson.getName());
            } else {
                log.info("Current person from crew with name={} already exists. Skipped!", crewPerson.getName());
            }

            Crew internalCrew = crewRepository.findByDescription(crewPerson.getJob());
            if (internalCrew != null) {
                Crew crew = new Crew();
                crew.setCrewType(internalCrewType);
                crew.setDescription(crewPerson.getJob());
                crew.setMovie(internalMovie);
                crew.setPerson(internalPerson);
                crewRepository.save(crew);

                externalSystemImportService.createExternalSystemImport(crew, crewPerson.getCreditId());

                log.info("Crew person with job title={} and name={} imported", crewPerson.getJob(),
                        crewPerson.getName());
            } else {
                log.info("Current crew person with name={} already exists. Skipped!", crewPerson.getName());
            }

            return crewPerson.getName();
        });

        // TODO import roles, crew, etc


        log.info("Movie with external id={} imported", movieExternalId);

        return movie.getId();
    }

    private Person createPerson(PersonReadDTO externalPerson) {
        Person person = new Person();
        person.setBirthday(externalPerson.getBirthday());
        person.setKnownForDepartment(externalPerson.getKnownForDepartment());
        person.setDeathday(externalPerson.getDeathday());
        person.setName(externalPerson.getName());
        person.setGender(externalPerson.getGender());
        person.setBiography(externalPerson.getBiography());
        person.setPlaceOfBirth(externalPerson.getPlaceOfBirth());
        person.setAdult(externalPerson.getAdult());
        person.setImdbId(externalPerson.getImdbId());
        return personRepository.save(person);
    }
}
