package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;

import java.util.HashSet;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_prod_countries; delete from movie; delete from country;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testSave() {
        Movie m = new Movie();
        m.setTitle("New movie");
        m = movieRepository.save(m);
        Set<Movie> ms = new HashSet<>();
        ms.add(m);

        Country c = new Country();
        c.setName("USA");
        c.setMovies(new HashSet<Movie>(ms));
        c = countryRepository.save(c);
        assertNotNull(c.getId());
        assertTrue(countryRepository.findById(c.getId()).isPresent());
    }
}
