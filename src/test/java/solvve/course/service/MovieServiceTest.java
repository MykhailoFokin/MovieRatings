package solvve.course.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
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
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCompanies("Paramount");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setFilmingLocations("USA");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setLanguages("English");
        movie.setSoundMix("DolbySurround");
        movie = movieRepository.save(movie);

        MovieReadDTO readDTO = movieService.getMovie(movie.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }

    @Test
    public void testCreateMovie() {
        MovieCreateDTO create = new MovieCreateDTO();
        create.setTitle("Movie Test");
        create.setYear((short) 2019);
        create.setGenres("Comedy");
        create.setAspectRatio("1:10");
        create.setCamera("Panasonic");
        create.setColour("Black");
        create.setCompanies("Paramount");
        create.setCritique("123");
        create.setDescription("Description");
        create.setFilmingLocations("USA");
        create.setLaboratory("CaliforniaDreaming");
        create.setLanguages("English");
        create.setSoundMix("DolbySurround");
        MovieReadDTO read = movieService.createMovie(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Movie movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movie);
    }
}
