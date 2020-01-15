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
import solvve.course.domain.Countries;
import solvve.course.dto.CountriesCreateDTO;
import solvve.course.dto.CountriesPatchDTO;
import solvve.course.dto.CountriesReadDTO;
import solvve.course.exception.EntityNotFoundException;

import solvve.course.service.CountriesService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CountriesController.class)
@ActiveProfiles("test")
public class CountriesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CountriesService countriesService;

    private CountriesReadDTO createCountriesRead() {
        CountriesReadDTO countries = new CountriesReadDTO();
        countries.setId(UUID.randomUUID());
        countries.setName("Laplandia");
        return countries;
    }

    @Test
    public void testGetCountries() throws Exception {
        CountriesReadDTO countries = createCountriesRead();

        Mockito.when(countriesService.getCountries(countries.getId())).thenReturn(countries);

        String resultJson = mvc.perform(get("/api/v1/countries/{id}", countries.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountriesReadDTO actualMovie = objectMapper.readValue(resultJson, CountriesReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(countries);

        Mockito.verify(countriesService).getCountries(countries.getId());
    }

    @Test
    public void testGetCountriesWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Countries.class,wrongId);
        Mockito.when(countriesService.getCountries(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/countries/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCountriesWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/countries/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateCountries() throws Exception {

        CountriesCreateDTO create = new CountriesCreateDTO();
        create.setName("Laplandia");

        CountriesReadDTO read = createCountriesRead();

        Mockito.when(countriesService.createCountries(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountriesReadDTO actualCountries = objectMapper.readValue(resultJson, CountriesReadDTO.class);
        Assertions.assertThat(actualCountries).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCountries() throws Exception {

        CountriesPatchDTO patchDTO = new CountriesPatchDTO();
        patchDTO.setName("Laplandia");

        CountriesReadDTO read = createCountriesRead();

        Mockito.when(countriesService.patchCountries(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountriesReadDTO actualCountries = objectMapper.readValue(resultJson, CountriesReadDTO.class);
        Assert.assertEquals(read, actualCountries);
    }

    @Test
    public void testDeleteCountries() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/countries/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(countriesService).deleteCountries(id);
    }
}
