package solvve.course.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieRepository;
import org.assertj.core.api.Assertions;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Test
    public void testGetMovie() {
        Movie movie = new Movie();
        movie.setId(UUID.randomUUID());
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setGenres("Comedy");
        movie = movieRepository.save(movie);

        MovieReadDTO readDTO = movieService.getMovie(movie.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }
}
