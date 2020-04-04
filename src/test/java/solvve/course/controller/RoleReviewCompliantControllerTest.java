package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleReviewCompliantController.class)
public class RoleReviewCompliantControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewCompliantService roleReviewCompliantService;

    @Test
    public void testGetRoleReviewCompliant() throws Exception {
        RoleReviewCompliantReadDTO roleReviewCompliant = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.getRoleReviewCompliant(roleReviewCompliant.getId()))
                .thenReturn(roleReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/rolereviewcompliants/{id}",
                roleReviewCompliant.getId()))
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

        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.createRoleReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCompliant() throws Exception {

        RoleReviewCompliantPatchDTO patchDTO = generateObject(RoleReviewCompliantPatchDTO.class);

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.patchRoleReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
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

        RoleReviewCompliantPutDTO putDTO = generateObject(RoleReviewCompliantPutDTO.class);

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.updateRoleReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testCreateRoleReviewCompliantValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).createRoleReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCompliantValidationFailed() throws Exception {
        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).updateRoleReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCompliantCheckLimitBorders() throws Exception {

        RoleReviewCompliantPutDTO putDTO = generateObject(RoleReviewCompliantPutDTO.class);
        putDTO.setDescription("D");

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.updateRoleReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCompliantPutDTO put = generateObject(RoleReviewCompliantPutDTO.class);
        put.setDescription("");

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).updateRoleReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantPutDTO put = generateObject(RoleReviewCompliantPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/rolereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).updateRoleReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).createRoleReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).createRoleReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleReviewCompliantCheckStingBorders() throws Exception {

        RoleReviewCompliantCreateDTO create =generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription("D");

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.createRoleReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/rolereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCompliantCheckStringBorders() throws Exception {

        RoleReviewCompliantPatchDTO patchDTO = generateObject(RoleReviewCompliantPatchDTO.class);
        patchDTO.setDescription("D");

        RoleReviewCompliantReadDTO read = generateObject(RoleReviewCompliantReadDTO.class);

        Mockito.when(roleReviewCompliantService.patchRoleReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReviewCompliant = objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCompliantPatchDTO patch = generateObject(RoleReviewCompliantPatchDTO.class);
        patch.setDescription("");

        String resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).patchRoleReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantPatchDTO patch = generateObject(RoleReviewCompliantPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/rolereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService, Mockito.never()).patchRoleReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
