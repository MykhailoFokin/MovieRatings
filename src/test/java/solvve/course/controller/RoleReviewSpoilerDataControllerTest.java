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
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.*;
import solvve.course.exception.ControllerValidationException;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewSpoilerDataService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleReviewSpoilerDataController.class)
public class RoleReviewSpoilerDataControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewSpoilerDataService roleSpoilerDataService;

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

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas",
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

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas"
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

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas"
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

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
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

        mvc.perform(delete("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
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

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
                roleReviewReadDTO.getId().toString(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper
                .readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }

    @Test
    public void testCreateRoleSpoilerDataValidationFailed() throws Exception {
        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleSpoilerDataService, Mockito.never()).createRoleReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleSpoilerDataValidationFailed() throws Exception {
        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleSpoilerDataService, Mockito.never()).updateRoleReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataCreateDTO roleSpoilerDataCreate = new RoleSpoilerDataCreateDTO();
        roleSpoilerDataCreate.setRoleReviewId(UUID.randomUUID());
        roleSpoilerDataCreate.setStartIndex(100);
        roleSpoilerDataCreate.setEndIndex(10);

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(roleSpoilerDataCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).createRoleReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataPatchDTO roleSpoilerDataPatch = new RoleSpoilerDataPatchDTO();
        roleSpoilerDataPatch.setRoleReviewId(UUID.randomUUID());
        roleSpoilerDataPatch.setStartIndex(100);
        roleSpoilerDataPatch.setEndIndex(10);

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(roleSpoilerDataPatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).patchRoleReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataPutDTO roleSpoilerDataPut = new RoleSpoilerDataPutDTO();
        roleSpoilerDataPut.setRoleReviewId(UUID.randomUUID());
        roleSpoilerDataPut.setStartIndex(100);
        roleSpoilerDataPut.setEndIndex(10);

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(roleSpoilerDataPut))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).updateRoleReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
