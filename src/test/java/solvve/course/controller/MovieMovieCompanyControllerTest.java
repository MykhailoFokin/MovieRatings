package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import solvve.course.dto.MovieMovieCompanyReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieMovieCompanyService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieMovieCompanyController.class)
public class MovieMovieCompanyControllerTest extends BaseControllerTest {

    @MockBean
    private MovieMovieCompanyService movieMovieCompanyService;

    @Test
    public void testAddMovieCompanyToMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID movieCompanyId = UUID.randomUUID();

        MovieMovieCompanyReadDTO read = new MovieMovieCompanyReadDTO();
        read.setId(movieCompanyId);
        List<MovieMovieCompanyReadDTO> expectedCompanies = List.of(read);
        Mockito.when(movieMovieCompanyService.addMovieCompanyToMovie(movieId, movieCompanyId))
                .thenReturn(expectedCompanies);

        String resultJson = mvc.perform(post("/api/v1/movies/{movieId}/movie-companies/{id}", movieId, movieCompanyId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieMovieCompanyReadDTO> actualCompanies = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieMovieCompanyReadDTO>>() {
                });
        Assert.assertEquals(expectedCompanies, actualCompanies);
    }

    @Test
    public void testRemoveMovieCompanyFromMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID movieCompanyId = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/{movieId}/movie-companies/{id}",movieId, movieCompanyId))
                .andExpect(status().isOk());

        Mockito.verify(movieMovieCompanyService).removeMovieCompanyFromMovie(movieId, movieCompanyId);
    }

    @Test
    public void testGetMovieCompanies() throws Exception {
        UUID movieId = UUID.randomUUID();

        List<MovieMovieCompanyReadDTO> companies = List.of(generateObject(MovieMovieCompanyReadDTO.class));

        Mockito.when(movieMovieCompanyService.getMovieCompanies(movieId)).thenReturn(companies);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/movie-companies", movieId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieMovieCompanyReadDTO> actualcompanies = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieMovieCompanyReadDTO>>(){});
        Assertions.assertThat(actualcompanies).isEqualTo(companies);

        Mockito.verify(movieMovieCompanyService).getMovieCompanies(movieId);
    }

    @Test
    public void testGetMovieMovieCompanyWrongId() throws Exception {
        UUID movieId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieMovieCompanyReadDTO.class, movieId);
        Mockito.when(movieMovieCompanyService.getMovieCompanies(movieId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/movie-companies", movieId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }
}
