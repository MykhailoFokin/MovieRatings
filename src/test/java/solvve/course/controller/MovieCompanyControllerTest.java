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
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieCompanyService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieCompanyController.class)
public class MovieCompanyControllerTest extends BaseControllerTest {

    @MockBean
    private MovieCompanyService movieCompanyService;

    @Test
    public void testGetMovieCompany() throws Exception {
        MovieCompanyReadDTO movieCompany = generateObject(MovieCompanyReadDTO.class);

        Mockito.when(movieCompanyService.getMovieCompany(movieCompany.getId())).thenReturn(movieCompany);

        String resultJson = mvc.perform(get("/api/v1/moviecompanies/{id}", movieCompany.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieCompanyReadDTO actualMovie = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieCompany);

        Mockito.verify(movieCompanyService).getMovieCompany(movieCompany.getId());
    }

    @Test
    public void testGetMovieCompanyWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieCompany.class,wrongId);
        Mockito.when(movieCompanyService.getMovieCompany(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviecompanies/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieCompany() throws Exception {

        MovieCompanyCreateDTO create = generateObject(MovieCompanyCreateDTO.class);

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);
        read.setCompanyDetailsId(create.getCompanyDetailsId());

        Mockito.when(movieCompanyService.createMovieCompany(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assertions.assertThat(actualMovieCompany).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieCompany() throws Exception {

        MovieCompanyPatchDTO patchDTO = generateObject(MovieCompanyPatchDTO.class);

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);

        Mockito.when(movieCompanyService.patchMovieCompany(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);
    }

    @Test
    public void testDeleteMovieCompany() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviecompanies/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieCompanyService).deleteMovieCompany(id);
    }

    @Test
    public void testPutMovieCompany() throws Exception {

        MovieCompanyPutDTO putDTO = generateObject(MovieCompanyPutDTO.class);

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);
        read.setCompanyDetailsId(putDTO.getCompanyDetailsId());

        Mockito.when(movieCompanyService.updateMovieCompany(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);
    }

    @Test
    public void testGetMovieCompanies() throws Exception {
        MovieCompanyFilter movieCompanyFilter = new MovieCompanyFilter();
        movieCompanyFilter.setCompanyDetailsId(UUID.randomUUID());
        movieCompanyFilter.setMovieProductionTypes(List.of(MovieProductionType.PRODUCTION_COMPANIES));

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);
        PageResult<MovieCompanyReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(movieCompanyService.getMovieCompanies(movieCompanyFilter, PageRequest.of(0, defaultPageSize)))
                .thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/moviecompanies")
                .param("companyDetailsId", movieCompanyFilter.getCompanyDetailsId().toString())
                .param("movieProductionTypes", "PRODUCTION_COMPANIES"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<MovieCompanyReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateMovieCompanyValidationFailed() throws Exception {
        MovieCompanyCreateDTO create = new MovieCompanyCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).createMovieCompany(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieCompanyValidationFailed() throws Exception {
        MovieCompanyPutDTO put = new MovieCompanyPutDTO();

        String resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).updateMovieCompany(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieCompanyCheckLimitBorders() throws Exception {

        MovieCompanyPutDTO putDTO = generateObject(MovieCompanyPutDTO.class);
        putDTO.setDescription("D");

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);

        Mockito.when(movieCompanyService.updateMovieCompany(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieCompanyPutDTO put =generateObject(MovieCompanyPutDTO.class);
        put.setDescription("");

        String resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).updateMovieCompany(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieCompanyPutDTO put = generateObject(MovieCompanyPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/moviecompanies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).updateMovieCompany(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieCompanyCreateDTO create = generateObject(MovieCompanyCreateDTO.class);
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).createMovieCompany(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieCompanyCreateDTO create = generateObject(MovieCompanyCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).createMovieCompany(ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieCompanyCheckStingBorders() throws Exception {

        MovieCompanyCreateDTO create = generateObject(MovieCompanyCreateDTO.class);
        create.setDescription("D");

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);

        Mockito.when(movieCompanyService.createMovieCompany(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assertions.assertThat(actualMovieCompany).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/moviecompanies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assertions.assertThat(actualMovieCompany).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieCompanyCheckStringBorders() throws Exception {

        MovieCompanyPatchDTO patchDTO = generateObject(MovieCompanyPatchDTO.class);
        patchDTO.setDescription("D");

        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);

        Mockito.when(movieCompanyService.patchMovieCompany(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieCompanyReadDTO actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/moviecompanies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieCompany = objectMapper.readValue(resultJson, MovieCompanyReadDTO.class);
        Assert.assertEquals(read, actualMovieCompany);
    }

    @Test
    public void testPatchMovieCompanyDescriptionEmptyValidationFailed() throws Exception {
        MovieCompanyPatchDTO patch = generateObject(MovieCompanyPatchDTO.class);
        patch.setDescription("");

        String resultJson = mvc.perform(patch("/api/v1/moviecompanies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).patchMovieCompany(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchMovieCompanyDescriptionLimitValidationFailed() throws Exception {
        MovieCompanyPatchDTO patch = generateObject(MovieCompanyPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/moviecompanies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieCompanyService, Mockito.never()).patchMovieCompany(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testGetMovieCompaniesWithPagingAndSorting() throws Exception {
        MovieCompanyReadDTO read = generateObject(MovieCompanyReadDTO.class);
        MovieCompanyFilter filter = new MovieCompanyFilter();
        filter.setCompanyDetailsId(read.getCompanyDetailsId());

        int page = 1;
        int size = 25;

        PageResult<MovieCompanyReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(movieCompanyService.getMovieCompanies(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/moviecompanies")
                .param("companyDetailsId", filter.getCompanyDetailsId().toString())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<MovieCompanyReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<MovieCompanyReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }
}
