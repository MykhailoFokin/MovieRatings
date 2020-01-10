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
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.NewsService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    public void testGetNews() throws Exception {
        NewsReadDTO news = new NewsReadDTO();
        news.setId(UUID.randomUUID());
        //news.setUserId(portalUserReadDTO.getId());
        news.setTopic("Main_News");
        news.setDescription("Our main news are absent today!");
        news.setPublished(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        Mockito.when(newsService.getNews(news.getId())).thenReturn(news);

        String resultJson = mvc.perform(get("/api/v1/news/{id}", news.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
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

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/news/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateNews() throws Exception {

        NewsCreateDTO create = new NewsCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        create.setTopic("Main_News");
        create.setDescription("Our main news are absent today!");
        create.setPublished(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        NewsReadDTO read = new NewsReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        read.setTopic("Main_News");
        read.setDescription("Our main news are absent today!");
        read.setPublished(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        Mockito.when(newsService.createNews(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsReadDTO actualNews = objectMapper.readValue(resultJson, NewsReadDTO.class);
        Assertions.assertThat(actualNews).isEqualToComparingFieldByField(read);
    }
}
