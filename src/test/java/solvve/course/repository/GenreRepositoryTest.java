package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Genre;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieGenreType;
import solvve.course.dto.GenreFilter;
import solvve.course.service.GenreService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from genre",
        "delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GenreRepositoryTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testGetGenresByEmptyFilter() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Genre g1 = testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        Genre g2 = testObjectsFactory.createGenre(m1, MovieGenreType.HISTORICAL);
        Genre g3 = testObjectsFactory.createGenre(m2, MovieGenreType.WAR);
        Genre g4 = testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        GenreFilter filter = new GenreFilter();
        Assertions.assertThat(genreService.getGenres(filter)).extracting("Id")
                .containsExactlyInAnyOrder(g1.getId(),g2.getId(),g3.getId(),g4.getId());
    }

    @Test
    public void testGetGenresByMovie() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        Genre g1 = testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        Genre g2 = testObjectsFactory.createGenre(m1, MovieGenreType.HISTORICAL);
        testObjectsFactory.createGenre(m2, MovieGenreType.WAR);
        testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        GenreFilter filter = new GenreFilter();
        filter.setMovieId(m1.getId());
        Assertions.assertThat(genreService.getGenres(filter)).extracting("Id")
                .containsExactlyInAnyOrder(g1.getId(),g2.getId());
    }

    @Test
    public void testGetGenresByName() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        Genre g2 = testObjectsFactory.createGenre(m1, MovieGenreType.WAR);
        Genre g3 = testObjectsFactory.createGenre(m2, MovieGenreType.HISTORICAL);
        testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        GenreFilter filter = new GenreFilter();
        filter.setGenres(List.of(MovieGenreType.HISTORICAL, MovieGenreType.WAR));
        Assertions.assertThat(genreService.getGenres(filter)).extracting("Id")
                .containsExactlyInAnyOrder(g2.getId(),g3.getId());
    }

    @Test
    public void testGetGenresByAllFilters() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        testObjectsFactory.createGenre(m2, MovieGenreType.HISTORICAL);
        Genre g3 = testObjectsFactory.createGenre(m3, MovieGenreType.WAR);
        Genre g4 = testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        GenreFilter filter = new GenreFilter();
        filter.setMovieId(m3.getId());
        filter.setGenres(List.of(MovieGenreType.WAR, MovieGenreType.DRAMA));
        Assertions.assertThat(genreService.getGenres(filter)).extracting("Id")
                .containsExactlyInAnyOrder(g3.getId(),g4.getId());
    }

    @Test
    public void testGetGenreOrderByMovieIdAsc() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        Genre g2 = testObjectsFactory.createGenre(m2, MovieGenreType.ACTION);
        Genre g3 = testObjectsFactory.createGenre(m2, MovieGenreType.SAGA);
        testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        List<Genre> res = genreRepository.findByMovieIdOrderByNameAsc(m2.getId());
        Assertions.assertThat(res).extracting(Genre::getId).isEqualTo(Arrays.asList(g2.getId(), g3.getId()));
    }

    @Test
    public void testFindGenreForMovieAndGenreName() {
        Movie m1 = testObjectsFactory.createMovie();
        Movie m2 = testObjectsFactory.createMovie();
        Movie m3 = testObjectsFactory.createMovie();
        testObjectsFactory.createGenre(m1, MovieGenreType.ADVENTURE);
        testObjectsFactory.createGenre(m2, MovieGenreType.HISTORICAL);
        Genre g3 = testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);
        Genre g4 = testObjectsFactory.createGenre(m3, MovieGenreType.DRAMA);

        List<Genre> res = genreRepository.findGenreForMovieAndGenreName(m3.getId(), MovieGenreType.DRAMA);
        Assertions.assertThat(res).extracting(Genre::getId).isEqualTo(Arrays.asList(g3.getId(), g4.getId()));
    }

    @Test
    public void testCteatedAtIsSet() {
        Movie m = testObjectsFactory.createMovie();
        Genre entity = testObjectsFactory.createGenre(m, MovieGenreType.ADVENTURE);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = genreRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        Movie m = testObjectsFactory.createMovie();
        Genre entity = testObjectsFactory.createGenre(m, MovieGenreType.ADVENTURE);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = genreRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        Movie m = testObjectsFactory.createMovie();
        Genre entity = testObjectsFactory.createGenre(m, MovieGenreType.ADVENTURE);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setName(MovieGenreType.SAGA);
        genreRepository.save(entity);
        entity = genreRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
