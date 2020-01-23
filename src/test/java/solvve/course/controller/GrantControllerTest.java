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
import solvve.course.domain.Grant;
import solvve.course.domain.UserPermType;
import solvve.course.dto.GrantCreateDTO;
import solvve.course.dto.GrantPatchDTO;
import solvve.course.dto.GrantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.GrantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GrantController.class)
@ActiveProfiles("test")
public class GrantControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GrantService grantService;

    private GrantReadDTO createGrantsRead() {
        GrantReadDTO grants = new GrantReadDTO();
        grants.setId(UUID.randomUUID());
        grants.setObjectName("Movie");
        grants.setUserPermission(UserPermType.READ);
        return grants;
    }

    @Test
    public void testGetGrants() throws Exception {
        GrantReadDTO grants = createGrantsRead();

        Mockito.when(grantService.getGrants(grants.getId())).thenReturn(grants);

        String resultJson = mvc.perform(get("/api/v1/grants/{id}", grants.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        GrantReadDTO actualMovie = objectMapper.readValue(resultJson, GrantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(grants);

        Mockito.verify(grantService).getGrants(grants.getId());
    }

    @Test
    public void testGetGrantsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Grant.class,wrongId);
        Mockito.when(grantService.getGrants(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/grants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetGrantsWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/grants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateGrants() throws Exception {

        GrantCreateDTO create = new GrantCreateDTO();
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);

        GrantReadDTO read = createGrantsRead();

        Mockito.when(grantService.createGrants(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/grants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GrantReadDTO actualGrants = objectMapper.readValue(resultJson, GrantReadDTO.class);
        Assertions.assertThat(actualGrants).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchGrants() throws Exception {

        GrantPatchDTO patchDTO = new GrantPatchDTO();
        patchDTO.setObjectName("Movie");
        patchDTO.setUserPermission(UserPermType.READ);

        GrantReadDTO read = createGrantsRead();

        Mockito.when(grantService.patchGrants(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/grants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GrantReadDTO actualGrants = objectMapper.readValue(resultJson, GrantReadDTO.class);
        Assert.assertEquals(read, actualGrants);
    }

    @Test
    public void testDeleteGrants() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/grants/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(grantService).deleteGrants(id);
    }
}
