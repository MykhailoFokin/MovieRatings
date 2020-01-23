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
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PortalUserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PortalUserController.class)
@ActiveProfiles("test")
public class PortalUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortalUserService portalUserService;

    private PortalUserReadDTO createPortalUserRead() {
        PortalUserReadDTO portalUser = new PortalUserReadDTO();
        portalUser.setId(UUID.randomUUID());
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        return portalUser;
    }

    @Test
    public void testGetPortalUser() throws Exception {
        PortalUserReadDTO portalUser = createPortalUserRead();

        Mockito.when(portalUserService.getPortalUser(portalUser.getId())).thenReturn(portalUser);

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}", portalUser.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualMovie = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(portalUser);

        Mockito.verify(portalUserService).getPortalUser(portalUser.getId());
    }

    @Test
    public void testGetPortalUserWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(PortalUser.class,wrongId);
        Mockito.when(portalUserService.getPortalUser(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPortalUserWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/portalusers/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreatePortalUser() throws Exception {

        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        create.setLogin("Login");
        create.setUserConfidence(UserConfidenceType.NORMAL);

        PortalUserReadDTO read = createPortalUserRead();

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portalusers")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchPortalUser() throws Exception {

        PortalUserPatchDTO patchDTO = new PortalUserPatchDTO();
        patchDTO.setSurname("Surname");
        patchDTO.setName("Name");
        patchDTO.setMiddleName("MiddleName");
        patchDTO.setLogin("Login");
        patchDTO.setUserConfidence(UserConfidenceType.NORMAL);

        PortalUserReadDTO read = createPortalUserRead();

        Mockito.when(portalUserService.patchPortalUser(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portalusers/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(read, actualPortalUser);
    }

    @Test
    public void testDeletePortalUser() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portalusers/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(portalUserService).deletePortalUser(id);
    }
}
