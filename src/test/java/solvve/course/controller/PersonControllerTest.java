package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.Person;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.PersonService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest extends BaseControllerTest {

    @MockBean
    private PersonService personService;

    @Test
    public void testGetPersons() throws Exception {
        PersonReadDTO persons = generateObject(PersonReadDTO.class);

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

        PersonCreateDTO create = generateObject(PersonCreateDTO.class);

        PersonReadDTO read = generateObject(PersonReadDTO.class);

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

        PersonPatchDTO patchDTO = generateObject(PersonPatchDTO.class);

        PersonReadDTO read = generateObject(PersonReadDTO.class);

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

    @Test
    public void testPutPersons() throws Exception {

        PersonPutDTO putDTO = generateObject(PersonPutDTO.class);

        PersonReadDTO read = generateObject(PersonReadDTO.class);

        Mockito.when(personService.updatePersons(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPersons = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPersons);
    }

    @Test
    public void testCreatePersonValidationFailed() throws Exception {
        PersonCreateDTO create = new PersonCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).createPersons(ArgumentMatchers.any());
    }

    @Test
    public void testPutPersonValidationFailed() throws Exception {
        PersonPutDTO put = new PersonPutDTO();

        String resultJson = mvc.perform(put("/api/v1/persons/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).updatePersons(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutPersonCheckLimitBorders() throws Exception {

        PersonPutDTO putDTO = generateObject(PersonPutDTO.class);
        putDTO.setSurname("S");
        putDTO.setName("N");
        putDTO.setMiddleName("M");

        PersonReadDTO read = generateObject(PersonReadDTO.class);

        Mockito.when(personService.updatePersons(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPerson);

        // Check upper border
        putDTO.setSurname(StringUtils.repeat("*", 255));
        putDTO.setName(StringUtils.repeat("*", 255));
        putDTO.setMiddleName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPerson);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PersonPutDTO put = generateObject(PersonPutDTO.class);
        put.setSurname("");
        put.setName("");
        put.setMiddleName("");

        String resultJson = mvc.perform(put("/api/v1/persons/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).updatePersons(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PersonPutDTO put = generateObject(PersonPutDTO.class);
        put.setSurname(StringUtils.repeat("*", 256));
        put.setName(StringUtils.repeat("*", 256));
        put.setMiddleName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/persons/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).updatePersons(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PersonCreateDTO create = generateObject(PersonCreateDTO.class);
        create.setSurname("");
        create.setName("");
        create.setMiddleName("");

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).createPersons(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PersonCreateDTO create = generateObject(PersonCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 256));
        create.setName(StringUtils.repeat("*", 256));
        create.setMiddleName(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).createPersons(ArgumentMatchers.any());
    }

    @Test
    public void testCreatePersonCheckStingBorders() throws Exception {

        PersonCreateDTO create = generateObject(PersonCreateDTO.class);
        create.setSurname(StringUtils.repeat("*", 1));
        create.setName(StringUtils.repeat("*", 1));
        create.setMiddleName(StringUtils.repeat("*", 1));

        PersonReadDTO read = generateObject(PersonReadDTO.class);

        Mockito.when(personService.createPersons(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assertions.assertThat(actualPerson).isEqualToComparingFieldByField(read);

        create.setSurname(StringUtils.repeat("*", 255));
        create.setName(StringUtils.repeat("*", 255));
        create.setMiddleName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assertions.assertThat(actualPerson).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchPersonCheckStringBorders() throws Exception {

        PersonPatchDTO patchDTO = generateObject(PersonPatchDTO.class);
        patchDTO.setSurname(StringUtils.repeat("*", 1));
        patchDTO.setName(StringUtils.repeat("*", 1));
        patchDTO.setMiddleName(StringUtils.repeat("*", 1));

        PersonReadDTO read = generateObject(PersonReadDTO.class);

        Mockito.when(personService.patchPersons(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PersonReadDTO actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPerson);

        patchDTO.setSurname(StringUtils.repeat("*", 255));
        patchDTO.setName(StringUtils.repeat("*", 255));
        patchDTO.setMiddleName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/persons/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualPerson = objectMapper.readValue(resultJson, PersonReadDTO.class);
        Assert.assertEquals(read, actualPerson);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        PersonPatchDTO patch = generateObject(PersonPatchDTO.class);
        patch.setSurname("");
        patch.setName("");
        patch.setMiddleName("");

        String resultJson = mvc.perform(patch("/api/v1/persons/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).patchPersons(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        PersonPatchDTO patch = generateObject(PersonPatchDTO.class);
        patch.setSurname(StringUtils.repeat("*", 256));
        patch.setName(StringUtils.repeat("*", 256));
        patch.setMiddleName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/persons/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(personService, Mockito.never()).patchPersons(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
