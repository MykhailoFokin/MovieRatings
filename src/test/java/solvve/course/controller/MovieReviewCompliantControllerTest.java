package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewCompliantController.class)
@ActiveProfiles("test")
public class MovieReviewCompliantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieReviewCompliantService movieReviewCompliantService;

    private MovieReviewCompliantReadDTO createMovieReviewCompliantRead() {
        MovieReviewCompliantReadDTO movieReviewCompliant = new MovieReviewCompliantReadDTO();
        movieReviewCompliant.setId(UUID.randomUUID());
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        return movieReviewCompliant;
    }

    @Test
    public void testGetMovieReviewCompliant() throws Exception {
        MovieReviewCompliantReadDTO movieReviewCompliant = createMovieReviewCompliantRead();

        Mockito.when(movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId())).thenReturn(movieReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}", movieReviewCompliant.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieReviewCompliantReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieReviewCompliant);

        Mockito.verify(movieReviewCompliantService).getMovieReviewCompliant(movieReviewCompliant.getId());
    }

    @Test
    public void testGetMovieReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewCompliant.class,wrongId);
        Mockito.when(movieReviewCompliantService.getMovieReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewCompliant() throws Exception {

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead();

        Mockito.when(movieReviewCompliantService.createMovieReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliant() throws Exception {

        MovieReviewCompliantPatchDTO patchDTO = new MovieReviewCompliantPatchDTO();
        patchDTO.setDescription("Just punish him!");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead();

        Mockito.when(movieReviewCompliantService.patchMovieReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testDeleteMovieReviewCompliant() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviereviewcompliants/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewCompliantService).deleteMovieReviewCompliant(id);
    }

    @Test
    public void testPutMovieReviewCompliant() throws Exception {

        MovieReviewCompliantPutDTO putDTO = new MovieReviewCompliantPutDTO();
        putDTO.setDescription("Just punish him!");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead();

        Mockito.when(movieReviewCompliantService.putMovieReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }
}
