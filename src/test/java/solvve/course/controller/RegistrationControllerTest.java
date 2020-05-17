package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.PortalUserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationController.class)
public class RegistrationControllerTest extends BaseControllerTest {

    @MockBean
    private PortalUserService portalUserService;

    @Test
    public void testCreatePortalUser() throws Exception {

        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testCreatePortalUserValidationFailed() throws Exception {
        PortalUserCreateDTO create = new PortalUserCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testCreatePortalUserEmptyValidationFailed() throws Exception {
        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname("");
        create.setName("");
        create.setMiddleName("");
        create.setLogin("");

        String resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testCreatePortalUserLimitValidationFailed() throws Exception {
        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 256));
        create.setName(StringUtils.repeat("*", 256));
        create.setMiddleName(StringUtils.repeat("*", 256));
        create.setLogin(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(portalUserService, Mockito.never()).createPortalUser(ArgumentMatchers.any());
    }

    @Test
    public void testCreatePortalUserCheckStingBorders() throws Exception {

        PortalUserCreateDTO create = generateObject(PortalUserCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 1));
        create.setName(StringUtils.repeat("*", 1));
        create.setMiddleName(StringUtils.repeat("*", 1));
        create.setLogin(StringUtils.repeat("*", 1));

        PortalUserReadDTO read = generateObject(PortalUserReadDTO.class);

        Mockito.when(portalUserService.createPortalUser(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);

        create.setSurname(StringUtils.repeat("*", 255));
        create.setName(StringUtils.repeat("*", 255));
        create.setMiddleName(StringUtils.repeat("*", 255));
        create.setLogin(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/registration")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assertions.assertThat(actualPortalUser).isEqualToComparingFieldByField(read);
    }
}
