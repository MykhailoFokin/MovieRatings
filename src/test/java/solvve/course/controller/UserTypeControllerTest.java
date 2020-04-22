package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypePutDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.UserTypeService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = UserTypeController.class)
public class UserTypeControllerTest extends BaseControllerTest {

    @MockBean
    private UserTypeService userTypeService;

    @Test
    public void testGetUserTypes() throws Exception {
        UserTypeReadDTO userTypes = generateObject(UserTypeReadDTO.class);

        Mockito.when(userTypeService.getUserTypes(userTypes.getId())).thenReturn(userTypes);

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}", userTypes.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypeReadDTO actualMovie = objectMapper.readValue(resultJson, UserTypeReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(userTypes);

        Mockito.verify(userTypeService).getUserTypes(userTypes.getId());
    }

    @Test
    public void testGetUserTypesWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(UserType.class,wrongId);
        Mockito.when(userTypeService.getUserTypes(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetUserTypesWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateUserTypes() throws Exception {

        UserTypeCreateDTO create = generateObject(UserTypeCreateDTO.class);

        UserTypeReadDTO read = generateObject(UserTypeReadDTO.class);

        Mockito.when(userTypeService.createUserTypes(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/usertypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypeReadDTO actualUserTypes = objectMapper.readValue(resultJson, UserTypeReadDTO.class);
        Assertions.assertThat(actualUserTypes).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchUserTypes() throws Exception {

        UserTypePatchDTO patchDTO = generateObject(UserTypePatchDTO.class);

        UserTypeReadDTO read =generateObject(UserTypeReadDTO.class);

        Mockito.when(userTypeService.patchUserTypes(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/usertypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypeReadDTO actualUserTypes = objectMapper.readValue(resultJson, UserTypeReadDTO.class);
        Assert.assertEquals(read, actualUserTypes);
    }

    @Test
    public void testDeleteUserTypes() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/usertypes/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(userTypeService).deleteUserTypes(id);
    }

    @Test
    public void testPutUserTypes() throws Exception {

        UserTypePutDTO putDTO = generateObject(UserTypePutDTO.class);

        UserTypeReadDTO read =generateObject(UserTypeReadDTO.class);

        Mockito.when(userTypeService.updateUserTypes(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/usertypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypeReadDTO actualUserTypes = objectMapper.readValue(resultJson, UserTypeReadDTO.class);
        Assert.assertEquals(read, actualUserTypes);
    }

    @Test
    public void testCreateUserTypeValidationFailed() throws Exception {
        UserTypeCreateDTO create = new UserTypeCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/usertypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userTypeService, Mockito.never()).createUserTypes(ArgumentMatchers.any());
    }

    @Test
    public void testPutUserTypeValidationFailed() throws Exception {
        UserTypePutDTO put = new UserTypePutDTO();

        String resultJson = mvc.perform(put("/api/v1/usertypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userTypeService, Mockito.never()).updateUserTypes(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
