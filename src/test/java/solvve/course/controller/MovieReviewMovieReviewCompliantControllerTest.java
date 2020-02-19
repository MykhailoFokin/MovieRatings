package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewMovieReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewMovieReviewCompliantController.class)
@ActiveProfiles("test")
public class MovieReviewMovieReviewCompliantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieReviewMovieReviewCompliantService movieReviewCompliantService;

    @Test
    public void testGetMovieReviewCompliant() throws Exception {
        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        List<MovieReviewCompliantReadDTO> movieReviewCompliant = List.of(
                createMovieReviewCompliantRead(movieReviewReadDTO.getId()));

        Mockito.when(movieReviewCompliantService.getMovieReviewMovieReviewCompliant(movieReviewReadDTO.getId()))
                .thenReturn(movieReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                movieReviewReadDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewCompliantReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewCompliantReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReviewCompliant);

        Mockito.verify(movieReviewCompliantService).getMovieReviewMovieReviewCompliant(movieReviewReadDTO.getId());
    }

    @Test
    public void testGetMovieReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewCompliant.class,wrongId);
        Mockito.when(movieReviewCompliantService.getMovieReviewMovieReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewCompliant() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setMovieReviewId(movieReviewReadDTO.getId());
        MovieReviewCompliantReadDTO read =
                createMovieReviewCompliantRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewCompliantService.createMovieReviewMovieReviewCompliant(movieReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                movieReviewReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliant() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();

        MovieReviewCompliantPatchDTO patchDTO = new MovieReviewCompliantPatchDTO();
        patchDTO.setDescription("Just punish him!");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patchDTO.setMovieReviewId(movieReviewReadDTO.getId());
        MovieReviewCompliantReadDTO read =
                createMovieReviewCompliantRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewCompliantService.patchMovieReviewMovieReviewCompliant(movieReviewReadDTO.getId(),
                read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}"
                ,movieReviewReadDTO.getId() ,read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testDeleteMovieReviewCompliant() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewCompliantService).deleteMovieReviewMovieReviewCompliant(id, id);
    }

    @Test
    public void testPutMovieReviewCompliant() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();

        MovieReviewCompliantPutDTO putDTO = new MovieReviewCompliantPutDTO();
        putDTO.setDescription("Just punish him!");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        putDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewCompliantReadDTO read =
                createMovieReviewCompliantRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewCompliantService.updateMovieReviewMovieReviewCompliant(movieReviewReadDTO.getId(),
                read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}"
                , movieReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    private MovieReviewCompliantReadDTO createMovieReviewCompliantRead(UUID movieReviewId) {
        MovieReviewCompliantReadDTO movieReviewCompliant = new MovieReviewCompliantReadDTO();
        movieReviewCompliant.setId(UUID.randomUUID());
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReviewCompliant.setMovieReviewId(movieReviewId);
        return movieReviewCompliant;
    }

    private MovieReviewReadDTO createMovieReview() {
        MovieReviewReadDTO movieReview = new MovieReviewReadDTO();
        movieReview.setId(UUID.randomUUID());
        movieReview.setTextReview("Review");
        return movieReview;
    }
}
