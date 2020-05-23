package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.service.RolePortalUserRoleVoteService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = RolePortalUserRoleVoteController.class)
public class RolePortalUserRoleVoteControllerTest extends BaseControllerTest {

    @MockBean
    private RolePortalUserRoleVoteService rolePortalUserRoleVoteService;

    @Test
    public void testGetRoleVoteByPortalUser() throws Exception {
        UUID portalUserId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        List<RoleVoteReadDTO> roles = List.of(generateObject(RoleVoteReadDTO.class));

        Mockito.when(rolePortalUserRoleVoteService.getRolesVotesByPortalUser(roleId, portalUserId)).thenReturn(roles);

        String resultJson = mvc.perform(get("/api/v1/role/{roleId}/portal-user/{id}/role-votes", roleId,
                portalUserId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleVoteReadDTO> actualRoles = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleVoteReadDTO>>(){});
        Assertions.assertThat(actualRoles).isEqualTo(roles);

        Mockito.verify(rolePortalUserRoleVoteService).getRolesVotesByPortalUser(roleId, portalUserId);
    }

    @Test
    public void testCreateRoleVoteByPortalUser() throws Exception {
        RoleVoteCreateDTO create = generateObject(RoleVoteCreateDTO.class);

        RoleVoteReadDTO read = generateObject(RoleVoteReadDTO.class);

        Mockito.when(rolePortalUserRoleVoteService.
                createRoleVoteByPortalUser(create.getPortalUserId(), create.getRoleId(), create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/role/{roleId}/portal-user/{id}/role-votes",
                create.getRoleId(), create.getPortalUserId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualRoleVote = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assertions.assertThat(actualRoleVote).isEqualToComparingFieldByField(read);
    }
}
