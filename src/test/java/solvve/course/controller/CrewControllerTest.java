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
import solvve.course.domain.Crew;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.CrewService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CrewController.class)
public class CrewControllerTest extends BaseControllerTest {

    @MockBean
    private CrewService crewService;

    private CrewReadDTO createCrewRead() {
        CrewReadDTO crew = new CrewReadDTO();
        crew.setId(UUID.randomUUID());
        crew.setDescription("Description");
        return crew;
    }

    @Test
    public void testGetCrew() throws Exception {
        CrewReadExtendedDTO crew = new CrewReadExtendedDTO();
        crew.setId(UUID.randomUUID());
        crew.setDescription("Description");

        Mockito.when(crewService.getCrew(crew.getId())).thenReturn(crew);

        String resultJson = mvc.perform(get("/api/v1/crew/{id}", crew.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        CrewReadExtendedDTO actualMovie = objectMapper.readValue(resultJson, CrewReadExtendedDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(crew);

        Mockito.verify(crewService).getCrew(crew.getId());
    }

    @Test
    public void testGetCrewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Crew.class,wrongId);
        Mockito.when(crewService.getCrew(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/crew/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCrewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/crew/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateCrew() throws Exception {

        CrewCreateDTO create = new CrewCreateDTO();
        create.setDescription("Description");
        create.setMovieId(UUID.randomUUID());
        create.setPersonId(UUID.randomUUID());
        create.setCrewTypeId(UUID.randomUUID());

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.createCrew(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assertions.assertThat(actualCrew).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrew() throws Exception {

        CrewPatchDTO patchDTO = new CrewPatchDTO();
        patchDTO.setDescription("Description");

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.patchCrew(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testDeleteCrew() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/crew/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(crewService).deleteCrew(id);
    }

    @Test
    public void testPutCrew() throws Exception {

        CrewPutDTO putDTO = new CrewPutDTO();
        putDTO.setDescription("Description");
        putDTO.setMovieId(UUID.randomUUID());
        putDTO.setCrewTypeId(UUID.randomUUID());

        CrewReadDTO read = createCrewRead();
        read.setMovieId(putDTO.getMovieId());
        read.setCrewTypeId(putDTO.getCrewTypeId());

        Mockito.when(crewService.updateCrew(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testGetCrews() throws Exception {
        CrewFilter crewFilter = new CrewFilter();
        crewFilter.setDescription("Description");

        CrewReadDTO read = createCrewRead();
        PageResult<CrewReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(crewService.getCrews(crewFilter, PageRequest.of(0, defaultPageSize))).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/crew")
                .param("description", crewFilter.getDescription().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<CrewReadDTO> actualResult =
                objectMapper.readValue(resultJson, new TypeReference<PageResult<CrewReadDTO>>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateCrewValidationFailed() throws Exception {
        CrewCreateDTO create = new CrewCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).createCrew(ArgumentMatchers.any());
    }

    @Test
    public void testPutCrewValidationFailed() throws Exception {
        CrewPutDTO put = new CrewPutDTO();

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).updateCrew(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCrewCheckLimitBorders() throws Exception {

        CrewPutDTO putDTO = new CrewPutDTO();
        putDTO.setDescription("D");
        putDTO.setMovieId(UUID.randomUUID());
        putDTO.setPersonId(UUID.randomUUID());
        putDTO.setCrewTypeId(UUID.randomUUID());

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.updateCrew(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));
        putDTO.setMovieId(UUID.randomUUID());
        putDTO.setPersonId(UUID.randomUUID());
        putDTO.setCrewTypeId(UUID.randomUUID());

        resultJson = mvc.perform(put("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CrewPutDTO put = new CrewPutDTO();
        put.setDescription("");
        put.setMovieId(UUID.randomUUID());
        put.setPersonId(UUID.randomUUID());
        put.setCrewTypeId(UUID.randomUUID());

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).updateCrew(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CrewPutDTO put = new CrewPutDTO();
        put.setDescription(StringUtils.repeat("*", 1001));
        put.setMovieId(UUID.randomUUID());
        put.setPersonId(UUID.randomUUID());
        put.setCrewTypeId(UUID.randomUUID());

        String resultJson = mvc.perform(put("/api/v1/crew/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).updateCrew(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        CrewCreateDTO create = new CrewCreateDTO();
        create.setDescription("");
        create.setMovieId(UUID.randomUUID());
        create.setPersonId(UUID.randomUUID());
        create.setCrewTypeId(UUID.randomUUID());

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).createCrew(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        CrewCreateDTO create = new CrewCreateDTO();
        create.setDescription(StringUtils.repeat("*", 1001));
        create.setMovieId(UUID.randomUUID());
        create.setPersonId(UUID.randomUUID());
        create.setCrewTypeId(UUID.randomUUID());


        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).createCrew(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCrewCheckStingBorders() throws Exception {

        CrewCreateDTO create = new CrewCreateDTO();
        create.setDescription("D");
        create.setMovieId(UUID.randomUUID());
        create.setPersonId(UUID.randomUUID());
        create.setCrewTypeId(UUID.randomUUID());

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.createCrew(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assertions.assertThat(actualCrew).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));
        create.setMovieId(UUID.randomUUID());
        create.setPersonId(UUID.randomUUID());
        create.setCrewTypeId(UUID.randomUUID());

        resultJson = mvc.perform(post("/api/v1/crew")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assertions.assertThat(actualCrew).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchCrewCheckStringBorders() throws Exception {

        CrewPatchDTO patchDTO = new CrewPatchDTO();
        patchDTO.setDescription("D");
        patchDTO.setMovieId(UUID.randomUUID());
        patchDTO.setPersonId(UUID.randomUUID());
        patchDTO.setCrewTypeId(UUID.randomUUID());

        CrewReadDTO read = createCrewRead();

        Mockito.when(crewService.patchCrew(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CrewReadDTO actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));
        patchDTO.setMovieId(UUID.randomUUID());
        patchDTO.setPersonId(UUID.randomUUID());
        patchDTO.setCrewTypeId(UUID.randomUUID());

        resultJson = mvc.perform(patch("/api/v1/crew/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualCrew = objectMapper.readValue(resultJson, CrewReadDTO.class);
        Assert.assertEquals(read, actualCrew);
    }

    @Test
    public void testPatchCrewDescriptionEmptyValidationFailed() throws Exception {
        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setDescription("");
        patch.setMovieId(UUID.randomUUID());
        patch.setPersonId(UUID.randomUUID());
        patch.setCrewTypeId(UUID.randomUUID());

        String resultJson = mvc.perform(patch("/api/v1/crew/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).patchCrew(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCrewDescriptionLimitValidationFailed() throws Exception {
        CrewPatchDTO patch = new CrewPatchDTO();
        patch.setDescription(StringUtils.repeat("*", 1001));
        patch.setMovieId(UUID.randomUUID());
        patch.setPersonId(UUID.randomUUID());
        patch.setCrewTypeId(UUID.randomUUID());

        String resultJson = mvc.perform(patch("/api/v1/crew/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(crewService, Mockito.never()).patchCrew(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testGetCrewsWithPagingAndSorting() throws Exception {
        CrewReadDTO read = createCrewRead();
        read.setDescription("XXX");
        read.setMovieId(UUID.randomUUID());
        read.setPersonId(UUID.randomUUID());
        read.setCrewTypeId(UUID.randomUUID());
        CrewFilter filter = new CrewFilter();
        filter.setDescription(read.getDescription());

        int page = 1;
        int size = 25;

        PageResult<CrewReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(crewService.getCrews(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/crew")
                .param("description", filter.getDescription())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<CrewReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<CrewReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }
}
