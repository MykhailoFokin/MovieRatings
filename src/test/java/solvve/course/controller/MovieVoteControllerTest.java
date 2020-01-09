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
import solvve.course.domain.MovieVote;
import solvve.course.domain.UserVoteRatingType;
import solvve.course.dto.MovieVoteCreateDTO;
import solvve.course.dto.MovieVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieVoteService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetMovieVote() throws Exception {
        MovieVoteReadDTO movieVote = new MovieVoteReadDTO();
        movieVote.setId(UUID.randomUUID());
        //movieVote.setMovieId(movieReadDTO.getId());
        //movieVote.setUserId(portalUserReadDTO.getId());
        movieVote.setRating(UserVoteRatingType.valueOf("R9"));

        Mockito.when(movieVoteService.getMovieVote(movieVote.getId())).thenReturn(movieVote);

        String resultJson = mvc.perform(get("/api/v1/movievote/{id}", movieVote.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieVoteReadDTO actualMovie = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieVote);

        Mockito.verify(movieVoteService).getMovieVote(movieVote.getId());
    }

    @Test
    public void testGetMovieVoteWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieVote.class,wrongId);
        Mockito.when(movieVoteService.getMovieVote(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movievote/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetMovieVoteWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }

    @Test
    public void testCreateMovieVote() throws Exception {

        MovieVoteCreateDTO create = new MovieVoteCreateDTO();
        //create.setMovieId(movieReadDTO.getId());
        //create.setUserId(portalUserReadDTO.getId());
        create.setRating(UserVoteRatingType.valueOf("R9"));

        MovieVoteReadDTO read = new MovieVoteReadDTO();
        read.setId(UUID.randomUUID());
        //read.setMovieId(movieReadDTO.getId());
        //read.setUserId(portalUserReadDTO.getId());
        read.setRating(UserVoteRatingType.valueOf("R9"));

        Mockito.when(movieVoteService.createMovieVote(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movievote")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieVoteReadDTO actualMovieVote = objectMapper.readValue(resultJson, MovieVoteReadDTO.class);
        Assertions.assertThat(actualMovieVote).isEqualToComparingFieldByField(read);
    }
}
