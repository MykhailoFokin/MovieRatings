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
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.UserTypeService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserTypeController.class)
@ActiveProfiles("test")
public class UserTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserTypeService userTypeService;

    private UserTypeReadDTO createUserTypesRead() {
        UserTypeReadDTO userTypes = new UserTypeReadDTO();
        userTypes.setId(UUID.randomUUID());
        userTypes.setUserGroup(UserGroupType.USER);
        return userTypes;
    }

    @Test
    public void testGetUserTypes() throws Exception {
        UserTypeReadDTO userTypes = createUserTypesRead();

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

        UserTypeCreateDTO create = new UserTypeCreateDTO();
        create.setUserGroup(UserGroupType.USER);

        UserTypeReadDTO read = createUserTypesRead();

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

        UserTypePatchDTO patchDTO = new UserTypePatchDTO();
        patchDTO.setUserGroup(UserGroupType.USER);

        UserTypeReadDTO read =createUserTypesRead();

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
}
