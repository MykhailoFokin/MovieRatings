package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
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

public class GenreServiceTest extends BaseTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @Test
    public void testGetCountries() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenreReadDTO readDTO = genreService.getGenre(genre.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(genre, "movieId");
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(genre.getMovie().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCountriesWrongId() {
        genreService.getGenre(UUID.randomUUID());
    }

    @Test
    public void testCreateGenres() {
        Movie movie = testObjectsFactory.createMovie();
        GenreCreateDTO create = new GenreCreateDTO();
        create.setName(MovieGenreType.ACTION);
        create.setMovieId(movie.getId());
        GenreReadDTO read = genreService.createGenre(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        testObjectsFactory.inTransaction(() -> {
            Genre genre = genreRepository.findById(read.getId()).get();
            Assertions.assertThat(read).isEqualToIgnoringGivenFields(genre, "movieId");
            Assertions.assertThat(read.getMovieId()).isEqualTo(genre.getMovie().getId());
        });
    }

    @Test
    public void testPatchGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePatchDTO patch = new GenrePatchDTO();
        patch.setName(MovieGenreType.ACTION);
        patch.setMovieId(movie.getId());
        GenreReadDTO read = genreService.patchGenre(genre.getId(), patch);

        Assertions.assertThat(patch).isEqualToIgnoringGivenFields(read,"movie");
        Assertions.assertThat(patch.getMovieId()).isEqualTo(read.getMovieId());

        testObjectsFactory.inTransaction(() -> {
            Genre genre1 = genreRepository.findById(read.getId()).get();
            Assertions.assertThat(genre1).isEqualToIgnoringGivenFields(read, "movie");
            Assertions.assertThat(genre1.getMovie().getId()).isEqualTo(read.getMovieId());
        });
    }

    @Test
    public void testPatchGenresEmptyPatch() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);


        GenrePatchDTO patch = new GenrePatchDTO();
        GenreReadDTO read = genreService.patchGenre(genre.getId(), patch);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getMovieId());

        testObjectsFactory.inTransaction(() -> {
            Genre genreAfterUpdate = genreRepository.findById(read.getId()).get();

            Assert.assertNotNull(genreAfterUpdate.getName());
            Assert.assertNotNull(genreAfterUpdate.getMovie());

            Assertions.assertThat(genre).isEqualToIgnoringGivenFields(genreAfterUpdate, "movie");
            Assertions.assertThat(genre.getMovie().getId()).isEqualTo(genreAfterUpdate.getMovie().getId());
        });
    }

    @Test
    public void testDeleteGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        testObjectsFactory.inTransaction(() -> {
            genreService.deleteGenre(genre.getId());
            Assert.assertFalse(genreRepository.existsById(genre.getId()));
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGenresNotFound() {
        genreService.deleteGenre(UUID.randomUUID());
    }

    @Test
    public void testPutGenres() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        Movie movie2 = testObjectsFactory.createMovie();
        GenrePutDTO put = new GenrePutDTO();
        put.setName(MovieGenreType.ACTION);
        put.setMovieId(movie2.getId());
        GenreReadDTO read = genreService.updateGenre(genre.getId(), put);

        Assertions.assertThat(put).isEqualToIgnoringGivenFields(read,"movie");
        Assertions.assertThat(put.getMovieId()).isEqualTo(read.getMovieId());

        testObjectsFactory.inTransaction(() -> {
            Genre genre1 = genreRepository.findById(read.getId()).get();
            Assertions.assertThat(genre1).isEqualToIgnoringGivenFields(read, "movie");
            Assertions.assertThat(genre1.getMovie().getId()).isEqualTo(read.getMovieId());
        });
    }

    @Test
    public void testPutGenresEmptyPut() {
        Movie movie = testObjectsFactory.createMovie();
        Genre genre = testObjectsFactory.createGenre(movie);

        GenrePutDTO put = new GenrePutDTO();
        GenreReadDTO read = genreService.updateGenre(genre.getId(), put);

        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getMovieId());

        testObjectsFactory.inTransaction(() -> {
            Genre genreAfterUpdate = genreRepository.findById(read.getId()).get();

            Assert.assertNotNull(genreAfterUpdate.getName());
            Assert.assertNotNull(genreAfterUpdate.getMovie());
        });
    }

}
