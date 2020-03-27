package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.ReleaseDetailService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReleaseDetailController.class)
public class ReleaseDetailControllerTest extends BaseControllerTest {

    @MockBean
    private ReleaseDetailService releaseDetailService;

    private ReleaseDetailReadDTO createReleaseDetailRead() {
        ReleaseDetailReadDTO releaseDetail = new ReleaseDetailReadDTO();
        releaseDetail.setId(UUID.randomUUID());
        releaseDetail.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        return releaseDetail;
    }

    @Test
    public void testGetReleaseDetail() throws Exception {
        ReleaseDetailReadDTO releaseDetail = createReleaseDetailRead();

        Mockito.when(releaseDetailService.getReleaseDetails(releaseDetail.getId())).thenReturn(releaseDetail);

        String resultJson = mvc.perform(get("/api/v1/releasedetails/{id}", releaseDetail.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReleaseDetailReadDTO actualMovie = objectMapper.readValue(resultJson, ReleaseDetailReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(releaseDetail);

        Mockito.verify(releaseDetailService).getReleaseDetails(releaseDetail.getId());
    }

    @Test
    public void testGetReleaseDetailWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(ReleaseDetail.class,wrongId);
        Mockito.when(releaseDetailService.getReleaseDetails(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/releasedetails/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetReleaseDetailWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/releasedetails/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateReleaseDetail() throws Exception {

        ReleaseDetailCreateDTO create = new ReleaseDetailCreateDTO();
        create.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        create.setCountryId(UUID.randomUUID());
        create.setMovieId(UUID.randomUUID());

        ReleaseDetailReadDTO read = createReleaseDetailRead();
        read.setCountryId(create.getCountryId());
        read.setMovieId(create.getMovieId());

        Mockito.when(releaseDetailService.createReleaseDetails(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/releasedetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReleaseDetailReadDTO actualReleaseDetail = objectMapper.readValue(resultJson, ReleaseDetailReadDTO.class);
        Assertions.assertThat(actualReleaseDetail).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchReleaseDetail() throws Exception {

        ReleaseDetailPatchDTO patchDTO = new ReleaseDetailPatchDTO();
        patchDTO.setReleaseDate(LocalDate.now(ZoneOffset.UTC));

        ReleaseDetailReadDTO read = createReleaseDetailRead();

        Mockito.when(releaseDetailService.patchReleaseDetails(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/releasedetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReleaseDetailReadDTO actualReleaseDetail = objectMapper.readValue(resultJson, ReleaseDetailReadDTO.class);
        Assert.assertEquals(read, actualReleaseDetail);
    }

    @Test
    public void testDeleteReleaseDetail() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/releasedetails/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(releaseDetailService).deleteReleaseDetails(id);
    }

    @Test
    public void testPutReleaseDetail() throws Exception {

        ReleaseDetailPutDTO putDTO = new ReleaseDetailPutDTO();
        putDTO.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        putDTO.setMovieId(UUID.randomUUID());
        putDTO.setCountryId(UUID.randomUUID());

        ReleaseDetailReadDTO read = createReleaseDetailRead();
        read.setMovieId(putDTO.getMovieId());
        read.setCountryId(putDTO.getCountryId());

        Mockito.when(releaseDetailService.updateReleaseDetails(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/releasedetails/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ReleaseDetailReadDTO actualReleaseDetail = objectMapper.readValue(resultJson, ReleaseDetailReadDTO.class);
        Assert.assertEquals(read, actualReleaseDetail);
    }

    @Test
    public void testCreateReleaseDetailValidationFailed() throws Exception {
        ReleaseDetailCreateDTO create = new ReleaseDetailCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/releasedetails")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(releaseDetailService, Mockito.never()).createReleaseDetails(ArgumentMatchers.any());
    }

    @Test
    public void testPutReleaseDetailValidationFailed() throws Exception {
        ReleaseDetailPutDTO put = new ReleaseDetailPutDTO();

        String resultJson = mvc.perform(put("/api/v1/releasedetails/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(releaseDetailService, Mockito.never()).updateReleaseDetails(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
