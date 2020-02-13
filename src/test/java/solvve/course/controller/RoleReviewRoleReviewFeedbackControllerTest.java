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
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewRoleReviewFeedbackService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewRoleReviewFeedbackController.class)
@ActiveProfiles("test")
public class RoleReviewRoleReviewFeedbackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleReviewRoleReviewFeedbackService roleReviewFeedbackService;

    private RoleReviewFeedbackReadDTO createRoleReviewFeedbackRead(UUID roleReviewId) {
        RoleReviewFeedbackReadDTO roleReviewFeedback = new RoleReviewFeedbackReadDTO();
        roleReviewFeedback.setId(UUID.randomUUID());
        roleReviewFeedback.setIsLiked(true);
        roleReviewFeedback.setRoleReviewId(roleReviewId);
        return roleReviewFeedback;
    }

    private RoleReviewReadDTO createRoleReview() {
        RoleReviewReadDTO roleReview = new RoleReviewReadDTO();
        roleReview.setId(UUID.randomUUID());
        roleReview.setTextReview("Review");
        return roleReview;
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedback() throws Exception {
        RoleReviewReadDTO roleReview = createRoleReview();
        List<RoleReviewFeedbackReadDTO> roleReviewFeedbackReadDTOList =
                List.of(createRoleReviewFeedbackRead(roleReview.getId()));

        Mockito.when(roleReviewFeedbackService.getRoleReviewRoleReviewFeedback(roleReview.getId()))
                .thenReturn(roleReviewFeedbackReadDTOList);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks",
                roleReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReviewFeedbackReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReviewFeedbackReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleReviewFeedbackReadDTOList);

        Mockito.verify(roleReviewFeedbackService).getRoleReviewRoleReviewFeedback(roleReview.getId());
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewFeedback.class,wrongId);
        Mockito.when(roleReviewFeedbackService.getRoleReviewRoleReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get(
                "/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewRoleReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get(
                "/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewRoleReviewFeedback() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setIsLiked(true);
        create.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewFeedbackService.createRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks",
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

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleReviewFeedbackPatchDTO patchDTO = new RoleReviewFeedbackPatchDTO();
        patchDTO.setIsLiked(true);
        patchDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewFeedbackService.patchRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks/{id}",
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

        mvc.perform(delete("/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewFeedbackService).deleteRoleReviewRoleReviewFeedback(id, id);
    }

    @Test
    public void testPutRoleReviewRoleReviewFeedback() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleReviewFeedbackPutDTO putDTO = new RoleReviewFeedbackPutDTO();
        putDTO.setIsLiked(true);
        putDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewFeedbackService.updateRoleReviewRoleReviewFeedback(roleReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{rolereviewid}/rolereviewfeedbacks/{id}",
                roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewRoleReviewFeedback = objectMapper
                .readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewRoleReviewFeedback);
    }
}
