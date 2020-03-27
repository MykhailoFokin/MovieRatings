package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.ControllerValidationException;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.VisitService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VisitController.class)
public class VisitControllerTest extends BaseControllerTest {

    @MockBean
    private VisitService visitService;

    private VisitReadDTO createVisitRead() {
        VisitReadDTO visit = new VisitReadDTO();
        visit.setId(UUID.randomUUID());
        visit.setStartAt(Instant.now());
        visit.setFinishAt(Instant.now());
        visit.setStatus(VisitStatus.FINISHED);
        return visit;
    }

    @Test
    public void testGetVisit() throws Exception {
        VisitReadExtendedDTO visit = new VisitReadExtendedDTO();
        visit.setId(UUID.randomUUID());
        visit.setStartAt(Instant.now());
        visit.setFinishAt(Instant.now());
        visit.setStatus(VisitStatus.FINISHED);

        Mockito.when(visitService.getVisit(visit.getId())).thenReturn(visit);

        String resultJson = mvc.perform(get("/api/v1/visits/{id}", visit.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadDTO actualMovie = objectMapper.readValue(resultJson, VisitReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToIgnoringGivenFields(visit,"portalUserId");

        Mockito.verify(visitService).getVisit(visit.getId());
    }

    @Test
    public void testGetVisitWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Visit.class,wrongId);
        Mockito.when(visitService.getVisit(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/visits/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetVisitWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/visits/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateVisit() throws Exception {

        VisitCreateDTO create = new VisitCreateDTO();
        create.setStartAt(Instant.now());
        create.setFinishAt(Instant.now().plusSeconds(10));
        create.setStatus(VisitStatus.FINISHED);
        create.setPortalUserId(UUID.randomUUID());

        VisitReadDTO read = createVisitRead();
        read.setPortalUserId(create.getPortalUserId());

        Mockito.when(visitService.createVisit(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/visits")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadDTO actualVisit = objectMapper.readValue(resultJson, VisitReadDTO.class);
        Assertions.assertThat(actualVisit).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchVisit() throws Exception {

        VisitPatchDTO patchDTO = new VisitPatchDTO();
        patchDTO.setStartAt(Instant.now());
        patchDTO.setFinishAt(Instant.now().plusSeconds(10));
        patchDTO.setStatus(VisitStatus.FINISHED);

        VisitReadDTO read = createVisitRead();

        Mockito.when(visitService.patchVisit(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/visits/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadDTO actualVisit = objectMapper.readValue(resultJson, VisitReadDTO.class);
        Assert.assertEquals(read, actualVisit);
    }

    @Test
    public void testDeleteVisit() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/visits/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(visitService).deleteVisit(id);
    }

    @Test
    public void testPutVisit() throws Exception {

        VisitPutDTO putDTO = new VisitPutDTO();
        putDTO.setStartAt(Instant.now());
        putDTO.setFinishAt(Instant.now().plusSeconds(10));
        putDTO.setStatus(VisitStatus.FINISHED);
        putDTO.setPortalUserId(UUID.randomUUID());

        VisitReadDTO read = createVisitRead();
        read.setPortalUserId(putDTO.getPortalUserId());

        Mockito.when(visitService.updateVisit(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/visits/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadDTO actualVisit = objectMapper.readValue(resultJson, VisitReadDTO.class);
        Assert.assertEquals(read, actualVisit);
    }

    @Test
    public void testGetVisits() throws Exception {
        VisitFilter visitFilter = new VisitFilter();
        visitFilter.setPortalUserId(UUID.randomUUID());
        visitFilter.setStatuses(Set.of(VisitStatus.SCHEDULED, VisitStatus.FINISHED));
        visitFilter.setStartAtFrom(Instant.now());
        visitFilter.setStartAtTo(Instant.now());

        VisitReadDTO read = new VisitReadDTO();
        read.setPortalUserId(visitFilter.getPortalUserId());
        read.setStatus(VisitStatus.SCHEDULED);
        read.setId(UUID.randomUUID());
        read.setStartAt(Instant.now());
        read.setFinishAt(Instant.now());
        PageResult<VisitReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(visitService.getVisits(visitFilter, PageRequest.of(0, defaultPageSize))).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/visits")
                .param("portalUserId", visitFilter.getPortalUserId().toString())
                .param("statuses", "SCHEDULED, FINISHED")
                .param("startAtFrom", visitFilter.getStartAtFrom().toString())
                .param("startAtTo", visitFilter.getStartAtTo().toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<VisitReadDTO> actualResult = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<VisitReadDTO>>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateVisitWrongDates() throws Exception {
        VisitCreateDTO visitCreate = new VisitCreateDTO();
        visitCreate.setPortalUserId(UUID.randomUUID());
        visitCreate.setStartAt(LocalDateTime.of(2020, 1,20, 0, 0).toInstant(ZoneOffset.UTC));
        visitCreate.setFinishAt(visitCreate.getStartAt().minus(30, ChronoUnit.MINUTES));

        String resultJson = mvc.perform(post("/api/v1/visits")
                .content(objectMapper.writeValueAsString(visitCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startAt"));
        Assert.assertTrue(errorInfo.getMessage().contains("finishAt"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(visitService, Mockito.never()).createVisit(ArgumentMatchers.any());
    }

    @Test
    public void testPatchVisitWrongDates() throws Exception {
        VisitPatchDTO visitPatch = new VisitPatchDTO();
        visitPatch.setPortalUserId(UUID.randomUUID());
        visitPatch.setStartAt(LocalDateTime.of(2020, 1,20, 0, 0).toInstant(ZoneOffset.UTC));
        visitPatch.setFinishAt(visitPatch.getStartAt().minus(30, ChronoUnit.MINUTES));

        String resultJson = mvc.perform(patch("/api/v1/visits/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(visitPatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startAt"));
        Assert.assertTrue(errorInfo.getMessage().contains("finishAt"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(visitService, Mockito.never()).patchVisit(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutVisitWrongDates() throws Exception {
        VisitPutDTO visitPut = new VisitPutDTO();
        visitPut.setPortalUserId(UUID.randomUUID());
        visitPut.setStartAt(LocalDateTime.of(2020, 1,20, 0, 0).toInstant(ZoneOffset.UTC));
        visitPut.setFinishAt(visitPut.getStartAt().minus(30, ChronoUnit.MINUTES));

        String resultJson = mvc.perform(put("/api/v1/visits/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(visitPut))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startAt"));
        Assert.assertTrue(errorInfo.getMessage().contains("finishAt"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(visitService, Mockito.never()).updateVisit(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateVisitValidationFailed() throws Exception {
        VisitCreateDTO create = new VisitCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/visits")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(visitService, Mockito.never()).createVisit(ArgumentMatchers.any());
    }

    @Test
    public void testPutVisitValidationFailed() throws Exception {
        VisitPutDTO put = new VisitPutDTO();

        String resultJson = mvc.perform(put("/api/v1/visits/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(visitService, Mockito.never()).updateVisit(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testGetVisitsWithPagingAndSorting() throws Exception {
        VisitReadDTO read = createVisitRead();
        read.setPortalUserId(UUID.randomUUID());
        VisitFilter visitFilter = new VisitFilter();
        visitFilter.setPortalUserId(read.getPortalUserId());

        int page = 1;
        int size = 25;

        PageResult<VisitReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startAt"));
        Mockito.when(visitService.getVisits(visitFilter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/visits")
                .param("portalUserId", visitFilter.getPortalUserId().toString())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "startAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<VisitReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<VisitReadDTO>>() {
        });
        Assert.assertEquals(resultPage, actualPage);
    }
}
