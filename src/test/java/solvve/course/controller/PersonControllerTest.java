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
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PersonService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonController.class)
@ActiveProfiles("test")
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    private PersonReadDTO createPersonsRead() {
        PersonReadDTO persons = new PersonReadDTO();
        persons.setId(UUID.randomUUID());
        persons.setSurname("Surname");
        persons.setName("Name");
        persons.setMiddleName("MiddleName");
        return persons;
    }

    @Test
    public void testGetPersons() throws Exception {
        PersonReadDTO persons = createPersonsRead();

        Mockito.when(personService.getPersons(persons.getId())).thenReturn(persons);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}", persons.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualMovie = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(persons);

        Mockito.verify(personService).getPersons(persons.getId());
    }

    @Test
    public void testGetPersonsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Person.class,wrongId);
        Mockito.when(personService.getPersons(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPersonsWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/persons/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreatePersons() throws Exception {

        PersonCreateDTO create = new PersonCreateDTO();
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");

        PersonReadDTO read = createPersonsRead();

        Mockito.when(personService.createPersons(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPersons = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assertions.assertThat(actualPersons).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchPersons() throws Exception {

        PersonPatchDTO patchDTO = new PersonPatchDTO();
        patchDTO.setSurname("Surname");
        patchDTO.setName("Name");
        patchDTO.setMiddleName("MiddleName");

        PersonReadDTO read = createPersonsRead();

        Mockito.when(personService.patchPersons(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPersons = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPersons);
    }

    @Test
    public void testDeletePersons() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/persons/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(personService).deletePersons(id);
    }
}
