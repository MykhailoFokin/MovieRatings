package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.domain.NewsUserReviewStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.NewsUserReviewReviewNoteService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = NewsUserReviewReviewNoteController.class)
@ActiveProfiles("test")
public class NewsUserReviewReviewNoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsUserReviewReviewNoteService newsUserReviewReviewNoteService;

    @Test
    public void testGetNewsUserReviewReviewNote() throws Exception {
        NewsUserReviewReadDTO newsUserReview = createNewsUserReview();
        List<NewsUserReviewNoteReadDTO> newsUserReviewNoteReadDTO =
                List.of(createNewsUserReviewNoteRead(newsUserReview.getId()));

        Mockito.when(newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId()))
                .thenReturn(newsUserReviewNoteReadDTO);

        String resultJson = mvc.perform(get("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes"
                ,newsUserReview.getNewsId(), newsUserReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<NewsUserReviewNoteReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<NewsUserReviewNoteReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(newsUserReviewNoteReadDTO);

        Mockito.verify(newsUserReviewReviewNoteService).getNewsUserReviewUserReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId());
    }

    @Test
    public void testGetNewsUserReviewReviewNoteWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(NewsUserReviewNote.class,wrongId);
        Mockito.when(newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(wrongId, wrongId))
                .thenThrow(exception);

        String resultJson = mvc.perform(get(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes", wrongId, wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetNewsUserReviewReviewNoteWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes", wrongId, wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateNewsUserReviewReviewNote() throws Exception {

        NewsUserReviewReadDTO newsUserReview = createNewsUserReview();
        NewsUserReviewNoteCreateDTO create = new NewsUserReviewNoteCreateDTO();
        create.setStartIndex(10);
        create.setEndIndex(50);
        create.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        create.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        create.setNewsUserReviewId(newsUserReview.getId());

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(newsUserReview.getId());

        Mockito.when(newsUserReviewReviewNoteService.createNewsUserReviewReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                newsUserReview.getNewsId(), newsUserReview.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualMovieReviewNewsUserReviewNote = objectMapper
                .readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assertions.assertThat(actualMovieReviewNewsUserReviewNote).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewNewsUserReviewNote() throws Exception {

        NewsUserReviewReadDTO newsUserReview = createNewsUserReview();
        NewsUserReviewNotePatchDTO patchDTO = new NewsUserReviewNotePatchDTO();
        patchDTO.setStartIndex(10);
        patchDTO.setEndIndex(50);
        patchDTO.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        patchDTO.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        patchDTO.setNewsUserReviewId(newsUserReview.getId());

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(newsUserReview.getId());

        Mockito.when(newsUserReviewReviewNoteService.patchNewsUserReviewReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                newsUserReview.getNewsId(), newsUserReview.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualMovieReviewNewsUserReviewNote = objectMapper
                .readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewNewsUserReviewNote);
    }

    @Test
    public void testDeleteMovieReviewNewsUserReviewNote() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                id, id, id.toString())).andExpect(status().isOk());

        Mockito.verify(newsUserReviewReviewNoteService).deleteNewsUserReviewReviewNote(id, id, id);
    }

    @Test
    public void testPutMovieReviewNewsUserReviewNote() throws Exception {

        NewsUserReviewReadDTO newsUserReview = createNewsUserReview();
        NewsUserReviewNotePutDTO putDTO = new NewsUserReviewNotePutDTO();
        putDTO.setStartIndex(10);
        putDTO.setEndIndex(50);
        putDTO.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        putDTO.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        putDTO.setNewsUserReviewId(newsUserReview.getId());

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(newsUserReview.getId());

        Mockito.when(newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                newsUserReview.getNewsId(), newsUserReview.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualNewsUserReviewNote = objectMapper
                .readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);
    }

    private NewsUserReviewNoteReadDTO createNewsUserReviewNoteRead(UUID newsUserReviewId) {
        NewsUserReviewNoteReadDTO newsUserReviewNoteReadDTO = new NewsUserReviewNoteReadDTO();
        newsUserReviewNoteReadDTO.setId(UUID.randomUUID());
        newsUserReviewNoteReadDTO.setNewsUserReviewId(newsUserReviewId);
        newsUserReviewNoteReadDTO.setStartIndex(10);
        newsUserReviewNoteReadDTO.setEndIndex(50);
        newsUserReviewNoteReadDTO.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        newsUserReviewNoteReadDTO.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);

        return newsUserReviewNoteReadDTO;
    }

    private NewsUserReviewReadDTO createNewsUserReview() {
        NewsUserReviewReadDTO newsUserReviewReadDTO = new NewsUserReviewReadDTO();
        newsUserReviewReadDTO.setId(UUID.randomUUID());
        newsUserReviewReadDTO.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        newsUserReviewReadDTO.setPortalUserId(UUID.randomUUID());
        newsUserReviewReadDTO.setNewsId(UUID.randomUUID());
        newsUserReviewReadDTO.setModeratorId(UUID.randomUUID());
        return newsUserReviewReadDTO;
    }
}
