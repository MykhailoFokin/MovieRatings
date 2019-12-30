package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import solvve.course.domain.Movie;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieController.class)
@ActiveProfiles("test")
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovie() throws Exception {

        MovieReadDTO movie = new MovieReadDTO();
        movie.setId(UUID.randomUUID());
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setGenres("Comedy");

        Mockito.when(movieService.getMovie(movie.getId())).thenReturn(movie);

        String resultJson = mvc.perform(get("/api/v1/movie/{id}", movie.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movie);

        Mockito.verify(movieService).getMovie(movie.getId());
    }

    @Test
    public void testGetMovieWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Movie.class,wrongId);
        Mockito.when(movieService.getMovie(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movie/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetMovieWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }
}
