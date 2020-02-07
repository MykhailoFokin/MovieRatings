package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import solvve.course.domain.Crew;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.exception.handler.RestExceptionHandler;
import solvve.course.service.CrewService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CrewController.class)
@ActiveProfiles("test")
public class CrewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrewService crewService;

    private CrewReadDTO createCrewRead() {
        CrewReadDTO crew = new CrewReadDTO();
        crew.setId(UUID.randomUUID());
        crew.setDescription("Description");
        return crew;
    }

    @Test
    public void testGetCrew() throws Exception {
        CrewReadExtendedDTO crew = new CrewReadExtendedDTO();
        crew.setId(UUID.randomUUID());
        crew.setDescription("Description");

        Mockito.when(crewService.getCrew(crew.getId())).thenReturn(crew);

        String resultJson = mvc.perform(get("/api/v1/crew/{id}", crew.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CrewReadExtendedDTO actualMovie = objectMapper.readValue(resultJson, CrewReadExtendedDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(crew);

        Mockito.verify(crewService).getCrew(crew.getId());
    }

    @Test
    public void testGetCrewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Crew.class,wrongId);
        Mockito.when(crewService.getCrew(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/crew/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCrewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/crew/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateCrew() throws Exception {

        CrewCreateDTO create = new CrewCreateDTO();
        create.setDescription("Description");

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.createCrew(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assertions.assertThat(actualCrew).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrew() throws Exception {

        CrewPatchDTO patchDTO = new CrewPatchDTO();
        patchDTO.setDescription("Description");

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.patchCrew(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testDeleteCrew() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/crew/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(crewService).deleteCrew(id);
    }

    @Test
    public void testPutCrew() throws Exception {

        CrewPutDTO putDTO = new CrewPutDTO();
        putDTO.setDescription("Description");

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.putCrew(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testGetCrews() throws Exception {
        CrewFilter crewFilter = new CrewFilter();
        crewFilter.setDescription("Description");

        CrewReadDTO read = createCrewRead();
        List<CrewReadDTO> expectedResult = List.of(read);
        Mockito.when(crewService.getCrews(crewFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/crew")
                .param("description", crewFilter.getDescription().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CrewReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }
}
