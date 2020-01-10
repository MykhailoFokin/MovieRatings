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
import solvve.course.domain.Crew;
import solvve.course.dto.CrewCreateDTO;
import solvve.course.dto.CrewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.CrewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetCrew() throws Exception {
        CrewReadDTO crew = new CrewReadDTO();
        crew.setId(UUID.randomUUID());
        //crew.setPersonId(personsReadDTO.getId());
        //crew.setCrewType(crewTypeReadDTO.getId());
        //crew.setMovieId(movieReadDTO.getId());
        crew.setDescription("Description");

        Mockito.when(crewService.getCrew(crew.getId())).thenReturn(crew);

        String resultJson = mvc.perform(get("/api/v1/crew/{id}", crew.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CrewReadDTO actualMovie = objectMapper.readValue(resultJson, CrewReadDTO.class);
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

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/crew/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateCrew() throws Exception {

        CrewCreateDTO create = new CrewCreateDTO();
        //create.setPersonId(personsReadDTO.getId());
        //create.setCrewType(crewTypeReadDTO.getId());
        //create.setMovieId(movieReadDTO.getId());
        create.setDescription("Description");

        CrewReadDTO read = new CrewReadDTO();
        read.setId(UUID.randomUUID());
        //read.setPersonId(personsReadDTO.getId());
        //read.setCrewType(crewTypeReadDTO.getId());
        //read.setMovieId(movieReadDTO.getId());
        read.setDescription("Description");

        Mockito.when(crewService.createCrew(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assertions.assertThat(actualCrew).isEqualToComparingFieldByField(read);
    }
}
