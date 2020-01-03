package solvve.course.repository;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Countries;
import solvve.course.domain.Movie;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_prod_countries; delete from movie; delete from countries;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    @Test
    public void testSave() {
        Countries c = new Countries();
        c.setName("C1");
        c = countriesRepository.save(c);
        Set<Countries> sc = new HashSet<>();
        sc.add(c);
        Movie m = new Movie();
        m.setMovieProdCountries(new HashSet<Countries>(sc));
        m = movieRepository.save(m);
        assertNotNull(m.getId());
        assertTrue(movieRepository.findById(m.getId()).isPresent());
    }
}
