package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.PortalUserUserRoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PortalUserUserRoleService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = PortalUserUserRoleController.class)
public class PortalUserUserRoleControllerTest extends BaseControllerTest {

    @MockBean
    private PortalUserUserRoleService portalUserUserRoleService;

    @Test
    public void testAddUserRoleToPortalUser() throws Exception {
        UUID portalUserId = UUID.randomUUID();
        UUID userRoleId = UUID.randomUUID();

        PortalUserUserRoleReadDTO read = new PortalUserUserRoleReadDTO();
        read.setId(userRoleId);
        List<PortalUserUserRoleReadDTO> expectedUserRoles = List.of(read);
        Mockito.when(portalUserUserRoleService.addUserRoleToPortalUser(portalUserId, userRoleId))
                .thenReturn(expectedUserRoles);

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/user-roles/{id}", portalUserId,
                userRoleId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PortalUserUserRoleReadDTO> actualUserRoles = objectMapper.readValue(resultJson,
                new TypeReference<List<PortalUserUserRoleReadDTO>>() {
                });
        Assert.assertEquals(expectedUserRoles, actualUserRoles);
    }

    @Test
    public void testRemoveUserRoleFromPortalUser() throws Exception {
        UUID portalUserId = UUID.randomUUID();
        UUID userRoleId = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portal-user/{portalUserId}/user-roles/{id}",portalUserId, userRoleId))
                .andExpect(status().isOk());

        Mockito.verify(portalUserUserRoleService).removeUserRoleFromPortalUser(portalUserId, userRoleId);
    }

    @Test
    public void testGetPortalUserUserRoles() throws Exception {
        UUID portalUserId = UUID.randomUUID();

        List<PortalUserUserRoleReadDTO> userRoles = List.of(generateObject(PortalUserUserRoleReadDTO.class));

        Mockito.when(portalUserUserRoleService.getPortalUserUserRoles(portalUserId)).thenReturn(userRoles);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/user-roles", portalUserId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PortalUserUserRoleReadDTO> actualUserRoles = objectMapper.readValue(resultJson,
                new TypeReference<List<PortalUserUserRoleReadDTO>>(){});
        Assertions.assertThat(actualUserRoles).isEqualTo(userRoles);

        Mockito.verify(portalUserUserRoleService).getPortalUserUserRoles(portalUserId);
    }

    @Test
    public void testGetPortalUserUserRoleWrongId() throws Exception {
        UUID portalUserId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(PortalUserUserRoleReadDTO.class, portalUserId);
        Mockito.when(portalUserUserRoleService.getPortalUserUserRoles(portalUserId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/user-roles", portalUserId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }
}
