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
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserPermType;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.UserGrantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserGrantController.class)
public class UserGrantControllerTest extends BaseControllerTest {

    @MockBean
    private UserGrantService userGrantService;

    @Test
    public void testGetGrants() throws Exception {
        UserGrantReadDTO grants = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.getGrants(grants.getId())).thenReturn(grants);

        String resultJson = mvc.perform(get("/api/v1/usergrants/{id}", grants.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        UserGrantReadDTO actualMovie = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(grants);

        Mockito.verify(userGrantService).getGrants(grants.getId());
    }

    @Test
    public void testGetGrantsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(UserGrant.class,wrongId);
        Mockito.when(userGrantService.getGrants(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/usergrants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetGrantsWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/usergrants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateGrants() throws Exception {

        UserGrantCreateDTO create = generateObject(UserGrantCreateDTO.class);

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.createGrants(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualGrants = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assertions.assertThat(actualGrants).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchGrants() throws Exception {

        UserGrantPatchDTO patchDTO = generateObject(UserGrantPatchDTO.class);

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.patchGrants(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualGrants = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualGrants);
    }

    @Test
    public void testDeleteGrants() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/usergrants/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(userGrantService).deleteGrants(id);
    }

    @Test
    public void testPutGrants() throws Exception {

        UserGrantPutDTO putDTO = generateObject(UserGrantPutDTO.class);

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.updateGrants(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualGrants = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualGrants);
    }

    @Test
    public void testCreateUserGrantValidationFailed() throws Exception {
        UserGrantCreateDTO create = new UserGrantCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).createGrants(ArgumentMatchers.any());
    }

    @Test
    public void testPutUserGrantValidationFailed() throws Exception {
        UserGrantPutDTO put = new UserGrantPutDTO();

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).updateGrants(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutUserGrantCheckLimitBorders() throws Exception {

        UserGrantPutDTO putDTO = generateObject(UserGrantPutDTO.class);
        putDTO.setObjectName("M");

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.updateGrants(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualUserGrant);

        // Check upper border
        putDTO.setObjectName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualUserGrant);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        UserGrantPutDTO put = generateObject(UserGrantPutDTO.class);
        put.setObjectName("");

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).updateGrants(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        UserGrantPutDTO put = generateObject(UserGrantPutDTO.class);
        put.setObjectName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).updateGrants(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        UserGrantCreateDTO create = generateObject(UserGrantCreateDTO.class);
        create.setObjectName("");

        String resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).createGrants(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        UserGrantCreateDTO create = generateObject(UserGrantCreateDTO.class);
        create.setObjectName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).createGrants(ArgumentMatchers.any());
    }

    @Test
    public void testCreateUserGrantCheckStingBorders() throws Exception {

        UserGrantCreateDTO create = generateObject(UserGrantCreateDTO.class);
        create.setObjectName(StringUtils.repeat("*", 1));

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.createGrants(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assertions.assertThat(actualUserGrant).isEqualToComparingFieldByField(read);

        create.setObjectName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/usergrants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assertions.assertThat(actualUserGrant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchUserGrantCheckStringBorders() throws Exception {

        UserGrantPatchDTO patchDTO = generateObject(UserGrantPatchDTO.class);
        patchDTO.setObjectName(StringUtils.repeat("*", 1));

        UserGrantReadDTO read = generateObject(UserGrantReadDTO.class);

        Mockito.when(userGrantService.patchGrants(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualUserGrant);

        patchDTO.setObjectName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualUserGrant = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualUserGrant);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        UserGrantPatchDTO patch = generateObject(UserGrantPatchDTO.class);
        patch.setObjectName("");

        String resultJson = mvc.perform(patch("/api/v1/usergrants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).patchGrants(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        UserGrantPatchDTO patch = generateObject(UserGrantPatchDTO.class);
        patch.setObjectName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/usergrants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userGrantService, Mockito.never()).patchGrants(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
