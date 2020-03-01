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
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.CompanyDetailsService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CompanyDetailsController.class)
@ActiveProfiles("test")
public class CompanyDetailsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyDetailsService companyDetailsService;

    private CompanyDetailsReadDTO createCompanyDetailsRead() {
        CompanyDetailsReadDTO companyDetails = new CompanyDetailsReadDTO();
        companyDetails.setId(UUID.randomUUID());
        companyDetails.setName("Director");
        companyDetails.setOverview("Test");
        companyDetails.setYearOfFoundation(LocalDate.of(2020,01,01));
        return companyDetails;
    }

    @Test
    public void testGetCompanyDetails() throws Exception {
        CompanyDetailsReadDTO companyDetails = createCompanyDetailsRead();

        Mockito.when(companyDetailsService.getCompanyDetails(companyDetails.getId())).thenReturn(companyDetails);

        String resultJson = mvc.perform(get("/api/v1/companydetails/{id}", companyDetails.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CompanyDetailsReadDTO actualMovie = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(companyDetails);

        Mockito.verify(companyDetailsService).getCompanyDetails(companyDetails.getId());
    }

    @Test
    public void testGetCompanyDetailsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(CompanyDetails.class,wrongId);
        Mockito.when(companyDetailsService.getCompanyDetails(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/companydetails/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateCompanyDetails() throws Exception {

        CompanyDetailsCreateDTO create = new CompanyDetailsCreateDTO();
        create.setName("Director");
        create.setOverview("Test");
        create.setYearOfFoundation(LocalDate.now());

        CompanyDetailsReadDTO read = createCompanyDetailsRead();

        Mockito.when(companyDetailsService.createCompanyDetails(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assertions.assertThat(actualCompanyDetails).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCompanyDetails() throws Exception {

        CompanyDetailsPatchDTO patchDTO = new CompanyDetailsPatchDTO();
        patchDTO.setName("Director");
        patchDTO.setOverview("Test");
        patchDTO.setYearOfFoundation(LocalDate.now());

        CompanyDetailsReadDTO read = createCompanyDetailsRead();

        Mockito.when(companyDetailsService.patchCompanyDetails(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);
    }

    @Test
    public void testDeleteCompanyDetails() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/companydetails/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(companyDetailsService).deleteCompanyDetails(id);
    }

    @Test
    public void testPutCompanyDetails() throws Exception {

        CompanyDetailsPutDTO putDTO = new CompanyDetailsPutDTO();
        putDTO.setName("Director");
        putDTO.setOverview("Test");
        putDTO.setYearOfFoundation(LocalDate.now());

        CompanyDetailsReadDTO read = createCompanyDetailsRead();

        Mockito.when(companyDetailsService.updateCompanyDetails(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);
    }

    @Test
    public void testGetCompanyDetailsFitler() throws Exception {
        CompanyDetailsFilter companyDetailsFilter = new CompanyDetailsFilter();
        companyDetailsFilter.setName("Director");
        CompanyDetailsReadDTO read = createCompanyDetailsRead();
        List<CompanyDetailsReadDTO> expectedResult = List.of(read);
        Mockito.when(companyDetailsService.getCompanyDetails(companyDetailsFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/companydetails")
                .param("name", companyDetailsFilter.getName().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<CompanyDetailsReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }
}
