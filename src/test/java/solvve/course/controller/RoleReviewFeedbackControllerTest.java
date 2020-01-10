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
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetRoleReviewFeedback() throws Exception {
        RoleReviewFeedbackReadDTO roleReviewFeedback = new RoleReviewFeedbackReadDTO();
        roleReviewFeedback.setId(UUID.randomUUID());
        //roleReviewFeedback.setUserId(portalUserReadDTO.getId());
        //roleReviewFeedback.setRoleId(roleReadDTO.getId());
        //roleReviewFeedback.setRoleReviewId(roleReviewReadDTO.getId());
        roleReviewFeedback.setIsLiked(true);

        Mockito.when(roleReviewFeedbackService.getRoleReviewFeedback(roleReviewFeedback.getId())).thenReturn(roleReviewFeedback);

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedback/{id}", roleReviewFeedback.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
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

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/rolereviewfeedback/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateRoleReviewFeedback() throws Exception {

        RoleReviewFeedbackCreateDTO create = new RoleReviewFeedbackCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setRoleId(roleReadDTO.getId());
        //create.setRoleReviewId(roleReviewReadDTO.getId());
        create.setIsLiked(true);

        RoleReviewFeedbackReadDTO read = new RoleReviewFeedbackReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setRoleId(roleReadDTO.getId());
        //read.setRoleReviewId(roleReviewReadDTO.getId());
        read.setIsLiked(true);

        Mockito.when(roleReviewFeedbackService.createRoleReviewFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewfeedback")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewFeedbackReadDTO actualRoleReviewFeedback = objectMapper.readValue(resultJson, RoleReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualRoleReviewFeedback).isEqualToComparingFieldByField(read);
    }
}
