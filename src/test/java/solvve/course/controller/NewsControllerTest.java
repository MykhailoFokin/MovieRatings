package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.NewsService;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = NewsController.class)
public class NewsControllerTest extends BaseControllerTest {

    @MockBean
    private NewsService newsService;

    @Test
    public void testGetNews() throws Exception {
        NewsReadDTO news = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.getNews(news.getId())).thenReturn(news);

        String resultJson = mvc.perform(get("/api/v1/news/{id}", news.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualMovie = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(news);

        Mockito.verify(newsService).getNews(news.getId());
    }

    @Test
    public void testGetNewsWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(News.class,wrongId);
        Mockito.when(newsService.getNews(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/news/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetNewsWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/news/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateNews() throws Exception {

        NewsCreateDTO create = generateObject(NewsCreateDTO.class);

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.createNews(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assertions.assertThat(actualNews).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchNews() throws Exception {

        NewsPatchDTO patchDTO = generateObject(NewsPatchDTO.class);

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.patchNews(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);
    }

    @Test
    public void testDeleteNews() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/news/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(newsService).deleteNews(id);
    }

    @Test
    public void testPutNews() throws Exception {

        NewsPutDTO putDTO = generateObject(NewsPutDTO.class);

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.updateNews(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);
    }

    @Test
    public void testCreateNewsValidationFailed() throws Exception {
        NewsCreateDTO create = new NewsCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).createNews(ArgumentMatchers.any());
    }

    @Test
    public void testPutNewsValidationFailed() throws Exception {
        NewsPutDTO put = new NewsPutDTO();

        String resultJson = mvc.perform(put("/api/v1/news/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).updateNews(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutNewsCheckLimitBorders() throws Exception {

        NewsPutDTO putDTO = generateObject(NewsPutDTO.class);
        putDTO.setDescription("D");

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.updateNews(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsPutDTO put = generateObject(NewsPutDTO.class);
        put.setDescription("");

        String resultJson = mvc.perform(put("/api/v1/news/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).updateNews(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsPutDTO put = generateObject(NewsPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/news/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).updateNews(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsCreateDTO create = generateObject(NewsCreateDTO.class);
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).createNews(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsCreateDTO create = generateObject(NewsCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).createNews(ArgumentMatchers.any());
    }

    @Test
    public void testCreateNewsCheckStingBorders() throws Exception {

        NewsCreateDTO create = generateObject(NewsCreateDTO.class);
        create.setDescription("D");

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.createNews(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assertions.assertThat(actualNews).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assertions.assertThat(actualNews).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchNewsCheckStringBorders() throws Exception {

        NewsPatchDTO patchDTO = generateObject(NewsPatchDTO.class);
        patchDTO.setDescription("D");

        NewsReadDTO read = generateObject(NewsReadDTO.class);

        Mockito.when(newsService.patchNews(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsPatchDTO patch = generateObject(NewsPatchDTO.class);
        patch.setDescription("");

        String resultJson = mvc.perform(patch("/api/v1/news/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).patchNews(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsPatchDTO patch = generateObject(NewsPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/news/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsService, Mockito.never()).patchNews(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
