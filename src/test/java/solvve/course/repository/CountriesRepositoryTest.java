package solvve.course.repository;

import org.junit.Test;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_prod_countries; delete from movie; delete from countries;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CountriesRepositoryTest {

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testSave() {
        Movie m = new Movie();
        m.setTitle("New movie");
        m = movieRepository.save(m);
        Set<Movie> ms = new HashSet<>();
        ms.add(m);

        Countries c = new Countries();
        c.setName("USA");
        c.setMovies(new HashSet<Movie>(ms));
        c = countriesRepository.save(c);
        assertNotNull(c.getId());
        assertTrue(countriesRepository.findById(c.getId()).isPresent());
    }
}
