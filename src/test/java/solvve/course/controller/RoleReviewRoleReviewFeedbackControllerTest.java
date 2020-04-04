package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewRoleReviewFeedbackService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleReviewRoleReviewFeedbackController.class)
public class RoleReviewRoleReviewFeedbackControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewRoleReviewFeedbackService roleReviewFeedbackService;

    private RoleReviewFeedbackReadDTO createRoleReviewFeedbackRead(UUID roleReviewId) {
        RoleReviewFeedbackReadDTO roleReviewFeedback = generateObject(RoleReviewFeedbackReadDTO.class);
        roleReviewFeedback.setRoleReviewId(roleReviewId);
        return roleReviewFeedback;
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedback() throws Exception {
        RoleReviewReadDTO roleReview = generateObject(RoleReviewReadDTO.class);
        List<RoleReviewFeedbackReadDTO> roleReviewFeedbackReadDTOs =
                List.of(createRoleReviewFeedbackRead(roleReview.getId()));

        Mockito.when(roleReviewFeedbackService.getRoleReviewRoleReviewFeedback(roleReview.getId()))
                .thenReturn(roleReviewFeedbackReadDTOs);

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks",
                roleReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReviewFeedbackReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReviewFeedbackReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleReviewFeedbackReadDTOs);

        Mockito.verify(roleReviewFeedbackService).getRoleReviewRoleReviewFeedback(roleReview.getId());
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewFeedback.class,wrongId);
        Mockito.when(roleReviewFeedbackService.getRoleReviewRoleReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get(
                "/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get(
                "/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewRoleReviewFeedback() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);
        RoleReviewFeedbackCreateDTO create = generateObject(RoleReviewFeedbackCreateDTO.class);
        create.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());
        read.setRoleId(create.getRoleId());
        read.setPortalUserId(UUID.randomUUID());

        Mockito.when(roleReviewFeedbackService.createRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks",
                roleReviewReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewRoleReviewFeedback = objectMapper
                .readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualRoleReviewRoleReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewRoleReviewFeedback() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);
        RoleReviewFeedbackPatchDTO patchDTO = generateObject(RoleReviewFeedbackPatchDTO.class);
        patchDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewFeedbackService.patchRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks/{id}",
                roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewRoleReviewFeedback = objectMapper
                .readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewRoleReviewFeedback);
    }

    @Test
    public void testDeleteRoleReviewRoleReviewFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewFeedbackService).deleteRoleReviewRoleReviewFeedback(id, id);
    }

    @Test
    public void testPutRoleReviewRoleReviewFeedback() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);
        RoleReviewFeedbackPutDTO putDTO = generateObject(RoleReviewFeedbackPutDTO.class);
        putDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());
        read.setRoleId(putDTO.getRoleId());
        read.setPortalUserId(putDTO.getPortalUserId());

        Mockito.when(roleReviewFeedbackService.updateRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks/{id}",
                roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewRoleReviewFeedback = objectMapper
                .readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewRoleReviewFeedback);
    }

    @Test
    public void testCreateRoleReviewFeedbackValidationFailed() throws Exception {
        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewFeedbackService,
                Mockito.never()).createRoleReviewRoleReviewFeedback(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewFeedbackValidationFailed() throws Exception {
        RoleReviewFeedbackPutDTO put = new RoleReviewFeedbackPutDTO();

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewFeedbackService,
                Mockito.never()).updateRoleReviewRoleReviewFeedback(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
