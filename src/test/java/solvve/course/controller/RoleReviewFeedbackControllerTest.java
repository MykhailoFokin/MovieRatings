package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = RoleReviewFeedbackController.class)
public class RoleReviewFeedbackControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewFeedbackService roleReviewFeedbackService;

    @Test
    public void testGetRoleReviewFeedback() throws Exception {
        RoleReviewFeedbackReadDTO roleReviewFeedback = generateObject(RoleReviewFeedbackReadDTO.class);

        Mockito.when(roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId()))
                .thenReturn(roleReviewFeedback);

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedbacks/{id}", roleReviewFeedback.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleReviewFeedback);

        Mockito.verify(roleReviewFeedbackService).getRoleReviewFeedback(roleReviewFeedback.getId());
    }

    @Test
    public void testGetRoleReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewFeedback.class,wrongId);
        Mockito.when(roleReviewFeedbackService.getRoleReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedbacks/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedbacks/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackCreateDTO create = generateObject(RoleReviewFeedbackCreateDTO.class);

        RoleReviewFeedbackReadDTO read =generateObject(RoleReviewFeedbackReadDTO.class);

        Mockito.when(roleReviewFeedbackService.createRoleReviewFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewfeedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback =
                objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualRoleReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackPatchDTO patchDTO = generateObject(RoleReviewFeedbackPatchDTO.class);

        RoleReviewFeedbackReadDTO read = generateObject(RoleReviewFeedbackReadDTO.class);

        Mockito.when(roleReviewFeedbackService.patchRoleReviewFeedback(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviewfeedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback =
                objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewFeedback);
    }

    @Test
    public void testDeleteRoleReviewFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviewfeedbacks/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewFeedbackService).deleteRoleReviewFeedback(id);
    }

    @Test
    public void testPutRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackPutDTO putDTO = generateObject(RoleReviewFeedbackPutDTO.class);

        RoleReviewFeedbackReadDTO read = generateObject(RoleReviewFeedbackReadDTO.class);

        Mockito.when(roleReviewFeedbackService.updateRoleReviewFeedback(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviewfeedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback =
                objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewFeedback);
    }

    @Test
    public void testCreateRoleReviewFeedbackValidationFailed() throws Exception {
        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/rolereviewfeedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewFeedbackService, Mockito.never()).createRoleReviewFeedback(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewFeedbackValidationFailed() throws Exception {
        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();

        String resultJson = mvc.perform(put("/api/v1/rolereviewfeedbacks/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewFeedbackService, Mockito.never()).updateRoleReviewFeedback(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
