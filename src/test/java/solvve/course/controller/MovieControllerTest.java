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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
import solvve.course.dto.MoviePutDTO;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private MovieReadDTO createMovieRead() {
        MovieReadDTO movie = new MovieReadDTO();
        movie.setId(UUID.randomUUID());
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setSoundMix("DolbySurround");
        return movie;
    }

    @Test
    public void testGetMovie() throws Exception {

        MovieReadDTO movie = createMovieRead();

        Mockito.when(movieService.getMovie(movie.getId())).thenReturn(movie);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", movie.getId()))
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

        String resultJson = mvc.perform(get("/api/v1/movies/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/movies/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovie() throws Exception {

        MovieCreateDTO create = new MovieCreateDTO();
        create.setTitle("Movie Test");
        create.setYear((short) 2019);
        create.setAspectRatio("1:10");
        create.setCamera("Panasonic");
        create.setColour("Black");
        create.setCritique("123");
        create.setDescription("Description");
        create.setLaboratory("CaliforniaDreaming");
        create.setSoundMix("DolbySurround");

        MovieReadDTO read = createMovieRead();

        Mockito.when(movieService.createMovie(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovie() throws Exception {

        MoviePatchDTO patchDTO = new MoviePatchDTO();
        patchDTO.setTitle("Movie Test");
        patchDTO.setYear((short) 2019);
        patchDTO.setAspectRatio("1:10");
        patchDTO.setCamera("Panasonic");
        patchDTO.setColour("Black");
        patchDTO.setCritique("123");
        patchDTO.setDescription("Description");
        patchDTO.setLaboratory("CaliforniaDreaming");
        patchDTO.setSoundMix("DolbySurround");

        MovieReadDTO read = createMovieRead();

        Mockito.when(movieService.patchMovie(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }

    @Test
    public void testDeleteMovie() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieService).deleteMovie(id);
    }

    @Test
    public void testPutMovie() throws Exception {

        MoviePutDTO putDTO = new MoviePutDTO();
        putDTO.setTitle("Movie Test");
        putDTO.setYear((short) 2019);
        putDTO.setAspectRatio("1:10");
        putDTO.setCamera("Panasonic");
        putDTO.setColour("Black");
        putDTO.setCritique("123");
        putDTO.setDescription("Description");
        putDTO.setLaboratory("CaliforniaDreaming");
        putDTO.setSoundMix("DolbySurround");

        MovieReadDTO read = createMovieRead();

        Mockito.when(movieService.putMovie(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }
}
