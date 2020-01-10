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
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.dto.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewCompliantController.class)
@ActiveProfiles("test")
public class RoleReviewCompliantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleReviewCompliantService roleReviewCompliantService;

    @Test
    public void testGetRoleReviewCompliant() throws Exception {
        RoleReviewCompliantReadDTO roleReviewCompliant = new RoleReviewCompliantReadDTO();
        roleReviewCompliant.setId(UUID.randomUUID());
        //roleReviewCompliant.setUserId(portalUserReadDTO.getId());
        //roleReviewCompliant.setRoleId(roleReadDTO.getId());
        //roleReviewCompliant.setRoleReviewId(roleReviewReadDTO.getId());
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //roleReviewCompliant.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId())).thenReturn(roleReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliant/{id}", roleReviewCompliant.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        RoleReviewCompliantReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleReviewCompliant);

        Mockito.verify(roleReviewCompliantService).getRoleReviewCompliant(roleReviewCompliant.getId());
    }

    @Test
    public void testGetRoleReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewCompliant.class,wrongId);
        Mockito.when(roleReviewCompliantService.getRoleReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliant/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliant/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateRoleReviewCompliant() throws Exception {

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setRoleId(roleReadDTO.getId());
        //create.setRoleReviewId(roleReviewReadDTO.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //create.setModeratorId(portalUserReadDTO.getId());

        RoleReviewCompliantReadDTO read = new RoleReviewCompliantReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setRoleId(roleReadDTO.getId());
        //read.setRoleReviewId(roleReviewReadDTO.getId());
        read.setDescription("Just punish him!");
        read.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //read.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(roleReviewCompliantService.createRoleReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliant")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);
    }
}
