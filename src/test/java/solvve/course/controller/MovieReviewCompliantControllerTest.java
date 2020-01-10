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
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.dto.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetMovieReviewCompliant() throws Exception {
        MovieReviewCompliantReadDTO movieReviewCompliant = new MovieReviewCompliantReadDTO();
        movieReviewCompliant.setId(UUID.randomUUID());
        //movieReviewCompliant.setUserId(portalUserReadDTO.getId());
        //movieReviewCompliant.setMovieId(movieReadDTO.getId());
        //movieReviewCompliant.setMovieReviewId(movieReviewReadDTO.getId());
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //movieReviewCompliant.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId())).thenReturn(movieReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliant/{id}", movieReviewCompliant.getId()))
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

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliant/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliant/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieReviewCompliant() throws Exception {

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setMovieId(movieReadDTO.getId());
        //create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //create.setModeratorId(portalUserReadDTO.getId());

        MovieReviewCompliantReadDTO read = new MovieReviewCompliantReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setMovieId(movieReadDTO.getId());
        //read.setMovieReviewId(movieReviewReadDTO.getId());
        read.setDescription("Just punish him!");
        read.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //read.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(movieReviewCompliantService.createMovieReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliant")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }
}
