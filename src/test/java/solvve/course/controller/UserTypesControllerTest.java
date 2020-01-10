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
import solvve.course.domain.UserTypes;
import solvve.course.dto.UserTypesCreateDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.UserTypesService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserTypesController.class)
@ActiveProfiles("test")
public class UserTypesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserTypesService userTypesService;

    @Test
    public void testGetUserTypes() throws Exception {
        UserTypesReadDTO userTypes = new UserTypesReadDTO();
        userTypes.setId(UUID.randomUUID());
        userTypes.setUserGroup(UserGroupType.USER);

        Mockito.when(userTypesService.getUserTypes(userTypes.getId())).thenReturn(userTypes);

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}", userTypes.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        UserTypesReadDTO actualMovie = objectMapper.readValue(resultJson, UserTypesReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(userTypes);

        Mockito.verify(userTypesService).getUserTypes(userTypes.getId());
    }

    @Test
    public void testGetUserTypesWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(UserTypes.class,wrongId);
        Mockito.when(userTypesService.getUserTypes(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetUserTypesWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/usertypes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateUserTypes() throws Exception {

        UserTypesCreateDTO create = new UserTypesCreateDTO();
        create.setUserGroup(UserGroupType.USER);

        UserTypesReadDTO read = new UserTypesReadDTO();
        read.setId(UUID.randomUUID());
        read.setUserGroup(UserGroupType.USER);

        Mockito.when(userTypesService.createUserTypes(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/usertypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypesReadDTO actualUserTypes = objectMapper.readValue(resultJson, UserTypesReadDTO.class);
        Assertions.assertThat(actualUserTypes).isEqualToComparingFieldByField(read);
    }
}
