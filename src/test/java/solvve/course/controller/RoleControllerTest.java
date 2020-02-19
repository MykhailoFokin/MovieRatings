package solvve.course.controller;

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
import solvve.course.domain.Role;
import solvve.course.domain.RoleType;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleController.class)
@ActiveProfiles("test")
public class RoleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    private RoleReadDTO createRoleRead() {
        RoleReadDTO role = new RoleReadDTO();
        role.setId(UUID.randomUUID());
        role.setTitle("Actor");
        role.setRoleType(RoleType.LEAD);
        role.setDescription("Description test");
        return role;
    }

    @Test
    public void testGetRole() throws Exception {
        RoleReadDTO role = createRoleRead();

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

        RoleCreateDTO create = new RoleCreateDTO();
        create.setTitle("Actor");
        create.setRoleType(RoleType.LEAD);
        create.setDescription("Description test");

        RoleReadDTO read = createRoleRead();

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

        RolePatchDTO patchDTO = new RolePatchDTO();
        patchDTO.setTitle("Role Test");
        patchDTO.setRoleType(RoleType.LEAD);
        patchDTO.setDescription("Description test");

        RoleReadDTO read = createRoleRead();

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

        RolePutDTO putDTO = new RolePutDTO();
        putDTO.setTitle("Role Test");
        putDTO.setRoleType(RoleType.LEAD);
        putDTO.setDescription("Description test");

        RoleReadDTO read = createRoleRead();

        Mockito.when(roleService.updateRole(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/roles/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReadDTO actualRole = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assert.assertEquals(read, actualRole);
    }
}
