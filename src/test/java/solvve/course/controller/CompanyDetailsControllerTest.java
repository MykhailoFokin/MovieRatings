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
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.CompanyDetailsService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = CompanyDetailsController.class)
public class CompanyDetailsControllerTest extends BaseControllerTest {

    @MockBean
    private CompanyDetailsService companyDetailsService;

    @Test
    public void testGetCompanyDetails() throws Exception {
        CompanyDetailsReadDTO companyDetails = generateObject(CompanyDetailsReadDTO.class);

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

        CompanyDetailsCreateDTO create = generateObject(CompanyDetailsCreateDTO.class);

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

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

        CompanyDetailsPatchDTO patchDTO = generateObject(CompanyDetailsPatchDTO.class);

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

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

        CompanyDetailsPutDTO putDTO = generateObject(CompanyDetailsPutDTO.class);

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

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
        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);
        PageResult<CompanyDetailsReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(companyDetailsService.getCompanyDetails(companyDetailsFilter, PageRequest.of(0, defaultPageSize)))
                .thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/companydetails")
                .param("name", companyDetailsFilter.getName().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<CompanyDetailsReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateCompanyDetailValidationFailed() throws Exception {
        CompanyDetailsCreateDTO create = new CompanyDetailsCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).createCompanyDetails(ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailValidationFailed() throws Exception {
        CompanyDetailsPutDTO put = new CompanyDetailsPutDTO();

        String resultJson = mvc.perform(put("/api/v1/companydetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).updateCompanyDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailsCheckLimitBorders() throws Exception {

        CompanyDetailsPutDTO putDTO = generateObject(CompanyDetailsPutDTO.class);
        putDTO.setName("D");
        putDTO.setOverview("T");

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

        Mockito.when(companyDetailsService.updateCompanyDetails(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);

        // Check upper border
        putDTO.setName(StringUtils.repeat("*", 255));
        putDTO.setOverview(StringUtils.repeat("*", 1000));
        putDTO.setYearOfFoundation(LocalDate.now());

        resultJson = mvc.perform(put("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CompanyDetailsPutDTO put = generateObject(CompanyDetailsPutDTO.class);
        put.setName("");
        put.setOverview("");

        String resultJson = mvc.perform(put("/api/v1/companydetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).updateCompanyDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CompanyDetailsPutDTO put = generateObject(CompanyDetailsPutDTO.class);
        put.setName(StringUtils.repeat("*", 256));
        put.setOverview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/companydetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).updateCompanyDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CompanyDetailsCreateDTO create = generateObject(CompanyDetailsCreateDTO.class);
        create.setName("");
        create.setOverview("");

        String resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).createCompanyDetails(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CompanyDetailsCreateDTO create = generateObject(CompanyDetailsCreateDTO.class);
        create.setName(StringUtils.repeat("*", 256));
        create.setOverview(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).createCompanyDetails(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailsCheckStingBorders() throws Exception {

        CompanyDetailsCreateDTO create = generateObject(CompanyDetailsCreateDTO.class);
        create.setName("D");
        create.setOverview("T");

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

        Mockito.when(companyDetailsService.createCompanyDetails(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assertions.assertThat(actualCompanyDetails).isEqualToComparingFieldByField(read);

        create.setName(StringUtils.repeat("*", 255));
        create.setOverview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/companydetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assertions.assertThat(actualCompanyDetails).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCompanyDetailsCheckStringBorders() throws Exception {

        CompanyDetailsPatchDTO patchDTO = generateObject(CompanyDetailsPatchDTO.class);
        patchDTO.setName("D");
        patchDTO.setOverview("T");

        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);

        Mockito.when(companyDetailsService.patchCompanyDetails(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CompanyDetailsReadDTO actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);

        patchDTO.setName(StringUtils.repeat("*", 255));
        patchDTO.setOverview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/companydetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCompanyDetails = objectMapper.readValue(resultJson, CompanyDetailsReadDTO.class);
        Assert.assertEquals(read, actualCompanyDetails);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CompanyDetailsPatchDTO patch = generateObject(CompanyDetailsPatchDTO.class);
        patch.setName("");
        patch.setOverview("");

        String resultJson = mvc.perform(patch("/api/v1/companydetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).updateCompanyDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CompanyDetailsPatchDTO patch = generateObject(CompanyDetailsPatchDTO.class);
        patch.setName(StringUtils.repeat("*", 256));
        patch.setOverview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/companydetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(companyDetailsService, Mockito.never()).updateCompanyDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testGetCompanyDetailsWithPagingAndSorting() throws Exception {
        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);
        CompanyDetailsFilter filter = new CompanyDetailsFilter();
        filter.setName(read.getName());

        int page = 1;
        int size = 25;

        PageResult<CompanyDetailsReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(companyDetailsService.getCompanyDetails(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/companydetails")
                .param("name", filter.getName())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<CompanyDetailsReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<CompanyDetailsReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }

    @Test
    public void testGetCompanyDetailsWithBigPage() throws Exception {
        CompanyDetailsReadDTO read = generateObject(CompanyDetailsReadDTO.class);
        CompanyDetailsFilter filter = new CompanyDetailsFilter();

        int page = 0;
        int size = 99999;

        PageResult<CompanyDetailsReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, maxPageSize);
        Mockito.when(companyDetailsService.getCompanyDetails(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/companydetails")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<CompanyDetailsReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<CompanyDetailsReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }
}