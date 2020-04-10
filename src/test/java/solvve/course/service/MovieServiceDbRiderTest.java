package solvve.course.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.repository.MovieRepository;

import java.util.UUID;

@DBRider
public class MovieServiceDbRiderTest extends BaseTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DataSet(value = "/datasets/testUpdateAverageRatingOfMovie.xml")
    @ExpectedDataSet(value = "/datasets/testUpdateAverageRatingOfMovie_result.xml")
    public void testUpdateAverageRatingOfMovie() {
        UUID movieId = UUID.fromString("6cd81a37-0e32-4282-8c88-8ff01f9c3aca");
        movieService.updateAverageRatingOfMovie(movieId);
    }
}
