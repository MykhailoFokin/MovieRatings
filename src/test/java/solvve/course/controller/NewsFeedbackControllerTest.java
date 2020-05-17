package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.NewsFeedback;
import solvve.course.dto.NewsFeedbackCreateDTO;
import solvve.course.dto.NewsFeedbackPatchDTO;
import solvve.course.dto.NewsFeedbackPutDTO;
import solvve.course.dto.NewsFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.NewsFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = NewsFeedbackController.class)
public class NewsFeedbackControllerTest extends BaseControllerTest {

    @MockBean
    private NewsFeedbackService newsFeedbackService;

    @Test
    public void testGetNewsFeedback() throws Exception {
        NewsFeedbackReadDTO newsFeedback = generateObject(NewsFeedbackReadDTO.class);

        Mockito.when(newsFeedbackService.getNewsFeedback(newsFeedback.getId()))
                .thenReturn(newsFeedback);

        String resultJson = mvc.perform(get("/api/v1/news-feedbacks/{id}",
                newsFeedback.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        NewsFeedbackReadDTO actualNews = objectMapper.readValue(resultJson, NewsFeedbackReadDTO.class);
        Assertions.assertThat(actualNews).isEqualToComparingFieldByField(newsFeedback);

        Mockito.verify(newsFeedbackService).getNewsFeedback(newsFeedback.getId());
    }

    @Test
    public void testGetNewsFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(NewsFeedback.class,wrongId);
        Mockito.when(newsFeedbackService.getNewsFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/news-feedbacks/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetNewsFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/news-feedbacks/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateNewsFeedback() throws Exception {

        NewsFeedbackCreateDTO create = generateObject(NewsFeedbackCreateDTO.class);

        NewsFeedbackReadDTO read = generateObject(NewsFeedbackReadDTO.class);

        Mockito.when(newsFeedbackService.createNewsFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news-feedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsFeedbackReadDTO actualNewsFeedback = objectMapper
                .readValue(resultJson, NewsFeedbackReadDTO.class);
        Assertions.assertThat(actualNewsFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchNewsFeedback() throws Exception {

        NewsFeedbackPatchDTO patchDTO = generateObject(NewsFeedbackPatchDTO.class);

        NewsFeedbackReadDTO read = generateObject(NewsFeedbackReadDTO.class);

        Mockito.when(newsFeedbackService.patchNewsFeedback(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/news-feedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsFeedbackReadDTO actualNewsFeedback = objectMapper
                .readValue(resultJson, NewsFeedbackReadDTO.class);
        Assert.assertEquals(read, actualNewsFeedback);
    }

    @Test
    public void testDeleteNewsFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/news-feedbacks/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(newsFeedbackService).deleteNewsFeedback(id);
    }

    @Test
    public void testPutNewsFeedback() throws Exception {

        NewsFeedbackPutDTO putDTO = generateObject(NewsFeedbackPutDTO.class);

        NewsFeedbackReadDTO read = generateObject(NewsFeedbackReadDTO.class);

        Mockito.when(newsFeedbackService.updateNewsFeedback(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/news-feedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsFeedbackReadDTO actualNewsFeedback = objectMapper
                .readValue(resultJson, NewsFeedbackReadDTO.class);
        Assert.assertEquals(read, actualNewsFeedback);
    }

    @Test
    public void testCreateNewsFeedbackValidationFailed() throws Exception {
        NewsFeedbackCreateDTO create = new NewsFeedbackCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/news-feedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsFeedbackService, Mockito.never()).createNewsFeedback(ArgumentMatchers.any());
    }

    @Test
    public void testPutNewsFeedbackValidationFailed() throws Exception {
        NewsFeedbackPutDTO put = new NewsFeedbackPutDTO();

        String resultJson = mvc.perform(put("/api/v1/news-feedbacks/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsFeedbackService, Mockito.never()).updateNewsFeedback(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
