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
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewController.class)
@ActiveProfiles("test")
public class RoleReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleReviewService roleReviewService;

    private RoleReviewReadDTO createRoleReviewRead() {
        RoleReviewReadDTO roleReview = new RoleReviewReadDTO();
        roleReview.setId(UUID.randomUUID());
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        return roleReview;
    }

    @Test
    public void testGetRoleReview() throws Exception {
        RoleReviewReadDTO roleReview = createRoleReviewRead();

        Mockito.when(roleReviewService.getRoleReview(roleReview.getId())).thenReturn(roleReview);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{id}", roleReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleReview);

        Mockito.verify(roleReviewService).getRoleReview(roleReview.getId());
    }

    @Test
    public void testGetRoleReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReview.class,wrongId);
        Mockito.when(roleReviewService.getRoleReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReview() throws Exception {

        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewReadDTO read = createRoleReviewRead();

        Mockito.when(roleReviewService.createRoleReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReview() throws Exception {

        RoleReviewPatchDTO patchDTO = new RoleReviewPatchDTO();
        patchDTO.setTextReview("This role can be described as junk.");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewReadDTO read = createRoleReviewRead();

        Mockito.when(roleReviewService.patchRoleReview(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testDeleteRoleReview() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviews/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewService).deleteRoleReview(id);
    }

    @Test
    public void testPutRoleReview() throws Exception {

        RoleReviewPutDTO putDTO = new RoleReviewPutDTO();
        putDTO.setTextReview("This role can be described as junk.");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewReadDTO read = createRoleReviewRead();

        Mockito.when(roleReviewService.putRoleReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }
}
