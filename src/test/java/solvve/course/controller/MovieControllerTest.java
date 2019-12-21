package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import solvve.course.dto.MovieReadDTO;
import solvve.course.service.MovieService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieController.class)
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
}
