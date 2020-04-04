package solvve.course.controller;

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
import solvve.course.service.RoleSpoilerDataService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleSpoilerDataController.class)
public class RoleSpoilerDataControllerTest extends BaseControllerTest {

    @MockBean
    private RoleSpoilerDataService roleSpoilerDataService;

    @Test
    public void testGetRoleSpoilerData() throws Exception {
        RoleSpoilerDataReadDTO roleSpoilerData = generateObject(RoleSpoilerDataReadDTO.class);

        Mockito.when(roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId())).thenReturn(roleSpoilerData);

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}", roleSpoilerData.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualMovie = objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleSpoilerData);

        Mockito.verify(roleSpoilerDataService).getRoleSpoilerData(roleSpoilerData.getId());
    }

    @Test
    public void testGetRoleSpoilerDataWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleSpoilerData.class,wrongId);
        Mockito.when(roleSpoilerDataService.getRoleSpoilerData(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleSpoilerDataWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleSpoilerData() throws Exception {

        RoleSpoilerDataCreateDTO create = generateObject(RoleSpoilerDataCreateDTO.class);
        create.setStartIndex(1);

        RoleSpoilerDataReadDTO read = generateObject(RoleSpoilerDataReadDTO.class);
        read.setRoleReviewId(create.getRoleReviewId());

        Mockito.when(roleSpoilerDataService.createRoleSpoilerData(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolespoilerdata")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData =
                objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assertions.assertThat(actualRoleSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleSpoilerData() throws Exception {

        RoleSpoilerDataPatchDTO patchDTO = generateObject(RoleSpoilerDataPatchDTO.class);
        patchDTO.setStartIndex(1);

        RoleSpoilerDataReadDTO read = generateObject(RoleSpoilerDataReadDTO.class);

        Mockito.when(roleSpoilerDataService.patchRoleSpoilerData(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolespoilerdata/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData =
                objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }

    @Test
    public void testDeleteRoleSpoilerData() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolespoilerdata/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleSpoilerDataService).deleteRoleSpoilerData(id);
    }

    @Test
    public void testPutRoleSpoilerData() throws Exception {

        RoleSpoilerDataPutDTO putDTO = generateObject(RoleSpoilerDataPutDTO.class);
        putDTO.setStartIndex(1);

        RoleSpoilerDataReadDTO read = generateObject(RoleSpoilerDataReadDTO.class);
        read.setRoleReviewId(putDTO.getRoleReviewId());

        Mockito.when(roleSpoilerDataService.updateRoleSpoilerData(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolespoilerdata/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData =
                objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }

    @Test
    public void testCreateRoleSpoilerDataValidationFailed() throws Exception {
        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/rolespoilerdata")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleSpoilerDataService, Mockito.never()).createRoleSpoilerData(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleSpoilerDataValidationFailed() throws Exception {
        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();

        String resultJson = mvc.perform(put("/api/v1/rolespoilerdata/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleSpoilerDataService, Mockito.never()).updateRoleSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataCreateDTO roleSpoilerDataCreate = generateObject(RoleSpoilerDataCreateDTO.class);
        roleSpoilerDataCreate.setStartIndex(100);
        roleSpoilerDataCreate.setEndIndex(10);

        String resultJson = mvc.perform(post("/api/v1//rolespoilerdata")
                .content(objectMapper.writeValueAsString(roleSpoilerDataCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).createRoleSpoilerData(ArgumentMatchers.any());
    }

    @Test
    public void testPatchRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataPatchDTO roleSpoilerDataPatch = generateObject(RoleSpoilerDataPatchDTO.class);
        roleSpoilerDataPatch.setStartIndex(100);
        roleSpoilerDataPatch.setEndIndex(10);

        String resultJson = mvc.perform(patch("/api/v1/rolespoilerdata/{id}",
                roleSpoilerDataPatch.getRoleReviewId())
                .content(objectMapper.writeValueAsString(roleSpoilerDataPatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).patchRoleSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleSpoilerDataWrongIndexes() throws Exception {
        RoleSpoilerDataPutDTO roleSpoilerDataPut = generateObject(RoleSpoilerDataPutDTO.class);
        roleSpoilerDataPut.setStartIndex(100);
        roleSpoilerDataPut.setEndIndex(10);

        String resultJson = mvc.perform(put("/api/v1/rolespoilerdata/{id}",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(roleSpoilerDataPut))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(roleSpoilerDataService, Mockito.never()).updateRoleSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
