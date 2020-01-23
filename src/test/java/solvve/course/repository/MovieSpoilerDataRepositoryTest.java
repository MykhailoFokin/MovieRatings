package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.MovieSpoilerData;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie_spoiler_data; delete from movie_review; delete from movie;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieSpoilerDataRepositoryTest {

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Test
    public void testSave() {
        Movie movie = new Movie();
        movie = movieRepository.save(movie);

        MovieReview movieReview = new MovieReview();
        movieReview.setMovieId(movie);
        movieReview = movieReviewRepository.save(movieReview);

        MovieSpoilerData r = new MovieSpoilerData();
        r.setMovieReviewId(movieReview);
        r = movieSpoilerDataRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(movieSpoilerDataRepository.findById(r.getId()).isPresent());
    }
}
