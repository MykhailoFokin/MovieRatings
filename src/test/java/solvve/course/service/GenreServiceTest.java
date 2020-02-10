package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Genre;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieGenreType;
import solvve.course.dto.GenreCreateDTO;
import solvve.course.dto.GenrePatchDTO;
import solvve.course.dto.GenrePutDTO;
import solvve.course.dto.GenreReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GenreRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from genre",
        "delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GenreServiceTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetCountries() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenreReadDTO readDTO = genreService.getGenre(genre.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(genre,"movieId");
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(genre.getMovieId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        genreService.getGenre(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateGenres() {
        Movie movie = testObjectsFactory.createMovie();
        GenreCreateDTO create = new GenreCreateDTO();
        create.setName(MovieGenreType.ACTION);
        create.setMovieId(movie.getId());
        GenreReadDTO read = genreService.createGenre(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Genre genre = genreRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(genre, "movieId");
        Assertions.assertThat(read.getMovieId()).isEqualTo(genre.getMovieId().getId());
    }

    @Transactional
    @Test
    public void testPatchGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePatchDTO patch = new GenrePatchDTO();
        patch.setName(MovieGenreType.ACTION);
        patch.setMovieId(movie.getId());
        GenreReadDTO read = genreService.patchGenre(genre.getId(), patch);

        Assertions.assertThat(patch).isEqualToIgnoringGivenFields(read,"movieId");
        Assertions.assertThat(patch.getMovieId()).isEqualTo(read.getMovieId());

        genre = genreRepository.findById(read.getId()).get();
        Assertions.assertThat(genre).isEqualToIgnoringGivenFields(read,"movieId");
        Assertions.assertThat(genre.getMovieId().getId()).isEqualTo(read.getMovieId());
    }

    @Transactional
    @Test
    public void testPatchGenresEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePatchDTO patch = new GenrePatchDTO();
        GenreReadDTO read = genreService.patchGenre(genre.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getMovieId());

        Genre genreAfterUpdate = genreRepository.findById(read.getId()).get();

        Assert.assertNotNull(genreAfterUpdate.getName());
        Assert.assertNotNull(genreAfterUpdate.getMovieId());

        Assertions.assertThat(genre).isEqualToIgnoringGivenFields(genreAfterUpdate,"movieId");
        Assertions.assertThat(genre.getMovieId()).isEqualTo(genreAfterUpdate.getMovieId());
    }

    @Test
    public void testDeleteGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        genreService.deleteGenre(genre.getId());
        Assert.assertFalse(genreRepository.existsById(genre.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGenresNotFound() {
        genreService.deleteGenre(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePutDTO put = new GenrePutDTO();
        put.setName(MovieGenreType.ACTION);
        GenreReadDTO read = genreService.updateGenre(genre.getId(), put);

        Assertions.assertThat(put).isEqualToIgnoringGivenFields(read,"movieId");
        Assertions.assertThat(put.getMovieId()).isEqualTo(read.getMovieId());

        genre = genreRepository.findById(read.getId()).get();
        Assertions.assertThat(genre).isEqualToIgnoringGivenFields(read,"movieId");
        Assertions.assertThat(genre.getMovieId().getId()).isEqualTo(read.getMovieId());
    }

    @Transactional
    @Test
    public void testPutGenresEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePutDTO put = new GenrePutDTO();
        GenreReadDTO read = genreService.updateGenre(genre.getId(), put);

        Assert.assertNull(read.getName());

        Genre genreAfterUpdate = genreRepository.findById(read.getId()).get();

        Assert.assertNull(genreAfterUpdate.getName());

        Assertions.assertThat(genre).isEqualToComparingFieldByField(genreAfterUpdate);
    }

}
