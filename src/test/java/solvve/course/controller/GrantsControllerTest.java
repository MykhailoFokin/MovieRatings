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
import solvve.course.domain.Grants;
import solvve.course.domain.UserPermType;
import solvve.course.dto.GrantsCreateDTO;
import solvve.course.dto.GrantsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.GrantsService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GrantsController.class)
@ActiveProfiles("test")
public class GrantsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GrantsService grantsService;

    @Test
    public void testGetGrants() throws Exception {
        GrantsReadDTO grants = new GrantsReadDTO();
        grants.setId(UUID.randomUUID());
        //grants.setUserTypeId(userTypes);
        grants.setObjectName("Movie");
        grants.setUserPermission(UserPermType.READ);
        //grants.setGrantedBy(portalUserReadDTO.getId());

        Mockito.when(grantsService.getGrants(grants.getId())).thenReturn(grants);

        String resultJson = mvc.perform(get("/api/v1/grants/{id}", grants.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        GrantsReadDTO actualMovie = objectMapper.readValue(resultJson, GrantsReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(grants);

        Mockito.verify(grantsService).getGrants(grants.getId());
    }

    @Test
    public void testGetGrantsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Grants.class,wrongId);
        Mockito.when(grantsService.getGrants(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/grants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetGrantsWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/grants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateGrants() throws Exception {

        GrantsCreateDTO create = new GrantsCreateDTO();
        //create.setUserTypeId(userTypes);
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);
        //create.setGrantedBy(portalUserReadDTO.getId());

        GrantsReadDTO read = new GrantsReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserTypeId(userTypes);
        read.setObjectName("Movie");
        read.setUserPermission(UserPermType.READ);
        //read.setGrantedBy(portalUserReadDTO.getId());

        Mockito.when(grantsService.createGrants(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/grants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GrantsReadDTO actualGrants = objectMapper.readValue(resultJson, GrantsReadDTO.class);
        Assertions.assertThat(actualGrants).isEqualToComparingFieldByField(read);
    }
}
