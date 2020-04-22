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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.domain.ModeratorTypoReviewStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.NewsUserReviewReviewNoteService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = NewsUserReviewReviewNoteController.class)
public class NewsUserReviewReviewNoteControllerTest extends BaseControllerTest {

    @MockBean
    private NewsUserReviewReviewNoteService newsUserReviewReviewNoteService;

    @Test
    public void testGetNewsUserReviewReviewNote() throws Exception {
        NewsUserReviewReadDTO newsUserReview = generateObject(NewsUserReviewReadDTO.class);
        List<NewsUserReviewNoteReadDTO> newsUserReviewNoteReadDTO =
                List.of(createNewsUserReviewNoteRead(newsUserReview.getId()));

        Mockito.when(newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId()))
                .thenReturn(newsUserReviewNoteReadDTO);

        String resultJson =
                mvc.perform(get("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes"
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

        NewsUserReviewReadDTO newsUserReview = generateObject(NewsUserReviewReadDTO.class);
        NewsUserReviewNoteCreateDTO create = generateObject(NewsUserReviewNoteCreateDTO.class);
        create.setNewsUserReviewId(newsUserReview.getId());

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(newsUserReview.getId());
        read.setNewsId(create.getNewsId());
        read.setSourceText(create.getSourceText());

        Mockito.when(newsUserReviewReviewNoteService.createNewsUserReviewReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId(),
                create)).thenReturn(read);

        String resultJson =
                mvc.perform(post("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
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

        NewsUserReviewReadDTO newsUserReview = generateObject(NewsUserReviewReadDTO.class);
        NewsUserReviewNotePatchDTO patchDTO = generateObject(NewsUserReviewNotePatchDTO.class);
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

        NewsUserReviewReadDTO newsUserReview = generateObject(NewsUserReviewReadDTO.class);
        NewsUserReviewNotePutDTO putDTO = generateObject(NewsUserReviewNotePutDTO.class);
        putDTO.setNewsUserReviewId(newsUserReview.getId());

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(newsUserReview.getId());
        read.setNewsId(putDTO.getNewsId());
        read.setSourceText(putDTO.getSourceText());

        Mockito.when(newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(newsUserReview.getNewsId(),
                newsUserReview.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson =
                mvc.perform(put("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                newsUserReview.getNewsId(), newsUserReview.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualNewsUserReviewNote = objectMapper
                .readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);
    }

    @Test
    public void testCreateNewsUserReviewNoteValidationFailed() throws Exception {
        NewsUserReviewNoteCreateDTO create = new NewsUserReviewNoteCreateDTO();

        String resultJson =
                mvc.perform(post("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                        UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).createNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutNewsUserReviewNoteValidationFailed() throws Exception {
        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();

        String resultJson =
                mvc.perform(put("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(put))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).updateNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutNewsUserReviewNoteCheckLimitBorders() throws Exception {

        NewsUserReviewNotePutDTO putDTO = generateObject(NewsUserReviewNotePutDTO.class);
        putDTO.setProposedText("I");
        putDTO.setSourceText("I");
        putDTO.setApprovedText("I");

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(putDTO.getNewsUserReviewId());

        Mockito.when(newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(putDTO.getNewsId(),
                putDTO.getNewsUserReviewId(), read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                putDTO.getNewsId().toString(), putDTO.getNewsUserReviewId().toString(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualNewsUserReviewNote =
                objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);

        // Check upper border
        putDTO.setProposedText(StringUtils.repeat("*", 1000));
        putDTO.setSourceText(StringUtils.repeat("*", 1000));
        putDTO.setApprovedText(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                putDTO.getNewsId(), putDTO.getNewsUserReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNewsUserReviewNote = objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsUserReviewNotePutDTO put = generateObject(NewsUserReviewNotePutDTO.class);
        put.setProposedText("");
        put.setSourceText("");
        put.setApprovedText("");

        String resultJson = mvc.perform(put(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).updateNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsUserReviewNotePutDTO put = generateObject(NewsUserReviewNotePutDTO.class);
        put.setProposedText(StringUtils.repeat("*", 1001));
        put.setSourceText(StringUtils.repeat("*", 1001));
        put.setApprovedText(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).updateNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsUserReviewNoteCreateDTO create = generateObject(NewsUserReviewNoteCreateDTO.class);
        create.setProposedText("");
        create.setSourceText("");
        create.setApprovedText("");

        String resultJson = mvc.perform(post(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).createNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsUserReviewNoteCreateDTO create = generateObject(NewsUserReviewNoteCreateDTO.class);
        create.setProposedText(StringUtils.repeat("*", 1001));
        create.setSourceText(StringUtils.repeat("*", 1001));
        create.setApprovedText(StringUtils.repeat("*", 1001));


        String resultJson = mvc.perform(post(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).createNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateNewsUserReviewNoteCheckStingBorders() throws Exception {

        NewsUserReviewNoteCreateDTO create = generateObject(NewsUserReviewNoteCreateDTO.class);
        create.setProposedText(StringUtils.repeat("*", 1));
        create.setSourceText(StringUtils.repeat("*", 1));
        create.setApprovedText(StringUtils.repeat("*", 1));

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(create.getNewsUserReviewId());

        Mockito.when(newsUserReviewReviewNoteService.createNewsUserReviewReviewNote(create.getNewsId(),
                create.getNewsUserReviewId(), create)).thenReturn(read);

        String resultJson = mvc.perform(post(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                create.getNewsId(), create.getNewsUserReviewId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualNewsUserReviewNote =
                objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assertions.assertThat(actualNewsUserReviewNote).isEqualToComparingFieldByField(read);

        create.setProposedText(StringUtils.repeat("*", 1000));
        create.setSourceText(StringUtils.repeat("*", 1000));
        create.setApprovedText(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes",
                create.getNewsId(), create.getNewsUserReviewId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNewsUserReviewNote = objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assertions.assertThat(actualNewsUserReviewNote).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchNewsUserReviewNoteCheckStringBorders() throws Exception {

        NewsUserReviewNotePatchDTO patchDTO = generateObject(NewsUserReviewNotePatchDTO.class);
        patchDTO.setProposedText(StringUtils.repeat("*", 1));
        patchDTO.setSourceText(StringUtils.repeat("*", 1));
        patchDTO.setApprovedText(StringUtils.repeat("*", 1));

        NewsUserReviewNoteReadDTO read = createNewsUserReviewNoteRead(patchDTO.getNewsUserReviewId());

        Mockito.when(newsUserReviewReviewNoteService.patchNewsUserReviewReviewNote(patchDTO.getNewsId(),
                patchDTO.getNewsUserReviewId(), read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                patchDTO.getNewsId(), patchDTO.getNewsUserReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        NewsUserReviewNoteReadDTO actualNewsUserReviewNote =
                objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);

        patchDTO.setProposedText(StringUtils.repeat("*", 1000));
        patchDTO.setSourceText(StringUtils.repeat("*", 1000));
        patchDTO.setApprovedText(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                patchDTO.getNewsId(), patchDTO.getNewsUserReviewId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualNewsUserReviewNote = objectMapper.readValue(resultJson, NewsUserReviewNoteReadDTO.class);
        Assert.assertEquals(read, actualNewsUserReviewNote);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        NewsUserReviewNotePatchDTO patch = generateObject(NewsUserReviewNotePatchDTO.class);
        patch.setProposedText("");
        patch.setSourceText("");
        patch.setApprovedText("");

        String resultJson = mvc.perform(patch(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).patchNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        NewsUserReviewNotePatchDTO patch = generateObject(NewsUserReviewNotePatchDTO.class);
        patch.setProposedText(StringUtils.repeat("*", 1001));
        patch.setSourceText(StringUtils.repeat("*", 1001));
        patch.setApprovedText(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch(
                "/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes/{id}",
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(newsUserReviewReviewNoteService,
                Mockito.never()).patchNewsUserReviewReviewNote(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    private NewsUserReviewNoteReadDTO createNewsUserReviewNoteRead(UUID newsUserReviewId) {
        NewsUserReviewNoteReadDTO newsUserReviewNoteReadDTO = generateObject(NewsUserReviewNoteReadDTO.class);
        newsUserReviewNoteReadDTO.setNewsUserReviewId(newsUserReviewId);
        return newsUserReviewNoteReadDTO;
    }
}
