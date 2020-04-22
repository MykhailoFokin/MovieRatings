package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import solvve.course.domain.Country;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;

import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.CountryService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = CountryController.class)
public class CountryControllerTest extends BaseControllerTest {

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountries() throws Exception {
        CountryReadDTO countries = generateObject(CountryReadDTO.class);

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

        CountryCreateDTO create = generateObject(CountryCreateDTO.class);

        CountryReadDTO read = generateObject(CountryReadDTO.class);

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

        CountryPatchDTO patchDTO = generateObject(CountryPatchDTO.class);

        CountryReadDTO read = generateObject(CountryReadDTO.class);

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

        CountryPutDTO putDTO = generateObject(CountryPutDTO.class);

        CountryReadDTO read = generateObject(CountryReadDTO.class);

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

        CountryReadDTO read = generateObject(CountryReadDTO.class);
        read.setName("Ukraine");

        PageResult<CountryReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(countryService.getCountries(countryFilter, PageRequest.of(0, defaultPageSize)))
                .thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/countries")
                .param("names", "Ukraine"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<CountryReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateCountryValidationFailed() throws Exception {
        CountryCreateDTO create = new CountryCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).createCountries(ArgumentMatchers.any());
    }

    @Test
    public void testPutCountryValidationFailed() throws Exception {
        CountryPutDTO put = new CountryPutDTO();

        String resultJson = mvc.perform(put("/api/v1/countries/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).updateCountries(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCountryCheckLimitBorders() throws Exception {

        CountryPutDTO putDTO = generateObject(CountryPutDTO.class);
        putDTO.setName("D");

        CountryReadDTO read = generateObject(CountryReadDTO.class);

        Mockito.when(countryService.updateCountries(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountry);

        // Check upper border
        putDTO.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountry);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CountryPutDTO put = generateObject(CountryPutDTO.class);
        put.setName("");

        String resultJson = mvc.perform(put("/api/v1/countries/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).updateCountries(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CountryPutDTO put = generateObject(CountryPutDTO.class);
        put.setName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/countries/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).updateCountries(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CountryCreateDTO create = generateObject(CountryCreateDTO.class);
        create.setName("");

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).createCountries(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CountryCreateDTO create = generateObject(CountryCreateDTO.class);
        create.setName(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).createCountries(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCountryCheckStingBorders() throws Exception {

        CountryCreateDTO create = generateObject(CountryCreateDTO.class);
        create.setName("D");

        CountryReadDTO read = generateObject(CountryReadDTO.class);

        Mockito.when(countryService.createCountries(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assertions.assertThat(actualCountry).isEqualToComparingFieldByField(read);

        create.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/countries")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assertions.assertThat(actualCountry).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCountryCheckStringBorders() throws Exception {

        CountryPatchDTO patchDTO = generateObject(CountryPatchDTO.class);
        patchDTO.setName("D");

        CountryReadDTO read = generateObject(CountryReadDTO.class);

        Mockito.when(countryService.patchCountries(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CountryReadDTO actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountry);

        patchDTO.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/countries/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCountry = objectMapper.readValue(resultJson, CountryReadDTO.class);
        Assert.assertEquals(read, actualCountry);
    }

    @Test
    public void testPatchCountryDescriptionEmptyValidationFailed() throws Exception {
        CountryPatchDTO patch = generateObject(CountryPatchDTO.class);
        patch.setName("");

        String resultJson = mvc.perform(patch("/api/v1/countries/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).patchCountries(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCountryDescriptionLimitValidationFailed() throws Exception {
        CountryPatchDTO patch = generateObject(CountryPatchDTO.class);
        patch.setName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/countries/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(countryService, Mockito.never()).patchCountries(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testGetCountryWithPagingAndSorting() throws Exception {
        CountryReadDTO read = new CountryReadDTO();
        read.setName("XXX");
        CountryFilter filter = new CountryFilter();
        filter.setNames(Set.of(read.getName()));

        int page = 1;
        int size = 25;

        PageResult<CountryReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(countryService.getCountries(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/countries")
                .param("names", "XXX")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<CountryReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<CountryReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }

    @Test
    public void testNoSession() throws Exception {
        UUID wrongId = UUID.randomUUID();

        Mockito.when(countryService.getCountries(wrongId)).thenReturn(new CountryReadDTO());

        MvcResult mvcResult = mvc.perform(get("/api/v1/countries/{id}", wrongId))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNull(mvcResult.getRequest().getSession(false));
    }
}
