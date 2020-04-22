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
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = RoleReviewController.class)
public class RoleReviewControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewService roleReviewService;

    @Test
    public void testGetRoleReview() throws Exception {
        RoleReviewReadDTO roleReview = generateObject(RoleReviewReadDTO.class);

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

        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);

        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

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

        RoleReviewPatchDTO patchDTO = generateObject(RoleReviewPatchDTO.class);

        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

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

        RoleReviewPutDTO putDTO = generateObject(RoleReviewPutDTO.class);

        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

        Mockito.when(roleReviewService.updateRoleReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testCreateRoleReviewValidationFailed() throws Exception {
        RoleReviewCreateDTO create = new RoleReviewCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).createRoleReview(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewValidationFailed() throws Exception {
        RoleReviewPutDTO put = new RoleReviewPutDTO();

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).updateRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCheckLimitBorders() throws Exception {

        RoleReviewPutDTO putDTO = generateObject(RoleReviewPutDTO.class);
        putDTO.setTextReview(StringUtils.repeat("*", 1));

        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

        Mockito.when(roleReviewService.updateRoleReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);

        // Check upper border
        putDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewPutDTO put = generateObject(RoleReviewPutDTO.class);
        put.setTextReview("");

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).updateRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewPutDTO put = generateObject(RoleReviewPutDTO.class);
        put.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/rolereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).updateRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview("");

        String resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).createRoleReview(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).createRoleReview(ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleReviewCheckStingBorders() throws Exception {

        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview(StringUtils.repeat("*", 1));
        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

        Mockito.when(roleReviewService.createRoleReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);

        create.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/rolereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReviewCheckStringBorders() throws Exception {

        RoleReviewPatchDTO patchDTO = generateObject(RoleReviewPatchDTO.class);
        patchDTO.setTextReview(StringUtils.repeat("*", 1));

        RoleReviewReadDTO read = generateObject(RoleReviewReadDTO.class);

        Mockito.when(roleReviewService.patchRoleReview(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);

        patchDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/rolereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewPatchDTO patch = generateObject(RoleReviewPatchDTO.class);
        patch.setTextReview("");

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).patchRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewPatchDTO patch = generateObject(RoleReviewPatchDTO.class);
        patch.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/rolereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewService, Mockito.never()).patchRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
