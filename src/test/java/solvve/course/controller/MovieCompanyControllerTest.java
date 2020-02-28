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
import solvve.course.domain.MovieCompany;
import solvve.course.domain.MovieProductionType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieCompanyService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieCompanyController.class)
@ActiveProfiles("test")
public class MovieCompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieCompanyService movieCompanyService;

    private MovieCompanyReadDTO createMovieCompanyRead() {
        MovieCompanyReadDTO movieCompany = new MovieCompanyReadDTO();
        movieCompany.setId(UUID.randomUUID());
        movieCompany.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);
        movieCompany.setDescription("Test");
        return movieCompany;
    }

    @Test
    public void testGetMovieCompany() throws Exception {
        MovieCompanyReadDTO movieCompany = createMovieCompanyRead();

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

        MovieCompanyCreateDTO create = new MovieCompanyCreateDTO();
        create.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);
        create.setDescription("Test");

        MovieCompanyReadDTO read = createMovieCompanyRead();

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

        MovieCompanyPatchDTO patchDTO = new MovieCompanyPatchDTO();
        patchDTO.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);
        patchDTO.setDescription("Test");

        MovieCompanyReadDTO read = createMovieCompanyRead();

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

        MovieCompanyPutDTO putDTO = new MovieCompanyPutDTO();
        putDTO.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);
        putDTO.setDescription("Test");

        MovieCompanyReadDTO read = createMovieCompanyRead();

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

        MovieCompanyReadDTO read = createMovieCompanyRead();
        List<MovieCompanyReadDTO> expectedResult = List.of(read);
        Mockito.when(movieCompanyService.getMovieCompanies(movieCompanyFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/moviecompanies")
                .param("companyDetailsId", movieCompanyFilter.getCompanyDetailsId().toString())
                .param("movieProductionTypes", "PRODUCTION_COMPANIES"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<MovieCompanyReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }
}
