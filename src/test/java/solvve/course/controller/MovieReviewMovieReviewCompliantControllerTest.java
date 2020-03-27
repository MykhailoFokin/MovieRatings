package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewMovieReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieReviewMovieReviewCompliantController.class)
public class MovieReviewMovieReviewCompliantControllerTest extends BaseControllerTest {

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
        create.setPortalUserId(UUID.randomUUID());
        create.setMovieId(UUID.randomUUID());
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
        putDTO.setMovieId(UUID.randomUUID());
        putDTO.setPortalUserId(UUID.randomUUID());

        MovieReviewCompliantReadDTO read =
                createMovieReviewCompliantRead(movieReviewReadDTO.getId());
        read.setMovieId(putDTO.getMovieId());
        read.setPortalUserId(putDTO.getPortalUserId());

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

    @Test
    public void testCreateMovieReviewCompliantValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants"
                , UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).createMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewCompliantValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}"
                , UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).updateMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any()
                , ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewCompliantCheckLimitBorders() throws Exception {

        MovieReviewCompliantPutDTO putDTO = new MovieReviewCompliantPutDTO();
        putDTO.setDescription("D");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        putDTO.setMovieReviewId(UUID.randomUUID());
        putDTO.setPortalUserId(UUID.randomUUID());
        putDTO.setMovieId(UUID.randomUUID());

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead(putDTO.getMovieReviewId());

        Mockito.when(movieReviewCompliantService.updateMovieReviewMovieReviewCompliant(putDTO.getMovieReviewId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                putDTO.getMovieReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                putDTO.getMovieReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        put.setDescription("");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setMovieReviewId(UUID.randomUUID());
        put.setPortalUserId(UUID.randomUUID());
        put.setMovieId(UUID.randomUUID());

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                UUID.randomUUID(),
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).updateMovieReviewMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();
        put.setDescription(StringUtils.repeat("*", 1001));
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setMovieReviewId(UUID.randomUUID());
        put.setPortalUserId(UUID.randomUUID());
        put.setMovieId(UUID.randomUUID());

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                UUID.randomUUID(),
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).updateMovieReviewMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setDescription("");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setMovieReviewId(UUID.randomUUID());
        create.setPortalUserId(UUID.randomUUID());
        create.setMovieId(UUID.randomUUID());

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).createMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setDescription(StringUtils.repeat("*", 1001));
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setMovieReviewId(UUID.randomUUID());
        create.setPortalUserId(UUID.randomUUID());
        create.setMovieId(UUID.randomUUID());


        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).createMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieReviewCompliantCheckStingBorders() throws Exception {

        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();
        create.setDescription("D");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setMovieReviewId(UUID.randomUUID());
        create.setPortalUserId(UUID.randomUUID());
        create.setMovieId(UUID.randomUUID());

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead(create.getMovieReviewId());

        Mockito.when(movieReviewCompliantService.createMovieReviewMovieReviewCompliant(create.getMovieReviewId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                create.getMovieReviewId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants",
                create.getMovieReviewId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliantCheckStringBorders() throws Exception {

        MovieReviewCompliantPatchDTO patchDTO = new MovieReviewCompliantPatchDTO();
        patchDTO.setDescription("D");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patchDTO.setMovieReviewId(UUID.randomUUID());
        patchDTO.setPortalUserId(UUID.randomUUID());
        patchDTO.setMovieId(UUID.randomUUID());

        MovieReviewCompliantReadDTO read = createMovieReviewCompliantRead(patchDTO.getMovieReviewId());

        Mockito.when(movieReviewCompliantService.patchMovieReviewMovieReviewCompliant(patchDTO.getMovieReviewId(),
                read.getId(),
                patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                patchDTO.getMovieReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                patchDTO.getMovieReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        patch.setDescription("");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setMovieReviewId(UUID.randomUUID());
        patch.setPortalUserId(UUID.randomUUID());
        patch.setMovieId(UUID.randomUUID());

        String resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).patchMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantPatchDTO patch = new MovieReviewCompliantPatchDTO();
        patch.setDescription(StringUtils.repeat("*", 1001));
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setMovieReviewId(UUID.randomUUID());
        patch.setPortalUserId(UUID.randomUUID());
        patch.setMovieId(UUID.randomUUID());

        String resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService,
                Mockito.never()).patchMovieReviewMovieReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
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
