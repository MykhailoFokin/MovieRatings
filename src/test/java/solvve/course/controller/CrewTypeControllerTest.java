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
import solvve.course.domain.CrewType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.CrewTypeService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CrewTypeController.class)
public class CrewTypeControllerTest extends BaseControllerTest {

    @MockBean
    private CrewTypeService crewTypeService;

    private CrewTypeReadDTO createCrewTypeRead() {
        CrewTypeReadDTO crewType = new CrewTypeReadDTO();
        crewType.setId(UUID.randomUUID());
        crewType.setName("Director");
        return crewType;
    }

    @Test
    public void testGetCrewType() throws Exception {
        CrewTypeReadDTO crewType = createCrewTypeRead();

        Mockito.when(crewTypeService.getCrewType(crewType.getId())).thenReturn(crewType);

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}", crewType.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CrewTypeReadDTO actualMovie = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(crewType);

        Mockito.verify(crewTypeService).getCrewType(crewType.getId());
    }

    @Test
    public void testGetCrewTypeWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(CrewType.class,wrongId);
        Mockito.when(crewTypeService.getCrewType(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCrewTypeWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/crewtypes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateCrewType() throws Exception {

        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.createCrewType(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualCrewType).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrewType() throws Exception {

        CrewTypePatchDTO patchDTO = new CrewTypePatchDTO();
        patchDTO.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.patchCrewType(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);
    }

    @Test
    public void testDeleteCrewType() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/crewtypes/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(crewTypeService).deleteCrewType(id);
    }

    @Test
    public void testPutCrewType() throws Exception {

        CrewTypePutDTO putDTO = new CrewTypePutDTO();
        putDTO.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.updateCrewType(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);
    }

    @Test
    public void testGetCrewTypes() throws Exception {
        CrewTypeFilter crewTypeFilter = new CrewTypeFilter();
        crewTypeFilter.setName("Director");

        CrewTypeReadDTO read = createCrewTypeRead();
        PageResult<CrewTypeReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(crewTypeService.getCrewTypes(crewTypeFilter, PageRequest.of(0, defaultPageSize)))
                .thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/crewtypes")
                .param("name", crewTypeFilter.getName()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<CrewTypeReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateCrewTypeValidationFailed() throws Exception {
        CrewTypeCreateDTO create = new CrewTypeCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).createCrewType(ArgumentMatchers.any());
    }

    @Test
    public void testPutCrewTypeValidationFailed() throws Exception {
        CrewTypePutDTO put = new CrewTypePutDTO();

        String resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).updateCrewType(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCrewTypeCheckLimitBorders() throws Exception {

        CrewTypePutDTO putDTO = new CrewTypePutDTO();
        putDTO.setName("D");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.updateCrewType(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);

        // Check upper border
        putDTO.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CrewTypePutDTO put = new CrewTypePutDTO();
        put.setName("");

        String resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).updateCrewType(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CrewTypePutDTO put = new CrewTypePutDTO();
        put.setName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/crewtypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).updateCrewType(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName("");

        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).createCrewType(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).createCrewType(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCrewTypeCheckStingBorders() throws Exception {

        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName("D");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.createCrewType(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualCrewType).isEqualToComparingFieldByField(read);

        create.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/crewtypes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assertions.assertThat(actualCrewType).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrewTypeCheckStringBorders() throws Exception {

        CrewTypePatchDTO patchDTO = new CrewTypePatchDTO();
        patchDTO.setName("D");

        CrewTypeReadDTO read = createCrewTypeRead();

        Mockito.when(crewTypeService.patchCrewType(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewTypeReadDTO actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);

        patchDTO.setName(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrewType = objectMapper.readValue(resultJson, CrewTypeReadDTO.class);
        Assert.assertEquals(read, actualCrewType);
    }

    @Test
    public void testPatchCrewTypeDescriptionEmptyValidationFailed() throws Exception {
        CrewTypePatchDTO patch = new CrewTypePatchDTO();
        patch.setName("");

        String resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).patchCrewType(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCrewTypeDescriptionLimitValidationFailed() throws Exception {
        CrewTypePatchDTO patch = new CrewTypePatchDTO();
        patch.setName(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/crewtypes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewTypeService, Mockito.never()).patchCrewType(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testGetCrewTypesWithPagingAndSorting() throws Exception {
        CrewTypeReadDTO read = createCrewTypeRead();
        read.setName("XXX");
        CrewTypeFilter filter = new CrewTypeFilter();
        filter.setName(read.getName());

        int page = 1;
        int size = 25;

        PageResult<CrewTypeReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(crewTypeService.getCrewTypes(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/crewtypes")
                .param("name", filter.getName())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<CrewTypeReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<CrewTypeReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }
}
