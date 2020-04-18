package solvve.course.client.themoviedb;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.client.themoviedb.dto.*;

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
