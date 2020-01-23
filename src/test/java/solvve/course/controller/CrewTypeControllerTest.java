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
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.CrewTypeService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CrewTypeController.class)
@ActiveProfiles("test")
public class CrewTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrewTypeService crewTypeService;

    private CrewTypeReadDTO createCrewTypeRead() {
        CrewTypeReadDTO crewType = new CrewTypeReadDTO();
        crewType.setId(UUID.randomUUID());
        crewType.setName("Director");
        return crewType;
    }

    @Test
    public void testGetCrewType() throws Exception {
        CrewTypeReadDTO crewType = createCrewTypeRead();

        Mockito.when(crewTypeService.getCrewType(crewType.getId())).thenReturn(crewType);

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}", crewType.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CrewTypeReadDTO actualMovie = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(crewType);

        Mockito.verify(crewTypeService).getCrewType(crewType.getId());
    }

    @Test
    public void testGetCrewTypeWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(CrewType.class,wrongId);
        Mockito.when(crewTypeService.getCrewType(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCrewTypeWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateCrewType() throws Exception {

        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.createCrewType(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualCrewType).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrewType() throws Exception {

        CrewTypePatchDTO patchDTO = new CrewTypePatchDTO();
        patchDTO.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.patchCrewType(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);
    }

    @Test
    public void testDeleteCrewType() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/crewtypes/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(crewTypeService).deleteCrewType(id);
    }
}
