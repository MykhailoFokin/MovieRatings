package solvve.course.client.themoviedb;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.client.themoviedb.dto.*;

import java.time.LocalDate;

public class TheMovieDbClientTest extends BaseTest {

    @Autowired
    private TheMovieDbClient theMovieDbClient;

    @Test
    public void testGetMovieRu() {
        String movieId = "280";
        MovieReadDTO movie = theMovieDbClient.getMovie(movieId, "ru");
        Assert.assertEquals(movieId, movie.getId());
        Assert.assertEquals("Terminator 2: Judgment Day", movie.getOriginalTitle());
        Assert.assertEquals("Терминатор 2: Судный день", movie.getTitle());
    }

    @Test
    public void testGetMovieDefaultLanguage() {
        String movieId = "280";
        MovieReadDTO movie = theMovieDbClient.getMovie(movieId, null);
        Assert.assertEquals(movieId, movie.getId());
        Assert.assertEquals("Terminator 2: Judgment Day", movie.getOriginalTitle());
        Assert.assertEquals(movie.getAdult(), false);
        Assert.assertEquals(movie.getBudget().longValue(), 102000000L);
        Assert.assertEquals(movie.getHomepage(), "");
        Assert.assertEquals(movie.getImdbId(), "tt0103064");
        Assert.assertEquals(movie.getOriginalLanguage(), "en");
        Assert.assertEquals(movie.getOverview(), "Nearly 10 years have passed since Sarah Connor was targeted for " +
                "termination by a cyborg from the future. Now her son, John, the future leader of the resistance, is " +
                "the target for a newer, more deadly terminator. Once again, the resistance has managed to send a " +
                "protector back to attempt to save John and his mother Sarah.");
        Assert.assertEquals(movie.getReleaseDate(), "1991-07-03");
        Assert.assertEquals(movie.getRevenue().longValue(), 520000000L);
        Assert.assertEquals(movie.getRuntime().intValue(), 137);
        Assert.assertEquals(movie.getTagline(), "It's nothing personal.");
    }

    @Test
    public void testGetTopRatedMovies() {
        MoviesPageDTO moviesPage = theMovieDbClient.getTopRatedMovies();
        Assert.assertTrue(moviesPage.getTotalPages() > 0);
        Assert.assertTrue(moviesPage.getTotalResults() > 0);
        Assert.assertTrue(moviesPage.getResults().size() > 0);
        for (MovieReadShortDTO read : moviesPage.getResults()) {
            Assert.assertNotNull(read.getId());
            Assert.assertNotNull(read.getTitle());
        }
    }

    @Test
    public void testGetPerson() {
        String personId = "1100";
        PersonReadDTO person = theMovieDbClient.getPerson(personId);
        Assert.assertEquals(personId, person.getId());
        Assert.assertEquals(LocalDate.of(1947, 07, 30), person.getBirthday());//"1947-07-30"
        Assert.assertEquals("Acting", person.getKnownForDepartment());
        Assert.assertNull(person.getDeathday());
        Assert.assertEquals("Arnold Schwarzenegger", person.getName());
        Assert.assertEquals((short) 2, person.getGender().shortValue());
        Assert.assertEquals("An Austrian-American former professional bodybuilder, actor, model, businessman and " +
                "politician who served as the 38th Governor of California (2003–2011).\n\n" +
                "Schwarzenegger began weight " +
                "training at 15. He was awarded the title of Mr. Universe at age 20 and went on to win the Mr. " +
                "Olympia contest a total of seven times. Schwarzenegger has remained a prominent presence in the " +
                "sport of bodybuilding and has written several books and numerous articles on the sport.\n\n" +
                "Schwarzenegger gained worldwide fame as a Hollywood action film icon, noted for his lead roles in " +
                "such films as Conan the Barbarian, The Terminator, Commando and Predator. He was nicknamed the " +
                "\"Austrian Oak\" and the \"Styrian Oak\" in his bodybuilding days, \"Arnie\" during his acting " +
                "career and more recently the \"Governator\" (a portmanteau of \"Governor\" and \"Terminator\")."
                , person.getBiography());
        Assert.assertEquals("Thal, Styria, Austria", person.getPlaceOfBirth());
        Assert.assertEquals(false, person.getAdult());
        Assert.assertEquals("nm0000216", person.getImdbId());
        Assert.assertEquals("http://www.schwarzenegger.com", person.getHomepage());
    }

    @Test
    public void testGetMovieCredits() {
        String movieId = "280";
        MovieCreditsCrewReadDTO crew = new MovieCreditsCrewReadDTO();
        crew.setId("563");
        crew.setName("Dody Dorn");
        crew.setJob("Additional Editing");
        crew.setDepartment("Editing");
        crew.setCreditId("56b23ee3c3a36845b7000470");
        crew.setGender("1");

        MovieCreditsCastReadDTO cast = new MovieCreditsCastReadDTO();
        cast.setId("1100");
        cast.setCastId("1");
        cast.setCharacter("The Terminator");
        cast.setCreditId("52fe4231c3a36847f800b283");
        cast.setGender("2");
        cast.setName("Arnold Schwarzenegger");
        cast.setOrder("0");

        MovieCreditsReadDTO movieCredits = theMovieDbClient.getMovieCredits(movieId, null);
        Assert.assertEquals(movieId, movieCredits.getId());
        Assertions.assertThat(movieCredits.getCrew().get(0)).isEqualToComparingFieldByField(crew);
        Assertions.assertThat(movieCredits.getCast().get(0)).isEqualToComparingFieldByField(cast);
        Assert.assertEquals(movieCredits.getCrew().size(), 177);
        Assert.assertEquals(movieCredits.getCast().size(), 57);
    }
}
