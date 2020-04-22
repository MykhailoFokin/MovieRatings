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
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.RoleReview;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.PortalUserRoleReviewService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = PortalUserRoleReviewController.class)
public class PortalUserRoleReviewControllerTest extends BaseControllerTest {

    @MockBean
    private PortalUserRoleReviewService portalUserRoleReviewService;

    @Test
    public void testGetRoleReview() throws Exception {
        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);
        List<RoleReviewReadDTO> roleReview = List.of(createRoleReview(portalUserReadDTO.getId()));

        Mockito.when(portalUserRoleReviewService.getPortalUserRoleReview(portalUserReadDTO.getId()))
                .thenReturn(roleReview);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews",
                portalUserReadDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReviewReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReviewReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleReview);

        Mockito.verify(portalUserRoleReviewService).getPortalUserRoleReview(portalUserReadDTO.getId());
    }

    @Test
    public void testGetRoleReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReview.class,wrongId);
        Mockito.when(portalUserRoleReviewService.getPortalUserRoleReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);

        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setPortalUserId(portalUserReadDTO.getId());
        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());
        read.setRoleId(create.getRoleId());

        Mockito.when(portalUserRoleReviewService.createPortalUserRoleReview(portalUserReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                portalUserReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);

        RoleReviewPatchDTO patchDTO = generateObject(RoleReviewPatchDTO.class);
        patchDTO.setPortalUserId(portalUserReadDTO.getId());
        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());

        Mockito.when(portalUserRoleReviewService.patchPortalUserRoleReview(portalUserReadDTO.getId(),
                read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}"
                ,portalUserReadDTO.getId() ,read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testDeleteRoleReview() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(portalUserRoleReviewService).deletePortalUserRoleReview(id, id);
    }

    @Test
    public void testPutRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);

        RoleReviewPutDTO putDTO = generateObject(RoleReviewPutDTO.class);
        putDTO.setPortalUserId(portalUserReadDTO.getId());

        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());
        read.setRoleId(putDTO.getRoleId());

        Mockito.when(portalUserRoleReviewService.updatePortalUserRoleReview(portalUserReadDTO.getId(),
                read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}"
                , portalUserReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testCreateRoleReviewValidationFailed() throws Exception {
        RoleReviewCreateDTO create = new RoleReviewCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).createPortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewValidationFailed() throws Exception {
        RoleReviewPutDTO put = new RoleReviewPutDTO();

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).updatePortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleReviewCheckLimitBorders() throws Exception {

        RoleReviewPutDTO putDTO = generateObject(RoleReviewPutDTO.class);
        putDTO.setTextReview(StringUtils.repeat("*", 1));

        RoleReviewReadDTO read = createRoleReview(putDTO.getPortalUserId());

        Mockito.when(portalUserRoleReviewService.updatePortalUserRoleReview(putDTO.getPortalUserId(), read.getId(),
                putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                putDTO.getPortalUserId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);

        // Check upper border
        putDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                putDTO.getPortalUserId(), read.getId().toString())
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

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).updatePortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewPutDTO put = generateObject(RoleReviewPutDTO.class);
        put.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).updatePortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview("");

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).createPortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).createPortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleReviewCheckStingBorders() throws Exception {

        RoleReviewCreateDTO create = generateObject(RoleReviewCreateDTO.class);
        create.setTextReview(StringUtils.repeat("*", 1));

        RoleReviewReadDTO read = createRoleReview(create.getPortalUserId());

        Mockito.when(portalUserRoleReviewService.createPortalUserRoleReview(create.getPortalUserId(), create))
                .thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                create.getPortalUserId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);

        create.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews", create.getPortalUserId())
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

        RoleReviewReadDTO read = createRoleReview(patchDTO.getPortalUserId());

        Mockito.when(portalUserRoleReviewService.patchPortalUserRoleReview(patchDTO.getPortalUserId(), read.getId(),
                patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                patchDTO.getPortalUserId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);

        patchDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                patchDTO.getPortalUserId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRoleReview = objectMapper.readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleReviewPatchDTO patch = new RoleReviewPatchDTO();
        patch.setTextReview("");

        String resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).patchPortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleReviewPatchDTO patch = generateObject(RoleReviewPatchDTO.class);
        patch.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserRoleReviewService, Mockito.never()).patchPortalUserRoleReview(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    private RoleReviewReadDTO createRoleReview(UUID portalUserId) {
        RoleReviewReadDTO roleReviewReadDTO = generateObject(RoleReviewReadDTO.class);
        roleReviewReadDTO.setPortalUserId(portalUserId);
        return roleReviewReadDTO;
    }
}
