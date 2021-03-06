package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.NewsUserReviewCreateDTO;
import solvve.course.dto.NewsUserReviewPatchDTO;
import solvve.course.dto.NewsUserReviewPutDTO;
import solvve.course.dto.NewsUserReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsUserReviewRepository;

import java.util.UUID;

public class NewsUserReviewServiceTest extends BaseTest {

    @Autowired
    private NewsUserReviewRepository newsUserReviewRepository;

    @Autowired
    private NewsUserReviewService newsUserReviewService;

    @Test
    public void testGetNewsUserReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewReadDTO readDTO = newsUserReviewService.getNewsUserReview(newsUserReview.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(newsUserReview, "portalUserId", "moderatorId",
                "newsId");
        Assertions.assertThat(readDTO.getPortalUserId()).isEqualTo(newsUserReview.getPortalUser().getId());
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(newsUserReview.getModerator().getId());
        Assertions.assertThat(readDTO.getNewsId()).isEqualTo(newsUserReview.getNews().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsUserReviewWrongId() {
        newsUserReviewService.getNewsUserReview(UUID.randomUUID());
    }

    @Test
    public void testCreateNewsUserReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsUserReviewCreateDTO create = testObjectsFactory.createNewsUserReviewCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setNewsId(news.getId());
        create.setModeratorId(portalUser.getId());
        NewsUserReviewReadDTO read = newsUserReviewService.createNewsUserReview(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        NewsUserReview newsUserReview = newsUserReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(newsUserReview, "portalUserId", "moderatorId",
                "newsId");
        Assertions.assertThat(read.getPortalUserId()).isEqualTo(newsUserReview.getPortalUser().getId());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(newsUserReview.getModerator().getId());
        Assertions.assertThat(read.getNewsId()).isEqualTo(newsUserReview.getNews().getId());
    }

    @Test
    public void testPatchNewsUserReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewPatchDTO patch = testObjectsFactory.createNewsUserReviewPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setNewsId(news.getId());
        patch.setModeratorId(portalUser.getId());
        NewsUserReviewReadDTO read = newsUserReviewService.patchNewsUserReview(newsUserReview.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        newsUserReview = newsUserReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(newsUserReview).isEqualToIgnoringGivenFields(read, "portalUser", "moderator", "news",
                "newsUserReviewNotes");
        Assertions.assertThat(newsUserReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(newsUserReview.getModerator().getId()).isEqualTo(read.getModeratorId());
        Assertions.assertThat(newsUserReview.getNews().getId()).isEqualTo(read.getNewsId());
    }

    @Test
    public void testPatchNewsUserReviewEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewPatchDTO patch = new NewsUserReviewPatchDTO();
        NewsUserReviewReadDTO read = newsUserReviewService.patchNewsUserReview(newsUserReview.getId(), patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getNewsId());
        Assert.assertNotNull(read.getModeratorId());
        Assert.assertNotNull(read.getModeratorTypoReviewStatusType());

        NewsUserReview newsUserReviewAfterUpdate = newsUserReviewRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsUserReviewAfterUpdate.getPortalUser());
        Assert.assertNotNull(newsUserReviewAfterUpdate.getNews());
        Assert.assertNotNull(newsUserReviewAfterUpdate.getModerator());
        Assert.assertNotNull(newsUserReviewAfterUpdate.getModeratorTypoReviewStatusType());

        Assertions.assertThat(newsUserReview).isEqualToIgnoringGivenFields(newsUserReviewAfterUpdate,
                "portalUser", "moderator", "newsUserReviewNotes", "news");
        Assertions.assertThat(newsUserReview.getPortalUser().getId())
                .isEqualTo(newsUserReviewAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(newsUserReview.getModerator().getId())
                .isEqualTo(newsUserReviewAfterUpdate.getModerator().getId());
        Assertions.assertThat(newsUserReview.getNews().getId()).isEqualTo(newsUserReviewAfterUpdate.getNews().getId());
    }

    @Test
    public void testDeleteNewsUserReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        newsUserReviewService.deleteNewsUserReview(newsUserReview.getId());
        Assert.assertFalse(newsUserReviewRepository.existsById(newsUserReview.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNewsUserReviewNotFound() {
        newsUserReviewService.deleteNewsUserReview(UUID.randomUUID());
    }

    @Test
    public void testPutNewsUserReview() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewPutDTO put = testObjectsFactory.createNewsUserReviewPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setNewsId(news.getId());
        put.setModeratorId(portalUser.getId());
        NewsUserReviewReadDTO read = newsUserReviewService.updateNewsUserReview(newsUserReview.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        newsUserReview = newsUserReviewRepository.findById(read.getId()).get();
        Assertions.assertThat(newsUserReview).isEqualToIgnoringGivenFields(read, "portalUser", "moderator", "news",
                "newsUserReviewNotes");
        Assertions.assertThat(newsUserReview.getPortalUser().getId()).isEqualTo(read.getPortalUserId());
        Assertions.assertThat(newsUserReview.getModerator().getId()).isEqualTo(read.getModeratorId());
        Assertions.assertThat(newsUserReview.getNews().getId()).isEqualTo(read.getNewsId());
    }

    @Test
    public void testPutNewsUserReviewEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewPutDTO put = new NewsUserReviewPutDTO();
        NewsUserReviewReadDTO read = newsUserReviewService.updateNewsUserReview(newsUserReview.getId(), put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getNewsId());
        Assert.assertNull(read.getModeratorId());
        Assert.assertNotNull(read.getModeratorTypoReviewStatusType());

        testObjectsFactory.inTransaction(() -> {

            NewsUserReview newsUserReviewAfterUpdate = newsUserReviewRepository.findById(read.getId()).get();

            Assert.assertNotNull(newsUserReviewAfterUpdate.getPortalUser().getId());
            Assert.assertNotNull(newsUserReviewAfterUpdate.getNews());
            Assert.assertNotNull(newsUserReviewAfterUpdate.getModeratorTypoReviewStatusType());
            Assert.assertNull(newsUserReviewAfterUpdate.getModerator());

            Assertions.assertThat(newsUserReview).isEqualToIgnoringGivenFields(newsUserReviewAfterUpdate,
                    "moderator", "updatedAt", "news", "portalUser", "newsUserReviewNotes");
            Assertions.assertThat(newsUserReview.getPortalUser().getId())
                    .isEqualTo(newsUserReviewAfterUpdate.getPortalUser().getId());
            Assertions.assertThat(newsUserReview.getNews().getId())
                    .isEqualTo(newsUserReviewAfterUpdate.getNews().getId());
        });
    }
}
