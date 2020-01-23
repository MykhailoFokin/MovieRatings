package solvve.course.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
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

    private Movie createMovie() {
        Movie movie = new Movie();
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
        movie.setIsPublished(true);
        return movieRepository.save(movie);
    }

    @Transactional
    @Test
    public void testGetMovie() {
        Movie movie = createMovie();

        MovieReadDTO readDTO = movieService.getMovie(movie.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }

    @Transactional
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
        create.setIsPublished(true);
        MovieReadDTO read = movieService.createMovie(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Movie movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(movie);
    }

    @Transactional
    @Test
    public void testPatchMovie() {
        Movie movie = createMovie();

        MoviePatchDTO patch = new MoviePatchDTO();
        patch.setTitle("Movie Test");
        patch.setYear((short) 2019);
        patch.setGenres("Comedy");
        patch.setAspectRatio("1:10");
        patch.setCamera("Panasonic");
        patch.setColour("Black");
        patch.setCompanies("Paramount");
        patch.setCritique("123");
        patch.setDescription("Description");
        patch.setFilmingLocations("USA");
        patch.setLaboratory("CaliforniaDreaming");
        patch.setLanguages("English");
        patch.setSoundMix("DolbySurround");
        patch.setIsPublished(true);
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        movie = movieRepository.findById(read.getId()).get();
        Assertions.assertThat(movie).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchMovieEmptyPatch() {
        Movie movie = createMovie();

        MoviePatchDTO patch = new MoviePatchDTO();
        MovieReadDTO read = movieService.patchMovie(movie.getId(), patch);

        Assert.assertNotNull(read.getTitle());
        Assert.assertNotNull(read.getYear());
        Assert.assertNotNull(read.getGenres());
        Assert.assertNotNull(read.getAspectRatio());
        Assert.assertNotNull(read.getCamera());
        Assert.assertNotNull(read.getColour());
        Assert.assertNotNull(read.getCompanies());
        Assert.assertNotNull(read.getCritique());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getFilmingLocations());
        Assert.assertNotNull(read.getLaboratory());
        Assert.assertNotNull(read.getLanguages());
        Assert.assertNotNull(read.getSoundMix());
        Assert.assertNotNull(read.getIsPublished());

        Movie movieAfterUpdate = movieRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieAfterUpdate.getTitle());
        Assert.assertNotNull(movieAfterUpdate.getYear());
        Assert.assertNotNull(movieAfterUpdate.getGenres());
        Assert.assertNotNull(movieAfterUpdate.getAspectRatio());
        Assert.assertNotNull(movieAfterUpdate.getCamera());
        Assert.assertNotNull(movieAfterUpdate.getColour());
        Assert.assertNotNull(movieAfterUpdate.getCompanies());
        Assert.assertNotNull(movieAfterUpdate.getCritique());
        Assert.assertNotNull(movieAfterUpdate.getDescription());
        Assert.assertNotNull(movieAfterUpdate.getFilmingLocations());
        Assert.assertNotNull(movieAfterUpdate.getLaboratory());
        Assert.assertNotNull(movieAfterUpdate.getLanguages());
        Assert.assertNotNull(movieAfterUpdate.getSoundMix());
        Assert.assertNotNull(movieAfterUpdate.getIsPublished());

        Assertions.assertThat(movie).isEqualToComparingFieldByField(movieAfterUpdate);
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = createMovie();

        movieService.deleteMovie(movie.getId());
        Assert.assertFalse(movieRepository.existsById(movie.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieNotFound() {
        movieService.deleteMovie(UUID.randomUUID());
    }
}
