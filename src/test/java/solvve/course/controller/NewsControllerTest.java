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
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.NewsService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = NewsController.class)
@ActiveProfiles("test")
public class NewsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService newsService;

    private NewsReadDTO createNewsRead() {
        NewsReadDTO news = new NewsReadDTO();
        news.setId(UUID.randomUUID());
        news.setTopic("Main_News");
        news.setDescription("Our main news are absent today!");
        news.setPublished(Instant.now());
        return news;
    }

    @Test
    public void testGetNews() throws Exception {
        NewsReadDTO news = createNewsRead();

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

        NewsCreateDTO create = new NewsCreateDTO();
        create.setTopic("Main_News");
        create.setDescription("Our main news are absent today!");
        create.setPublished(Instant.now());

        NewsReadDTO read = createNewsRead();

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

        NewsPatchDTO patchDTO = new NewsPatchDTO();
        patchDTO.setTopic("Main_News");
        patchDTO.setDescription("Our main news are absent today!");
        patchDTO.setPublished(Instant.now());

        NewsReadDTO read = createNewsRead();

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

        NewsPutDTO putDTO = new NewsPutDTO();
        putDTO.setTopic("Main_News");
        putDTO.setDescription("Our main news are absent today!");
        putDTO.setPublished(Instant.now());

        NewsReadDTO read = createNewsRead();

        Mockito.when(newsService.updateNews(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/news/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assert.assertEquals(read, actualNews);
    }
}
