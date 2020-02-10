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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.Country;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;

import solvve.course.service.CountryService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CountryController.class)
@ActiveProfiles("test")
public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountries() throws Exception {
        CountryReadDTO countries = createCountriesRead();

        Mockito.when(countryService.getCountries(countries.getId())).thenReturn(countries);

        String resultJson = mvc.perform(get("/api/v1/countries/{id}", countries.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualMovie = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(countries);

        Mockito.verify(countryService).getCountries(countries.getId());
    }

    @Test
    public void testGetCountriesWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Country.class,wrongId);
        Mockito.when(countryService.getCountries(wrongId)).thenThrow(exception);

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

        CountryCreateDTO create = new CountryCreateDTO();
        create.setName("Laplandia");

        CountryReadDTO read = createCountriesRead();

        Mockito.when(countryService.createCountries(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountries = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assertions.assertThat(actualCountries).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCountries() throws Exception {

        CountryPatchDTO patchDTO = new CountryPatchDTO();
        patchDTO.setName("Laplandia");

        CountryReadDTO read = createCountriesRead();

        Mockito.when(countryService.patchCountries(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountries = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountries);
    }

    @Test
    public void testDeleteCountries() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/countries/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(countryService).deleteCountries(id);
    }

    @Test
    public void testPutCountries() throws Exception {

        CountryPutDTO putDTO = new CountryPutDTO();
        putDTO.setName("Laplandia");

        CountryReadDTO read = createCountriesRead();

        Mockito.when(countryService.updateCountries(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountries = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountries);
    }

    @Test
    public void testGetCountriesFilter() throws Exception {
        CountryFilter countryFilter = new CountryFilter();
        countryFilter.setNames(Set.of("Ukraine"));

        CountryReadDTO read = new CountryReadDTO();
        read.setName("Ukraine");
        List<CountryReadDTO> expectedResult = List.of(read);
        Mockito.when(countryService.getCountries(countryFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/countries")
                .param("names", "Ukraine"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CountryReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }

    private CountryReadDTO createCountriesRead() {
        CountryReadDTO countries = new CountryReadDTO();
        countries.setId(UUID.randomUUID());
        countries.setName("Laplandia");
        return countries;
    }
}
