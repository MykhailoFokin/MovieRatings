package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PortalUserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetPortalUser() throws Exception {
        PortalUserReadDTO portalUser = new PortalUserReadDTO();
        portalUser.setId(UUID.randomUUID());
        //portalUser.setUserType(userTypesReadDTO.getId());
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);

        Mockito.when(portalUserService.getPortalUser(portalUser.getId())).thenReturn(portalUser);

        String resultJson = mvc.perform(get("/api/v1/portaluser/{id}", portalUser.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        PortalUserReadDTO actualMovie = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(portalUser);

        Mockito.verify(portalUserService).getPortalUser(portalUser.getId());
    }

    @Test
    public void testGetPortalUserWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(PortalUser.class,wrongId);
        Mockito.when(portalUserService.getPortalUser(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portaluser/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetPortalUserWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }

    @Test
    public void testCreatePortalUser() throws Exception {

        PortalUserCreateDTO create = new PortalUserCreateDTO();
        //create.setUserType(userTypesReadDTO.getId());
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        create.setLogin("Login");
        create.setUserConfidence(UserConfidenceType.NORMAL);

        PortalUserReadDTO read = new PortalUserReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserType(userTypesReadDTO.getId());
        read.setSurname("Surname");
        read.setName("Name");
        read.setMiddleName("MiddleName");
        read.setLogin("Login");
        read.setUserConfidence(UserConfidenceType.NORMAL);

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portaluser")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }
}
