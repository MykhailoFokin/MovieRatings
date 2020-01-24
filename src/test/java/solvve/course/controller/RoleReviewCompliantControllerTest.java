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
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private RoleReviewCompliantReadDTO createRoleReviewCompliantRead() {
        RoleReviewCompliantReadDTO roleReviewCompliant = new RoleReviewCompliantReadDTO();
        roleReviewCompliant.setId(UUID.randomUUID());
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        return  roleReviewCompliant;
    }

    @Test
    public void testGetRoleReviewCompliant() throws Exception {
        RoleReviewCompliantReadDTO roleReviewCompliant = createRoleReviewCompliantRead();

        Mockito.when(roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId())).thenReturn(roleReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliants/{id}", roleReviewCompliant.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleReviewCompliant);

        Mockito.verify(roleReviewCompliantService).getRoleReviewCompliant(roleReviewCompliant.getId());
    }

    @Test
    public void testGetRoleReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewCompliant.class,wrongId);
        Mockito.when(roleReviewCompliantService.getRoleReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewCompliant() throws Exception {

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead();

        Mockito.when(roleReviewCompliantService.createRoleReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCompliant() throws Exception {

        RoleReviewCompliantPatchDTO patchDTO = new RoleReviewCompliantPatchDTO();
        patchDTO.setDescription("Just punish him!");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead();

        Mockito.when(roleReviewCompliantService.patchRoleReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testDeleteRoleReviewCompliant() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviewcompliants/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewCompliantService).deleteRoleReviewCompliant(id);
    }

    @Test
    public void testPutRoleReviewCompliant() throws Exception {

        RoleReviewCompliantPutDTO putDTO = new RoleReviewCompliantPutDTO();
        putDTO.setDescription("Just punish him!");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead();

        Mockito.when(roleReviewCompliantService.putRoleReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }
}
