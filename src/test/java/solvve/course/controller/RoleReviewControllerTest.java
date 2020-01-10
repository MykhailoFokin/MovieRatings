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
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.dto.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetRoleReview() throws Exception {
        RoleReviewReadDTO roleReview = new RoleReviewReadDTO();
        roleReview.setId(UUID.randomUUID());
        //roleReview.setUserId(portalUserReadDTO.getId());
        //roleReview.setRoleId(roleReadDTO.getId());
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //roleReview.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(roleReviewService.getRoleReview(roleReview.getId())).thenReturn(roleReview);

        String resultJson = mvc.perform(get("/api/v1/rolereview/{id}", roleReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        RoleReviewReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleReview);

        Mockito.verify(roleReviewService).getRoleReview(roleReview.getId());
    }

    @Test
    public void testGetRoleReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReview.class,wrongId);
        Mockito.when(roleReviewService.getRoleReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereview/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/rolereview/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateRoleReview() throws Exception {

        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setRoleId(roleReadDTO.getId());
        create.setTextReview("This role can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //create.setModeratorId(portalUserReadDTO.getId());

        RoleReviewReadDTO read = new RoleReviewReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setRoleId(roleReadDTO.getId());
        read.setTextReview("This role can be described as junk.");
        read.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //read.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(roleReviewService.createRoleReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereview")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);
    }
}
