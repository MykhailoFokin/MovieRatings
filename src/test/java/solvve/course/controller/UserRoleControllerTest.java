package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;
import solvve.course.dto.UserRoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.UserRoleService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = UserRoleController.class)
public class UserRoleControllerTest extends BaseControllerTest {

    @MockBean
    private UserRoleService userRoleService;

    @Test
    public void testGetUserRoles() throws Exception {
        UserRoleReadDTO userRoles = generateObject(UserRoleReadDTO.class);

        Mockito.when(userRoleService.getUserRoles(userRoles.getId())).thenReturn(userRoles);

        String resultJson = mvc.perform(get("/api/v1/user-roles/{id}", userRoles.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserRoleReadDTO actualMovie = objectMapper.readValue(resultJson, UserRoleReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(userRoles);

        Mockito.verify(userRoleService).getUserRoles(userRoles.getId());
    }

    @Test
    public void testGetUserRolesWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(UserRole.class,wrongId);
        Mockito.when(userRoleService.getUserRoles(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/user-roles/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetUserRolesWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/user-roles/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testGetUserRolesByUserGroupType() throws Exception {
        UserRoleReadDTO userRoles = generateObject(UserRoleReadDTO.class);
        userRoles.setUserGroupType(UserGroupType.MODERATOR);

        Mockito.when(userRoleService.getUserRolesByUserGroupType(UserGroupType.MODERATOR)).thenReturn(userRoles);

        String resultJson = mvc.perform(get("/api/v1/user-roles/role/{userGroupType}",
                userRoles.getUserGroupType()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserRoleReadDTO actualRole = objectMapper.readValue(resultJson, UserRoleReadDTO.class);
        Assertions.assertThat(actualRole).isEqualToComparingFieldByField(userRoles);

        Mockito.verify(userRoleService).getUserRolesByUserGroupType(userRoles.getUserGroupType());
    }
}
