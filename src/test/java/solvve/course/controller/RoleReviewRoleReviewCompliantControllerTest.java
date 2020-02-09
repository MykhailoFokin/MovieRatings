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
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewRoleReviewCompliantController.class)
@ActiveProfiles("test")
public class RoleReviewRoleReviewCompliantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleReviewCompliantService roleReviewCompliantService;

    @Test
    public void testGetRoleReviewCompliant() throws Exception {
        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        List<RoleReviewCompliantReadDTO> roleReviewCompliant = List.of(
                createRoleReviewCompliantRead(roleReviewReadDTO.getId()));

        Mockito.when(roleReviewCompliantService.getRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId()))
                .thenReturn(roleReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants",
                roleReviewReadDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReviewCompliantReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReviewCompliantReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleReviewCompliant);

        Mockito.verify(roleReviewCompliantService).getRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId());
    }

    @Test
    public void testGetRoleReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReviewCompliant.class,wrongId);
        Mockito.when(roleReviewCompliantService.getRoleReviewRoleReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewCompliant() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setRoleReviewId(roleReviewReadDTO.getId());
        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewCompliantService.createRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants",
                roleReviewReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper
                .readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCompliant() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();

        RoleReviewCompliantPatchDTO patchDTO = new RoleReviewCompliantPatchDTO();
        patchDTO.setDescription("Just punish him!");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patchDTO.setRoleReviewId(roleReviewReadDTO.getId());
        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewCompliantService.patchRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants/{id}"
                ,roleReviewReadDTO.getId() ,read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper
                .readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testDeleteRoleReviewCompliant() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewCompliantService).deleteRoleReviewRoleReviewCompliant(id, id);
    }

    @Test
    public void testPutRoleReviewCompliant() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();

        RoleReviewCompliantPutDTO putDTO = new RoleReviewCompliantPutDTO();
        putDTO.setDescription("Just punish him!");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        putDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewCompliantService.putRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{rolereviewid}/rolereviewcompliants/{id}"
                , roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper
                .readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    private RoleReviewReadDTO createRoleReview() {
        RoleReviewReadDTO roleReviewReadDTO = new RoleReviewReadDTO();
        roleReviewReadDTO.setId(UUID.randomUUID());
        roleReviewReadDTO.setTextReview("Review");
        return roleReviewReadDTO;
    }

    private RoleReviewCompliantReadDTO createRoleReviewCompliantRead(UUID roleReviewId) {
        RoleReviewCompliantReadDTO roleReviewCompliant = new RoleReviewCompliantReadDTO();
        roleReviewCompliant.setId(UUID.randomUUID());
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReviewCompliant.setRoleReviewId(roleReviewId);
        return  roleReviewCompliant;
    }
}
