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
import solvve.course.domain.Countries;
import solvve.course.dto.CountriesCreateDTO;
import solvve.course.dto.CountriesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.CountriesService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetCountries() throws Exception {
        CountriesReadDTO countries = new CountriesReadDTO();
        countries.setId(UUID.randomUUID());
        countries.setName("Laplandia");

        Mockito.when(countriesService.getCountries(countries.getId())).thenReturn(countries);

        String resultJson = mvc.perform(get("/api/v1/countries/{id}", countries.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
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

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetCountriesWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }

    @Test
    public void testCreateCountries() throws Exception {

        CountriesCreateDTO create = new CountriesCreateDTO();
        create.setName("Laplandia");

        CountriesReadDTO read = new CountriesReadDTO();
        read.setId(UUID.randomUUID());
        read.setName("Laplandia");

        Mockito.when(countriesService.createCountries(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountriesReadDTO actualCountries = objectMapper.readValue(resultJson, CountriesReadDTO.class);
        Assertions.assertThat(actualCountries).isEqualToComparingFieldByField(read);
    }
}
