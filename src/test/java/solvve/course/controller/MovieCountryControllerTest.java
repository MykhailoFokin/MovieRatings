package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import solvve.course.dto.MovieCountryReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieCountryService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieCountryController.class)
public class MovieCountryControllerTest extends BaseControllerTest {

    @MockBean
    private MovieCountryService movieCountryService;

    @Test
    public void testAddCountryToMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        MovieCountryReadDTO read = new MovieCountryReadDTO();
        read.setId(countryId);
        List<MovieCountryReadDTO> expectedcountries = List.of(read);
        Mockito.when(movieCountryService.addCountryToMovie(movieId, countryId)).thenReturn(expectedcountries);

        String resultJson = mvc.perform(post("/api/v1/movies/{movieId}/countries/{id}", movieId, countryId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieCountryReadDTO> actualcountries = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieCountryReadDTO>>() {
                });
        Assert.assertEquals(expectedcountries, actualcountries);
    }

    @Test
    public void testRemoveCountryFromMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/{movieId}/countries/{id}",movieId, countryId))
                .andExpect(status().isOk());

        Mockito.verify(movieCountryService).removeCountryFromMovie(movieId, countryId);
    }

    @Test
    public void testGetMovieCountries() throws Exception {
        UUID movieId = UUID.randomUUID();

        List<MovieCountryReadDTO> countries = List.of(generateObject(MovieCountryReadDTO.class));

        Mockito.when(movieCountryService.getMovieCountries(movieId)).thenReturn(countries);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/countries", movieId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieCountryReadDTO> actualcountries = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieCountryReadDTO>>(){});
        Assertions.assertThat(actualcountries).isEqualTo(countries);

        Mockito.verify(movieCountryService).getMovieCountries(movieId);
    }

    @Test
    public void testGetMovieCountryWrongId() throws Exception {
        UUID movieId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieCountryReadDTO.class, movieId);
        Mockito.when(movieCountryService.getMovieCountries(movieId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/countries", movieId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }
}
