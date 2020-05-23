package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.News;
import solvve.course.domain.NewsFeedback;
import solvve.course.domain.PortalUser;
import solvve.course.dto.NewsFeedbackCreateDTO;
import solvve.course.dto.NewsFeedbackPatchDTO;
import solvve.course.dto.NewsFeedbackPutDTO;
import solvve.course.dto.NewsFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewFeedbackRepository;
import solvve.course.repository.NewsFeedbackRepository;

import java.util.UUID;

public class NewsFeedbackServiceTest extends BaseTest {

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Autowired
    private NewsFeedbackService newsFeedbackService;

    @Test
    public void testGetMovieReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        NewsFeedbackReadDTO readDTO =
                newsFeedbackService.getNewsFeedback(newsFeedback.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(newsFeedback,
                "portalUserId", "newsId");
        Assertions.assertThat(readDTO.getNewsId()).isEqualTo(newsFeedback.getNews().getId());
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(newsFeedback.getPortalUser().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieReviewFeedbackWrongId() {
        newsFeedbackService.getNewsFeedback(UUID.randomUUID());
    }

    @Test
    public void testCreateMovieReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsFeedbackCreateDTO create = testObjectsFactory.createNewsFeedbackCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setNewsId(news.getId());

        NewsFeedbackReadDTO read = newsFeedbackService.createNewsFeedback(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        NewsFeedback movieReviewFeedback = newsFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(movieReviewFeedback,
                "portalUserId", "newsId");
        Assertions.assertThat(read.getNewsId()).isEqualTo(movieReviewFeedback.getNews().getId());
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(movieReviewFeedback.getPortalUser().getId());
    }

    @Test
    public void testPatchMovieReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        NewsFeedbackPatchDTO patch = new NewsFeedbackPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setNewsId(news.getId());
        patch.setIsLiked(false);
        NewsFeedbackReadDTO read =
                newsFeedbackService.patchNewsFeedback(newsFeedback.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        newsFeedback = newsFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(newsFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "news");
        Assertions.assertThat(newsFeedback.getNews().getId()).isEqualTo(read.getNewsId());
        Assertions.assertThat(newsFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
    }

    @Test
    public void testPatchNewsFeedbackEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        NewsFeedbackPatchDTO patch = new NewsFeedbackPatchDTO();
        NewsFeedbackReadDTO read =
                newsFeedbackService.patchNewsFeedback(newsFeedback.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getNewsId());
        Assert.assertNotNull(read.getIsLiked());

        NewsFeedback movieReviewFeedbackAfterUpdate =
                newsFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getNews());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getIsLiked());

        Assertions.assertThat(newsFeedback).isEqualToIgnoringGivenFields(movieReviewFeedbackAfterUpdate,
                "portalUser", "news");
        Assertions.assertThat(newsFeedback.getNews().getId())
                .isEqualTo(movieReviewFeedbackAfterUpdate.getNews().getId());
        Assertions.assertThat(newsFeedback.getPortalUser().getId())
                .isEqualTo(movieReviewFeedbackAfterUpdate.getPortalUser().getId());
    }

    @Test
    public void testDeleteMovieReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        newsFeedbackService.deleteNewsFeedback(newsFeedback.getId());
        Assert.assertFalse(newsFeedbackRepository.existsById(newsFeedback.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMovieReviewFeedbackNotFound() {
        newsFeedbackService.deleteNewsFeedback(UUID.randomUUID());
    }

    @Test
    public void testPutMovieReviewFeedback() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        NewsFeedbackPutDTO put = new NewsFeedbackPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setNewsId(news.getId());
        put.setIsLiked(false);
        NewsFeedbackReadDTO read =
                newsFeedbackService.updateNewsFeedback(newsFeedback.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        newsFeedback = newsFeedbackRepository.findById(read.getId()).get();
        Assertions.assertThat(newsFeedback).isEqualToIgnoringGivenFields(read,
                "portalUser", "news");
        Assertions.assertThat(newsFeedback.getNews().getId()).isEqualTo(read.getNewsId());
        Assertions.assertThat(newsFeedback.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
    }

    @Test
    public void testPutMovieReviewFeedbackEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsFeedback newsFeedback =
                testObjectsFactory.createNewsFeedback(portalUser, news);

        NewsFeedbackPutDTO put = new NewsFeedbackPutDTO();
        NewsFeedbackReadDTO read =
                newsFeedbackService.updateNewsFeedback(newsFeedback.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getNewsId());
        Assert.assertNull(read.getIsLiked());

        NewsFeedback movieReviewFeedbackAfterUpdate =
                newsFeedbackRepository.findById(read.getId()).get();

        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getPortalUser());
        Assert.assertNotNull(movieReviewFeedbackAfterUpdate.getNews());
        Assert.assertNull(movieReviewFeedbackAfterUpdate.getIsLiked());
    }
}
