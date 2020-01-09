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
import solvve.course.domain.Persons;
import solvve.course.dto.PersonsCreateDTO;
import solvve.course.dto.PersonsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PersonsService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonsController.class)
@ActiveProfiles("test")
public class PersonsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonsService personsService;

    @Test
    public void testGetPersons() throws Exception {
        PersonsReadDTO persons = new PersonsReadDTO();
        persons.setId(UUID.randomUUID());
        persons.setSurname("Surname");
        persons.setName("Name");
        persons.setMiddleName("MiddleName");

        Mockito.when(personsService.getPersons(persons.getId())).thenReturn(persons);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}", persons.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        PersonsReadDTO actualMovie = objectMapper.readValue(resultJson, PersonsReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(persons);

        Mockito.verify(personsService).getPersons(persons.getId());
    }

    @Test
    public void testGetPersonsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Persons.class,wrongId);
        Mockito.when(personsService.getPersons(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetPersonsWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }

    @Test
    public void testCreatePersons() throws Exception {

        PersonsCreateDTO create = new PersonsCreateDTO();
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");

        PersonsReadDTO read = new PersonsReadDTO();
        read.setId(UUID.randomUUID());
        read.setSurname("Surname");
        read.setName("Name");
        read.setMiddleName("MiddleName");

        Mockito.when(personsService.createPersons(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonsReadDTO actualPersons = objectMapper.readValue(resultJson, PersonsReadDTO.class);
        Assertions.assertThat(actualPersons).isEqualToComparingFieldByField(read);
    }
}
