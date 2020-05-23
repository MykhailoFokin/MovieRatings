package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.domain.PortalUser;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.ModeratorMovieReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = ModeratorMovieReviewCompliantController.class)
public class ModeratorMovieReviewCompliantControllerTest extends BaseControllerTest {

    @MockBean
    private ModeratorMovieReviewCompliantService moderatorMovieReviewCompliantService;

    @Test
    public void testGetCreatedMovieReviewCompliants() throws Exception {
        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);
        PortalUserReadDTO moderatorDTO = generateObject(PortalUserReadDTO.class);
        List<MovieReviewCompliantReadDTO> movieReviewCompliant = List.of(
                createMovieReviewCompliantRead(movieReviewReadDTO.getId(), moderatorDTO.getId()));

        Mockito.when(moderatorMovieReviewCompliantService.getCreatedMovieReviewCompliants(moderatorDTO.getId()))
                .thenReturn(movieReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/moderator/{moderatorId}/movie-review-compliants",
                moderatorDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewCompliantReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewCompliantReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReviewCompliant);

        Mockito.verify(moderatorMovieReviewCompliantService).getCreatedMovieReviewCompliants(moderatorDTO.getId());
    }

    @Test
    public void testGetCreatedMovieReviewCompliantsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewCompliant.class,wrongId);
        Mockito.when(moderatorMovieReviewCompliantService.getCreatedMovieReviewCompliants(wrongId))
                .thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moderator/{moderatorId}/movie-review-compliants",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCreatedMovieReviewCompliantsWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/moderator/{moderatorId}/movie-review-compliants"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testPatchMovieReviewCompliant() throws Exception {

        PortalUserReadDTO moderatorDTO = generateObject(PortalUserReadDTO.class);
        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);

        MovieReviewCompliantPatchDTO patchDTO = generateObject(MovieReviewCompliantPatchDTO.class);
        patchDTO.setMovieReviewId(movieReviewReadDTO.getId());
        MovieReviewCompliantReadDTO read =
                createMovieReviewCompliantRead(movieReviewReadDTO.getId(), moderatorDTO.getId());

        Mockito.when(moderatorMovieReviewCompliantService.patchMovieReviewCompliantByModeratedStatus(
                read.getId(), patchDTO, moderatorDTO.getId())).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/moderator/{moderatorId}/movie-review-compliants/{id}"
                , moderatorDTO.getId(), read.getId())
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

        mvc.perform(delete("/api/v1/moderator/{moderatorId}/movie-review-compliants/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(moderatorMovieReviewCompliantService).deleteMovieReviewByCompliantByModerator(id, id);
    }

    private MovieReviewCompliantReadDTO createMovieReviewCompliantRead(UUID movieReviewId, UUID moderatorId) {
        MovieReviewCompliantReadDTO movieReviewCompliant = generateObject(MovieReviewCompliantReadDTO.class);
        movieReviewCompliant.setMovieReviewId(movieReviewId);
        movieReviewCompliant.setModeratorId(moderatorId);
        return movieReviewCompliant;
    }
}
