package solvve.course.service.importer;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import solvve.course.BaseTest;
import solvve.course.client.themoviedb.TheMovieDbClient;
import solvve.course.client.themoviedb.dto.MovieCreditsCastReadDTO;
import solvve.course.client.themoviedb.dto.MovieCreditsCrewReadDTO;
import solvve.course.client.themoviedb.dto.MovieCreditsReadDTO;
import solvve.course.client.themoviedb.dto.MovieReadDTO;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;
import solvve.course.domain.Role;
import solvve.course.domain.RoleType;
import solvve.course.exception.ImportAlreadyPerformedException;
import solvve.course.exception.ImportedEntityAlreadyExistException;
import solvve.course.repository.CrewRepository;
import solvve.course.repository.MovieRepository;
import solvve.course.repository.PersonRepository;
import solvve.course.repository.RoleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MovieImporterServiceTest extends BaseTest {

    @MockBean
    private TheMovieDbClient movieDbClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImporterService movieImporterService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testMovieImporter() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        String movieExternalId = "id1";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate(LocalDate.of(2020,01,01));
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        UUID movieId = movieImporterService.importMovie(movieExternalId, null);
        Movie movie = movieRepository.findById(movieId).get();

        Assert.assertEquals(read.getTitle(), movie.getTitle());
    }

    @Test
    public void testMovieImportAlreadyExist() {
        String movieExternalId = "id2";

        Movie existingMovie = testObjectsFactory.generateFlatEntityWithoutId(Movie.class);
        existingMovie = movieRepository.save(existingMovie);

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setTitle(existingMovie.getTitle());
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        ImportedEntityAlreadyExistException ex =
                Assertions.catchThrowableOfType(() -> movieImporterService.importMovie(movieExternalId, null),
                        ImportedEntityAlreadyExistException.class);
        Assert.assertEquals(Movie.class, ex.getEntityClass());
        Assert.assertEquals(existingMovie.getId(), ex.getEntityId());
    }

    @Test
    public void testNoCallToClientOnDuplicateImport() throws ImportedEntityAlreadyExistException,
            ImportAlreadyPerformedException {

        String movieExternalId = "id3";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate(LocalDate.of(2020,01,01));
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        movieImporterService.importMovie(movieExternalId, null);
        Mockito.verify(movieDbClient).getMovie(movieExternalId, null);
        Mockito.reset(movieDbClient);

        Assertions.assertThatThrownBy(() -> movieImporterService.importMovie(movieExternalId, null))
                .isInstanceOf(ImportAlreadyPerformedException.class);

        Mockito.verifyNoInteractions(movieDbClient);
    }

    @Test
    public void testMovieImporterCast() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        String movieExternalId = "id4";

        MovieReadDTO read = testObjectsFactory.generateObject(MovieReadDTO.class);
        read.setReleaseDate(LocalDate.of(2020,01,01));
        Mockito.when(movieDbClient.getMovie(movieExternalId, null)).thenReturn(read);

        MovieCreditsCastReadDTO movieCreditsCastReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCastReadDTO.class);
        MovieCreditsCrewReadDTO movieCreditsCrewReadDTO =
                testObjectsFactory.generateObject(MovieCreditsCrewReadDTO.class);
        MovieCreditsReadDTO movieCreditsReadDTO = new MovieCreditsReadDTO();
        movieCreditsReadDTO.setCast(List.of(movieCreditsCastReadDTO));
        movieCreditsReadDTO.setCrew(List.of(movieCreditsCrewReadDTO));
        movieCreditsReadDTO.setId(movieExternalId);
        Mockito.when(movieDbClient.getMovieCredits(movieExternalId, null)).thenReturn(movieCreditsReadDTO);

        UUID movieId = movieImporterService.importMovie(movieExternalId, null);
        Movie movie = movieRepository.findById(movieId).get();

        Assert.assertEquals(read.getTitle(), movie.getTitle());
    }

    @Ignore
    @Test
    public void testMovieImport() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        String movieExternalId = "280";

        UUID movieId = movieImporterService.importMovie("280", null);

        Movie movie = movieRepository.findById(movieId).get();
        Assert.assertEquals(movieId, movie.getId());
        Assert.assertEquals("Terminator 2: Judgment Day", movie.getOriginalTitle());
        Assert.assertEquals(movie.getAdult(), false);
        Assert.assertEquals(movie.getBudget().longValue(), 102000000L);
        Assert.assertEquals(movie.getHomepage(), "");
        Assert.assertEquals(movie.getImdbId(), "tt0103064");
        Assert.assertEquals(movie.getOriginalLanguage(), "en");
        Assert.assertEquals(movie.getDescription(), "Nearly 10 years have passed since Sarah Connor was targeted " +
                "for termination by a cyborg from the future. Now her son, John, the future leader of the resistance," +
                " is the target for a newer, more deadly terminator. Once again, the resistance has managed to send" +
                " a " +
                "protector back to attempt to save John and his mother Sarah.");
        Assert.assertEquals(movie.getYear(), "1991");
        Assert.assertEquals(movie.getRevenue().longValue(), 520000000L);
        Assert.assertEquals(movie.getRuntime().intValue(), 137);
        Assert.assertEquals(movie.getTagline(), "It's nothing personal.");

        Person person = personRepository.findById(movie.getRole().getPerson().getId()).get();
        Assert.assertEquals("1947-07-30", person.getBirthday());
        Assert.assertEquals("Acting", person.getKnownForDepartment());
        Assert.assertNull(person.getDeathday());
        Assert.assertEquals("Arnold Schwarzenegger", person.getName());
        Assert.assertEquals((short) 2, person.getGender().shortValue());
        Assert.assertEquals("An Austrian-American former professional bodybuilder, actor, model, businessman and " +
                "politician who served as the 38th Governor of California (2003–2011). Schwarzenegger began weight " +
                "training at 15. He was awarded the title of Mr. Universe at age 20 and went on to win the Mr. " +
                "Olympia contest a total of seven times. Schwarzenegger has remained a prominent presence in the " +
                "sport of bodybuilding and has written several books and numerous articles on the sport. " +
                "Schwarzenegger gained worldwide fame as a Hollywood action film icon, noted for his lead roles in " +
                "such films as Conan the Barbarian, The Terminator, Commando and Predator. He was nicknamed the " +
                "\"Austrian Oak\" and the \"Styrian Oak\" in his bodybuilding days, \"Arnie\" during his acting " +
                "career and more recently the \"Governator\" (a portmanteau of \"Governor\" and \"Terminator\"). " +
                "Arnold has starred in many films. Most notably are the following... Hercules in New York as Hercules" +
                " (1970)  Stay Hungry as Joe Santo (1976)  Pumping Iron as himself (1977)  The Villain as Handsome " +
                "Stranger (1979)  The Jayne Mansfield Story as Mickey Hargitay (1980)  Conan the Barbarian as Conan " +
                "(1982)  Conan the Destroyer as Conan (1984)  The Terminator as The Terminator/T-800 Model 101 (1984)" +
                "  Red Sonja as Kalidor (1985)  Commando as John Matrix (1985)  Raw Deal as Mark Kaminsky, a.k.a. " +
                "Joseph P. Brenner (1986)  Predator as Major Alan \"Dutch\" Schaeffer (1987)  The Running Man as Ben " +
                "Richards (1987)  Red Heat as Captain Ivan Danko (1988)  Twins as Julius Benedict (1988)  Total " +
                "Recall as Douglas Quaid/Hauser (1990)  Kindergarten Cop as Detective John Kimble (1990)  Terminator " +
                "2: Judgment Day as The Terminator/T-800 Model 101 (1991)  Last Action Hero as Jack Slater / Himself " +
                "(1993)  True Lies as Harry Tasker (1994)  Junior as Dr. Alex Hesse (1994)  Eraser as U.S. Marshal " +
                "John Kruger (1996)  Jingle All the Way as Howard Langston (1996)  Batman and Robin as Mr. Freeze " +
                "(1997)  End of Days as Jericho Cane (1999)  The 6th Day as Adam Gibson / Adam Gibson Clone (2000)  " +
                "Collateral Damage as Gordy Brewer (2002)  Terminator 3: Rise of the Machines as The Terminator/T-850" +
                " Model 101 (2003)  Around the World in 80 Days as Prince Hapi (2004)  The Expendables as Trench " +
                "(2010)  The Expendables 2 as Trench (2012)  The Last Stand as Sheriff Ray Owens (2013)  Escape Plan " +
                "as Rottmayer (2013)  Sabotage as John 'Breacher' Wharton (2014)  The Expendables 3 as Trench (2014) " +
                " Maggie as Wade Vogel (2015)  Terminator Genisys as The Terminator/T-800 Model 101/ The Guardian " +
                "(2015)  Aftermath as Roman Melnik (2017)  Killing Gunther as Gunther (2017)  Journey to China: The " +
                "Mystery of Iron Mask (2017)  Triplets as Julius Benedict (2018)  The Expendables 4 as Trench (2018) " +
                " Terminator 6 as The Terminator/T-800 Model 101 (2019)", person.getBiography());
        Assert.assertEquals("Thal, Styria, Austria", person.getPlaceOfBirth());
        Assert.assertEquals(false, person.getAdult());
        Assert.assertEquals("nm0000216", person.getImdbId());
        Assert.assertEquals("http://www.schwarzenegger.com", person.getHomepage());

        Role role = roleRepository.findById(movie.getRole().getId()).get();
        Assert.assertEquals(role.getRoleType(),RoleType.NOT_DEFINED);
        Assert.assertEquals(role.getTitle(),"The Terminator");
        Assert.assertEquals(role.getPerson(),person);
        Assert.assertEquals(role.getMovie(),movie);
    }
}
