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
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewMovieReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = MovieReviewMovieReviewCompliantController.class)
public class MovieReviewMovieReviewCompliantControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewMovieReviewCompliantService movieReviewCompliantService;

    @Test
    public void testGetMovieReviewCompliant() throws Exception {
        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);
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

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);

        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        
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

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);

        MovieReviewCompliantPatchDTO patchDTO = generateObject(MovieReviewCompliantPatchDTO.class);
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

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);

        MovieReviewCompliantPutDTO putDTO = generateObject(MovieReviewCompliantPutDTO.class);
        putDTO.setMovieReviewId(movieReviewReadDTO.getId());

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

        MovieReviewCompliantPutDTO putDTO = generateObject(MovieReviewCompliantPutDTO.class);
        putDTO.setDescription("D");

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
        MovieReviewCompliantPutDTO put = generateObject(MovieReviewCompliantPutDTO.class);
        put.setDescription("");

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
        MovieReviewCompliantPutDTO put = generateObject(MovieReviewCompliantPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

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
        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription("");

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
        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));


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

        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription("D");

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

        MovieReviewCompliantPatchDTO patchDTO = generateObject(MovieReviewCompliantPatchDTO.class);
        patchDTO.setDescription("D");

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
        MovieReviewCompliantPatchDTO patch = generateObject(MovieReviewCompliantPatchDTO.class);
        patch.setDescription("");

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
        MovieReviewCompliantPatchDTO patch = generateObject(MovieReviewCompliantPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

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
        MovieReviewCompliantReadDTO movieReviewCompliant = generateObject(MovieReviewCompliantReadDTO.class);
        movieReviewCompliant.setMovieReviewId(movieReviewId);
        return movieReviewCompliant;
    }
}
