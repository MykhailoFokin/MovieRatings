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
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewFeedbackController.class)
@ActiveProfiles("test")
public class RoleReviewFeedbackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleReviewFeedbackService roleReviewFeedbackService;

    private RoleReviewFeedbackReadDTO createRoleReviewFeedbackRead() {
        RoleReviewFeedbackReadDTO roleReviewFeedback = new RoleReviewFeedbackReadDTO();
        roleReviewFeedback.setId(UUID.randomUUID());
        roleReviewFeedback.setIsLiked(true);
        return roleReviewFeedback;
    }

    @Test
    public void testGetRoleReviewFeedback() throws Exception {
        RoleReviewFeedbackReadDTO roleReviewFeedback = createRoleReviewFeedbackRead();

        Mockito.when(roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId())).thenReturn(roleReviewFeedback);

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedback/{id}", roleReviewFeedback.getId()))
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

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedback/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedback/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read =createRoleReviewFeedbackRead();

        Mockito.when(roleReviewFeedbackService.createRoleReviewFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewfeedback")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback = objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualRoleReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackPatchDTO patchDTO = new RoleReviewFeedbackPatchDTO();
        patchDTO.setIsLiked(true);

        RoleReviewFeedbackReadDTO read = createRoleReviewFeedbackRead();

        Mockito.when(roleReviewFeedbackService.patchRoleReviewFeedback(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviewfeedback/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback = objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewFeedback);
    }

    @Test
    public void testDeleteRoleReviewFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviewfeedback/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewFeedbackService).deleteRoleReviewFeedback(id);
    }
}
