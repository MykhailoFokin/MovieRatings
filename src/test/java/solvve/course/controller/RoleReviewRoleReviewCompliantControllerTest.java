package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleReviewRoleReviewCompliantService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleReviewRoleReviewCompliantController.class)
public class RoleReviewRoleReviewCompliantControllerTest extends BaseControllerTest {

    @MockBean
    private RoleReviewRoleReviewCompliantService roleReviewCompliantService;

    @Test
    public void testGetRoleReviewCompliant() throws Exception {
        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);
        List<RoleReviewCompliantReadDTO> roleReviewCompliant = List.of(
                createRoleReviewCompliantRead(roleReviewReadDTO.getId()));

        Mockito.when(roleReviewCompliantService.getRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId()))
                .thenReturn(roleReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
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

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/role-reviews/{roleReviewId}/role-review-compliants"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReviewCompliant() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);

        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setRoleReviewId(roleReviewReadDTO.getId());
        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewCompliantService.createRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
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

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);

        RoleReviewCompliantPatchDTO patchDTO = generateObject(RoleReviewCompliantPatchDTO.class);
        patchDTO.setRoleReviewId(roleReviewReadDTO.getId());
        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());

        Mockito.when(roleReviewCompliantService.patchRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}"
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

        mvc.perform(delete("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(roleReviewCompliantService).deleteRoleReviewRoleReviewCompliant(id, id);
    }

    @Test
    public void testPutRoleReviewCompliant() throws Exception {

        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);

        RoleReviewCompliantPutDTO putDTO = generateObject(RoleReviewCompliantPutDTO.class);
        putDTO.setRoleReviewId(roleReviewReadDTO.getId());

        RoleReviewCompliantReadDTO read =
                createRoleReviewCompliantRead(roleReviewReadDTO.getId());
        read.setPortalUserId(putDTO.getPortalUserId());
        read.setRoleId(putDTO.getRoleId());

        Mockito.when(roleReviewCompliantService.updateRoleReviewRoleReviewCompliant(roleReviewReadDTO.getId(),
                read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}"
                , roleReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant = objectMapper
                .readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);
    }

    @Test
    public void testCreateRoleReviewCompliantValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).createRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCompliantValidationFailed() throws Exception {
        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).updateRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCompliantCheckLimitBorders() throws Exception {

        RoleReviewCompliantPutDTO putDTO = generateObject(RoleReviewCompliantPutDTO.class);
        putDTO.setDescription("D");

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead(putDTO.getRoleReviewId());

        Mockito.when(roleReviewCompliantService.updateRoleReviewRoleReviewCompliant(putDTO.getRoleReviewId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                putDTO.getRoleReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                putDTO.getRoleReviewId(), read.getId().toString())
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

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).updateRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantPutDTO put = generateObject(RoleReviewCompliantPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).updateRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).createRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).createRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleReviewCompliantCheckStingBorders() throws Exception {

        RoleReviewCompliantCreateDTO create = generateObject(RoleReviewCompliantCreateDTO.class);
        create.setDescription("D");

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead(create.getRoleReviewId());

        Mockito.when(roleReviewCompliantService.createRoleReviewRoleReviewCompliant(create.getRoleReviewId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                create.getRoleReviewId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assertions.assertThat(actualRoleReviewCompliant).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/role-reviews/{roleReviewId}/role-review-compliants",
                create.getRoleReviewId())
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

        RoleReviewCompliantReadDTO read = createRoleReviewCompliantRead(patchDTO.getRoleReviewId());

        Mockito.when(roleReviewCompliantService.patchRoleReviewRoleReviewCompliant(patchDTO.getRoleReviewId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                patchDTO.getRoleReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewCompliantReadDTO actualRoleReviewCompliant =
                objectMapper.readValue(resultJson, RoleReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualRoleReviewCompliant);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                patchDTO.getRoleReviewId(), read.getId().toString())
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

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).patchRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCompliantPatchDTO patch = generateObject(RoleReviewCompliantPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/role-reviews/{roleReviewId}/role-review-compliants/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleReviewCompliantService,
                Mockito.never()).patchRoleReviewRoleReviewCompliant(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    private RoleReviewCompliantReadDTO createRoleReviewCompliantRead(UUID roleReviewId) {
        RoleReviewCompliantReadDTO roleReviewCompliant = generateObject(RoleReviewCompliantReadDTO.class);
        roleReviewCompliant.setRoleReviewId(roleReviewId);
        return  roleReviewCompliant;
    }
}
