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
import solvve.course.domain.Role;
import solvve.course.domain.RoleType;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
public class RoleControllerTest extends BaseControllerTest {

    @MockBean
    private RoleService roleService;

    @Test
    public void testGetRole() throws Exception {
        RoleReadDTO role = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.getRole(role.getId())).thenReturn(role);

        String resultJson = mvc.perform(get("/api/v1/roles/{id}", role.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(role);

        Mockito.verify(roleService).getRole(role.getId());
    }

    @Test
    public void testGetRoleWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Role.class,wrongId);
        Mockito.when(roleService.getRole(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/roles/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/roles/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRole() throws Exception {

        RoleCreateDTO create = generateObject(RoleCreateDTO.class);

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.createRole(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualRole).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRole() throws Exception {

        RolePatchDTO patchDTO = generateObject(RolePatchDTO.class);

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.patchRole(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);
    }

    @Test
    public void testDeleteRole() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/roles/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleService).deleteRole(id);
    }

    @Test
    public void testPutRole() throws Exception {

        RolePutDTO putDTO = generateObject(RolePutDTO.class);

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.updateRole(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);
    }

    @Test
    public void testCreateRoleValidationFailed() throws Exception {
        RoleCreateDTO create = new RoleCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).createRole(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleValidationFailed() throws Exception {
        RolePutDTO put = new RolePutDTO();

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).updateRole(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleCheckLimitBorders() throws Exception {

        RolePutDTO putDTO = generateObject(RolePutDTO.class);
        putDTO.setTitle(StringUtils.repeat("*", 1));
        putDTO.setDescription(StringUtils.repeat("*", 1));

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.updateRole(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);

        // Check upper border
        putDTO.setTitle(StringUtils.repeat("*", 255));
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RolePutDTO put = generateObject(RolePutDTO.class);
        put.setTitle("");
        put.setDescription("");

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).updateRole(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RolePutDTO put = generateObject(RolePutDTO.class);
        put.setTitle(StringUtils.repeat("*", 256));
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).updateRole(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RoleCreateDTO create = generateObject(RoleCreateDTO.class);
        create.setTitle("");
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).createRole(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RoleCreateDTO create = generateObject(RoleCreateDTO.class);
        create.setTitle(StringUtils.repeat("*", 256));
        create.setDescription(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).createRole(ArgumentMatchers.any());
    }

    @Test
    public void testCreateRoleCheckStingBorders() throws Exception {

        RoleCreateDTO create = generateObject(RoleCreateDTO.class);
        create.setTitle(StringUtils.repeat("*", 1));
        create.setDescription(StringUtils.repeat("*", 1));

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.createRole(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualRole).isEqualToComparingFieldByField(read);

        create.setTitle(StringUtils.repeat("*", 255));
        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/roles")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualRole).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleCheckStringBorders() throws Exception {

        RolePatchDTO patchDTO = generateObject(RolePatchDTO.class);
        patchDTO.setTitle(StringUtils.repeat("*", 1));
        patchDTO.setDescription(StringUtils.repeat("*", 1));

        RoleReadDTO read = generateObject(RoleReadDTO.class);

        Mockito.when(roleService.patchRole(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);

        patchDTO.setTitle(StringUtils.repeat("*", 255));
        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        RolePatchDTO patch = generateObject(RolePatchDTO.class);
        patch.setTitle("");
        patch.setDescription("");

        String resultJson = mvc.perform(patch("/api/v1/roles/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).patchRole(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        RolePatchDTO patch = generateObject(RolePatchDTO.class);
        patch.setTitle(StringUtils.repeat("*", 256));
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/roles/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleService, Mockito.never()).patchRole(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
