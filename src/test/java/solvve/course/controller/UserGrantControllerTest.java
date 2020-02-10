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
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserPermType;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.UserGrantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserGrantController.class)
@ActiveProfiles("test")
public class UserGrantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserGrantService userGrantService;

    private UserGrantReadDTO createGrantsRead() {
        UserGrantReadDTO grants = new UserGrantReadDTO();
        grants.setId(UUID.randomUUID());
        grants.setObjectName("Movie");
        grants.setUserPermission(UserPermType.READ);
        return grants;
    }

    @Test
    public void testGetGrants() throws Exception {
        UserGrantReadDTO grants = createGrantsRead();

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

        UserGrantCreateDTO create = new UserGrantCreateDTO();
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);

        UserGrantReadDTO read = createGrantsRead();

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

        UserGrantPatchDTO patchDTO = new UserGrantPatchDTO();
        patchDTO.setObjectName("Movie");
        patchDTO.setUserPermission(UserPermType.READ);

        UserGrantReadDTO read = createGrantsRead();

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

        UserGrantPutDTO putDTO = new UserGrantPutDTO();
        putDTO.setObjectName("Movie");
        putDTO.setUserPermission(UserPermType.READ);

        UserGrantReadDTO read = createGrantsRead();

        Mockito.when(userGrantService.updateGrants(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/usergrants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserGrantReadDTO actualGrants = objectMapper.readValue(resultJson, UserGrantReadDTO.class);
        Assert.assertEquals(read, actualGrants);
    }
}
