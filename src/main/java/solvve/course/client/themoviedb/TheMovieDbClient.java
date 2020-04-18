package solvve.course.client.themoviedb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import solvve.course.client.themoviedb.dto.MovieCreditsReadDTO;
import solvve.course.client.themoviedb.dto.MovieReadDTO;
import solvve.course.client.themoviedb.dto.MoviesPageDTO;
import solvve.course.client.themoviedb.dto.PersonReadDTO;

@FeignClient(value = "api.themoviedb.org", url = "${themoviedb.api.url}", configuration = TheMovieDbClientConfig.class)
public interface TheMovieDbClient {

    @RequestMapping(method = RequestMethod.GET, value = "/movie/{movieId}")
    MovieReadDTO getMovie(@PathVariable("movieId") String movieId, @RequestParam String language);

    @RequestMapping(method = RequestMethod.GET, value = "/movie/top_rated")
    MoviesPageDTO getTopRatedMovies();

    @RequestMapping(method = RequestMethod.GET, value = "/person/{personId}")
    PersonReadDTO getPerson(@PathVariable("personId") String personId);

    @RequestMapping(method = RequestMethod.GET, value = "/movie/{movieId}/credits")
    MovieCreditsReadDTO getMovieCredits(@PathVariable("movieId") String movieId, @RequestParam String language);
}
