package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.LanguageType;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieLanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieLanguageService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = MovieLanguageController.class)
public class MovieLanguageControllerTest extends BaseControllerTest {

    @MockBean
    private MovieLanguageService movieLanguageService;

    @Test
    public void testAddLanguageToMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID languageId = UUID.randomUUID();

        MovieLanguageReadDTO read = new MovieLanguageReadDTO();
        read.setId(languageId);
        read.setName(LanguageType.JAPANESE);
        List<MovieLanguageReadDTO> expectedLanguages = List.of(read);
        Mockito.when(movieLanguageService.addLanguageToMovie(movieId, languageId)).thenReturn(expectedLanguages);

        String resultJson = mvc.perform(post("/api/v1/movies/{movieId}/languages/{id}", movieId, languageId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieLanguageReadDTO> actualLanguages = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieLanguageReadDTO>>() {
        });
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testRemoveLanguageFromMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID languageId = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/{movieId}/languages/{id}",movieId, languageId))
                .andExpect(status().isOk());

        Mockito.verify(movieLanguageService).removeLanguageFromMovie(movieId, languageId);
    }

    @Test
    public void testGetMovieLanguages() throws Exception {
        UUID movieId = UUID.randomUUID();

        List<MovieLanguageReadDTO> languages = List.of(generateObject(MovieLanguageReadDTO.class));

        Mockito.when(movieLanguageService.getMovieLanguages(movieId)).thenReturn(languages);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/languages", movieId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieLanguageReadDTO> actualLanguages = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieLanguageReadDTO>>(){});
        Assertions.assertThat(actualLanguages).isEqualTo(languages);

        Mockito.verify(movieLanguageService).getMovieLanguages(movieId);
    }

    @Test
    public void testGetMovieLanguageWrongId() throws Exception {
        UUID movieId = UUID.randomUUID();
        UUID languageId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieLanguageReadDTO.class, movieId);
        Mockito.when(movieLanguageService.getMovieLanguages(movieId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/languages", movieId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }
}
