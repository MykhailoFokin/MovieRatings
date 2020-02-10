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
import solvve.course.domain.MovieVote;
import solvve.course.domain.UserVoteRatingType;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVotePatchDTO;
import solvve.course.dto.MovieVotePutDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieVoteService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieVoteController.class)
@ActiveProfiles("test")
public class MovieVoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieVoteService movieVoteService;

    private MovieVoteReadDTO createMovieVoteRead() {
        MovieVoteReadDTO movieVote = new MovieVoteReadDTO();
        movieVote.setId(UUID.randomUUID());
        movieVote.setRating(UserVoteRatingType.R9);
        return movieVote;
    }

    @Test
    public void testGetMovieVote() throws Exception {
        MovieVoteReadDTO movieVote = createMovieVoteRead();

        Mockito.when(movieVoteService.getMovieVote(movieVote.getId())).thenReturn(movieVote);

        String resultJson = mvc.perform(get("/api/v1/movievotes/{id}", movieVote.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieVoteReadDTO actualMovie = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieVote);

        Mockito.verify(movieVoteService).getMovieVote(movieVote.getId());
    }

    @Test
    public void testGetMovieVoteWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieVote.class,wrongId);
        Mockito.when(movieVoteService.getMovieVote(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movievotes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieVoteWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/movievotes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieVote() throws Exception {

        MovieVoteCreateDTO create = new MovieVoteCreateDTO();
        create.setRating(UserVoteRatingType.R9);

        MovieVoteReadDTO read = createMovieVoteRead();

        Mockito.when(movieVoteService.createMovieVote(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movievotes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieVoteReadDTO actualMovieVote = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assertions.assertThat(actualMovieVote).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieVote() throws Exception {

        MovieVotePatchDTO patchDTO = new MovieVotePatchDTO();
        patchDTO.setRating(UserVoteRatingType.R9);

        MovieVoteReadDTO read = createMovieVoteRead();

        Mockito.when(movieVoteService.patchMovieVote(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/movievotes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieVoteReadDTO actualMovieVote = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assert.assertEquals(read, actualMovieVote);
    }

    @Test
    public void testDeleteMovieVote() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movievotes/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieVoteService).deleteMovieVote(id);
    }

    @Test
    public void testPutMovieVote() throws Exception {

        MovieVotePutDTO putDTO = new MovieVotePutDTO();
        putDTO.setRating(UserVoteRatingType.R9);

        MovieVoteReadDTO read = createMovieVoteRead();

        Mockito.when(movieVoteService.updateMovieVote(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movievotes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieVoteReadDTO actualMovieVote = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assert.assertEquals(read, actualMovieVote);
    }
}
