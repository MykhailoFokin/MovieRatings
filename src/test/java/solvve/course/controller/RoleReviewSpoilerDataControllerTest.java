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
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleSpoilerDataService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleReviewSpoilerDataController.class)
@ActiveProfiles("test")
public class RoleReviewSpoilerDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleSpoilerDataService roleSpoilerDataService;

    private RoleSpoilerDataReadDTO createRoleSpoilerDataRead(UUID roleReviewId) {
        RoleSpoilerDataReadDTO roleSpoilerData = new RoleSpoilerDataReadDTO();
        roleSpoilerData.setId(UUID.randomUUID());
        roleSpoilerData.setStartIndex(100);
        roleSpoilerData.setEndIndex(150);
        roleSpoilerData.setRoleReviewId(roleReviewId);
        return roleSpoilerData;
    }

    private RoleReviewReadDTO createRoleReview() {
        RoleReviewReadDTO roleReview = new RoleReviewReadDTO();
        roleReview.setId(UUID.randomUUID());
        roleReview.setTextReview("Review");
        return roleReview;
    }

    @Test
    public void testGetRoleSpoilerData() throws Exception {
        RoleReviewReadDTO roleReview = createRoleReview();
        List<RoleSpoilerDataReadDTO> roleSpoilerData = List.of(createRoleSpoilerDataRead(roleReview.getId()));

        Mockito.when(roleSpoilerDataService.getRoleReviewSpoilerData(roleReview.getId()))
                .thenReturn(roleSpoilerData);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas",
                roleReview.getId().toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleSpoilerDataReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleSpoilerDataReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleSpoilerData);

        Mockito.verify(roleSpoilerDataService).getRoleReviewSpoilerData(roleReview.getId());
    }

    @Test
    public void testGetRoleSpoilerDataWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleSpoilerData.class,wrongId);
        Mockito.when(roleSpoilerDataService.getRoleReviewSpoilerData(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas"
                , wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateRoleSpoilerData() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();
        create.setStartIndex(100);
        create.setEndIndex(150);
        create.setRoleReviewId(roleReviewReadDTO.getId());

        RoleSpoilerDataReadDTO read = createRoleSpoilerDataRead(roleReviewReadDTO.getId());

        Mockito.when(roleSpoilerDataService.createRoleReviewSpoilerData(roleReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas"
                , roleReviewReadDTO.getId().toString())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper
                .readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assertions.assertThat(actualRoleSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleSpoilerData() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleSpoilerDataPatchDTO patchDTO = new RoleSpoilerDataPatchDTO();
        patchDTO.setStartIndex(100);
        patchDTO.setEndIndex(150);
        patchDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleSpoilerDataReadDTO read = createRoleSpoilerDataRead(roleReviewReadDTO.getId());

        Mockito.when(roleSpoilerDataService.patchRoleReviewSpoilerData(roleReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas/{id}",
                roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper
                .readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }

    @Test
    public void testDeleteRoleSpoilerData() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(roleSpoilerDataService).deleteRoleReviewSpoilerData(id, id);
    }

    @Test
    public void testPutRoleSpoilerData() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = createRoleReview();
        RoleSpoilerDataPutDTO putDTO = new RoleSpoilerDataPutDTO();
        putDTO.setStartIndex(100);
        putDTO.setEndIndex(150);
        putDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleSpoilerDataReadDTO read = createRoleSpoilerDataRead(roleReviewReadDTO.getId());

        Mockito.when(roleSpoilerDataService.updateRoleReviewSpoilerData(roleReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas/{id}",
                roleReviewReadDTO.getId().toString(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper
                .readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }
}
