package solvve.course.controller;

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
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.VisitService;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VisitController.class)
@ActiveProfiles("test")
public class VisitControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VisitService visitService;

    private VisitReadExtendedDTO createVisitRead() {
        VisitReadExtendedDTO visit = new VisitReadExtendedDTO();
        visit.setId(UUID.randomUUID());
        //visit.setUserId(portalUser);
        //visit.setMasterId(master);
        visit.setStartAt(Instant.now());
        visit.setFinishAt(Instant.now());
        visit.setStatus(VisitStatus.FINISHED);
        return visit;
    }

    @Test
    public void testGetVisit() throws Exception {
        VisitReadExtendedDTO visit = createVisitRead();

        Mockito.when(visitService.getVisit(visit.getId())).thenReturn(visit);

        String resultJson = mvc.perform(get("/api/v1/visits/{id}", visit.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadDTO actualMovie = objectMapper.readValue(resultJson, VisitReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(visit);

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
        //create.setUserId(portalUser);
        //create.setMasterId(master);
        create.setStartAt(Instant.now());
        create.setFinishAt(Instant.now());
        create.setStatus(VisitStatus.FINISHED);

        VisitReadExtendedDTO read = createVisitRead();

        Mockito.when(visitService.createVisit(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/visits")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadExtendedDTO actualVisit = objectMapper.readValue(resultJson, VisitReadExtendedDTO.class);
        Assertions.assertThat(actualVisit).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchVisit() throws Exception {

        VisitPatchDTO patchDTO = new VisitPatchDTO();
        //patchDTO.setUserId(portalUser);
        //patchDTO.setMasterId(master);
        patchDTO.setStartAt(Instant.now());
        patchDTO.setFinishAt(Instant.now());
        patchDTO.setStatus(VisitStatus.FINISHED);

        VisitReadExtendedDTO read = createVisitRead();

        Mockito.when(visitService.patchVisit(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/visits/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadExtendedDTO actualVisit = objectMapper.readValue(resultJson, VisitReadExtendedDTO.class);
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
        //putDTO.setUserId(portalUser);
        //putDTO.setMasterId(master);
        putDTO.setStartAt(Instant.now());
        putDTO.setFinishAt(Instant.now());
        putDTO.setStatus(VisitStatus.FINISHED);

        VisitReadExtendedDTO read = createVisitRead();

        Mockito.when(visitService.putVisit(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/visits/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VisitReadExtendedDTO actualVisit = objectMapper.readValue(resultJson, VisitReadExtendedDTO.class);
        Assert.assertEquals(read, actualVisit);
    }
}
