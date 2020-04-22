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
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserPutDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.PortalUserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = PortalUserController.class)
public class PortalUserControllerTest extends BaseControllerTest {

    @MockBean
    private PortalUserService portalUserService;

    @Test
    public void testGetPortalUser() throws Exception {
        PortalUserReadDTO portalUser = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.getPortalUser(portalUser.getId())).thenReturn(portalUser);

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}", portalUser.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualMovie = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(portalUser);

        Mockito.verify(portalUserService).getPortalUser(portalUser.getId());
    }

    @Test
    public void testGetPortalUserWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(PortalUser.class,wrongId);
        Mockito.when(portalUserService.getPortalUser(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPortalUserWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreatePortalUser() throws Exception {

        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchPortalUser() throws Exception {

        PortalUserPatchDTO patchDTO = generateObject(PortalUserPatchDTO.class);

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.patchPortalUser(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testDeletePortalUser() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portalusers/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(portalUserService).deletePortalUser(id);
    }

    @Test
    public void testPutPortalUser() throws Exception {

        PortalUserPutDTO putDTO = generateObject(PortalUserPutDTO.class);

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.updatePortalUser(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testCreatePortalUserValidationFailed() throws Exception {
        PortalUserCreateDTO create = new PortalUserCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testPutPortalUserValidationFailed() throws Exception {
        PortalUserPutDTO put = new PortalUserPutDTO();

        String resultJson = mvc.perform(put("/api/v1/portalusers/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).updatePortalUser(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutPortalUserCheckLimitBorders() throws Exception {

        PortalUserPutDTO putDTO = generateObject(PortalUserPutDTO.class);
        putDTO.setSurname(StringUtils.repeat("*", 1));
        putDTO.setName(StringUtils.repeat("*", 1));
        putDTO.setMiddleName(StringUtils.repeat("*", 1));
        putDTO.setLogin(StringUtils.repeat("*", 1));

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.updatePortalUser(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);

        // Check upper border
        putDTO.setSurname(StringUtils.repeat("*", 255));
        putDTO.setName(StringUtils.repeat("*", 255));
        putDTO.setMiddleName(StringUtils.repeat("*", 255));
        putDTO.setLogin(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PortalUserPutDTO put = generateObject(PortalUserPutDTO.class);
        put.setSurname("");
        put.setName("");
        put.setMiddleName("");
        put.setLogin("");

        String resultJson = mvc.perform(put("/api/v1/portalusers/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).updatePortalUser(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PortalUserPutDTO put = generateObject(PortalUserPutDTO.class);
        put.setSurname(StringUtils.repeat("*", 256));
        put.setName(StringUtils.repeat("*", 256));
        put.setMiddleName(StringUtils.repeat("*", 256));
        put.setLogin(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/portalusers/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).updatePortalUser(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname("");
        create.setName("");
        create.setMiddleName("");
        create.setLogin("");

        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 256));
        create.setName(StringUtils.repeat("*", 256));
        create.setMiddleName(StringUtils.repeat("*", 256));
        create.setLogin(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testCreatePortalUserCheckStingBorders() throws Exception {

        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 1));
        create.setName(StringUtils.repeat("*", 1));
        create.setMiddleName(StringUtils.repeat("*", 1));
        create.setLogin(StringUtils.repeat("*", 1));

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);

        create.setSurname(StringUtils.repeat("*", 255));
        create.setName(StringUtils.repeat("*", 255));
        create.setMiddleName(StringUtils.repeat("*", 255));
        create.setLogin(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchPortalUserCheckStringBorders() throws Exception {

        PortalUserPatchDTO patchDTO = generateObject(PortalUserPatchDTO.class);
        patchDTO.setSurname(StringUtils.repeat("*", 1));
        patchDTO.setName(StringUtils.repeat("*", 1));
        patchDTO.setMiddleName(StringUtils.repeat("*", 1));
        patchDTO.setLogin(StringUtils.repeat("*", 1));

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.patchPortalUser(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);

        patchDTO.setSurname(StringUtils.repeat("*", 255));
        patchDTO.setName(StringUtils.repeat("*", 255));
        patchDTO.setMiddleName(StringUtils.repeat("*", 255));
        patchDTO.setLogin(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PortalUserPatchDTO patch = generateObject(PortalUserPatchDTO.class);
        patch.setSurname("");
        patch.setName("");
        patch.setMiddleName("");
        patch.setLogin("");

        String resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).patchPortalUser(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PortalUserPatchDTO patch = generateObject(PortalUserPatchDTO.class);
        patch.setSurname(StringUtils.repeat("*", 256));
        patch.setName(StringUtils.repeat("*", 256));
        patch.setMiddleName(StringUtils.repeat("*", 256));
        patch.setLogin(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).patchPortalUser(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
