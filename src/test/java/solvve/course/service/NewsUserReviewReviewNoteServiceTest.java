package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsUserReviewNoteRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from news_user_review_note",
        "delete from news_user_review",
        "delete from news",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NewsUserReviewReviewNoteServiceTest {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private NewsUserReviewReviewNoteService newsUserReviewReviewNoteService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        List<NewsUserReviewNoteReadDTO> readDTO =
                newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(news.getId(), newsUserReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(newsUserReviewNote.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsUserReviewNoteWrongId() {
        newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testCreateNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);

        NewsUserReviewNoteCreateDTO create = new NewsUserReviewNoteCreateDTO();
        create.setNewsUserReviewId(newsUserReview.getId());
        create.setStartIndex(10);
        create.setEndIndex(50);
        create.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        create.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.IN_REVIEW);
        create.setModeratorId(portalUser.getId());
        create.setNewsId(news.getId());
        create.setSourceText("Ich reise viel");

        NewsUserReviewNoteReadDTO read =
                newsUserReviewReviewNoteService.createNewsUserReviewReviewNote(news.getId(), newsUserReview.getId()
                        , create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        NewsUserReviewNote newsUserReviewNote = newsUserReviewNoteRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(newsUserReviewNote, "moderatorId",
                "newsUserReviewId", "newsId");
        Assertions.assertThat(read.getModeratorId()).isEqualTo(newsUserReviewNote.getModerator().getId());
        Assertions.assertThat(read.getNewsUserReviewId()).isEqualTo(newsUserReviewNote.getNewsUserReview().getId());
        Assertions.assertThat(read.getNewsId()).isEqualTo(newsUserReviewNote.getNews().getId());
    }

    @Test
    public void testPatchNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setNewsUserReviewId(newsUserReview.getId());
        patch.setStartIndex(10);
        patch.setEndIndex(50);
        patch.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        patch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.IN_REVIEW);
        patch.setModeratorId(portalUser.getId());
        patch.setSourceText("Ich reise viel");
        patch.setNewsId(news.getId());
        patch.setApprovedText("ApprText");
        NewsUserReviewNoteReadDTO read =
                newsUserReviewReviewNoteService.patchNewsUserReviewReviewNote(news.getId(), newsUserReview.getId(),
                        newsUserReviewNote.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        newsUserReviewNote = newsUserReviewNoteRepository.findById(read.getId()).get();
        Assertions.assertThat(newsUserReviewNote).isEqualToIgnoringGivenFields(read, "moderator",
                "newsUserReview", "news");
        Assertions.assertThat(newsUserReviewNote.getModerator().getId()).isEqualTo(read.getModeratorId());
        Assertions.assertThat(newsUserReviewNote.getNewsUserReview().getId()).isEqualTo(read.getNewsUserReviewId());
        Assertions.assertThat(newsUserReviewNote.getNews().getId()).isEqualTo(read.getNewsId());
    }

    @Test
    public void testPatchNewsUserReviewNoteEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        NewsUserReviewNoteReadDTO read =
                newsUserReviewReviewNoteService.patchNewsUserReviewReviewNote(news.getId(), newsUserReview.getId(),
                        newsUserReviewNote.getId(), patch);

        Assert.assertNotNull(read.getNewsUserReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());
        Assert.assertNotNull(read.getProposedText());
        Assert.assertNotNull(read.getModeratorTypoReviewStatusType());
        Assert.assertNotNull(read.getModeratorId());
        Assert.assertNotNull(read.getNewsId());
        Assert.assertNotNull(read.getApprovedText());
        Assert.assertNotNull(read.getSourceText());

        NewsUserReviewNote newsUserReviewNoteAfterUpdate = newsUserReviewNoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReview());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getStartIndex());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getEndIndex());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getProposedText());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getModeratorTypoReviewStatusType());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getModerator());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNews());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getApprovedText());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getSourceText());

        Assertions.assertThat(newsUserReviewNote).isEqualToIgnoringGivenFields(newsUserReviewNoteAfterUpdate,
                "moderator", "newsUserReview", "news");
        Assertions.assertThat(newsUserReviewNote.getModerator().getId())
                .isEqualTo(newsUserReviewNoteAfterUpdate.getModerator().getId());
        Assertions.assertThat(newsUserReviewNote.getNewsUserReview().getId())
                .isEqualTo(newsUserReviewNoteAfterUpdate.getNewsUserReview().getId());
        Assertions.assertThat(newsUserReviewNote.getNews().getId())
                .isEqualTo(newsUserReviewNoteAfterUpdate.getNews().getId());
    }

    @Test
    public void testDeleteNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        newsUserReviewReviewNoteService.deleteNewsUserReviewReviewNote(news.getId(), newsUserReview.getId(),
                newsUserReviewNote.getId());
        Assert.assertFalse(newsUserReviewNoteRepository.existsById(newsUserReviewNote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNewsUserReviewNoteNotFound() {
        newsUserReviewReviewNoteService.deleteNewsUserReviewReviewNote(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID());
    }

    @Test
    public void testPutNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();
        put.setNewsUserReviewId(newsUserReview.getId());
        put.setStartIndex(10);
        put.setEndIndex(50);
        put.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.IN_REVIEW);
        put.setModeratorId(portalUser.getId());
        put.setSourceText("Ich reise viel");
        put.setNewsId(news.getId());
        put.setApprovedText("ApprText");
        NewsUserReviewNoteReadDTO read =
                newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(news.getId(), newsUserReview.getId(),
                        newsUserReviewNote.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        newsUserReviewNote = newsUserReviewNoteRepository.findById(read.getId()).get();
        Assertions.assertThat(newsUserReviewNote).isEqualToIgnoringGivenFields(read, "moderator",
                "newsUserReview", "news");
        Assertions.assertThat(newsUserReviewNote.getModerator().getId()).isEqualTo(read.getModeratorId());
        Assertions.assertThat(newsUserReviewNote.getNewsUserReview().getId()).isEqualTo(read.getNewsUserReviewId());
        Assertions.assertThat(newsUserReviewNote.getNews().getId()).isEqualTo(read.getNewsId());
    }

    @Test
    public void testPutNewsUserReviewNoteEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                ModeratorTypoReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();
        NewsUserReviewNoteReadDTO read =
                newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(news.getId(), newsUserReview.getId(),
                        newsUserReviewNote.getId(), put);

        Assert.assertNull(read.getModeratorId());
        Assert.assertNull(read.getProposedText());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());
        Assert.assertNotNull(read.getNewsUserReviewId());
        Assert.assertNotNull(read.getModeratorTypoReviewStatusType());

        testObjectsFactory.inTransaction(() -> {

            NewsUserReviewNote newsUserReviewNoteAfterUpdate = newsUserReviewNoteRepository.findById(read.getId()).get();

            Assert.assertNull(newsUserReviewNoteAfterUpdate.getModerator());
            Assert.assertNull(newsUserReviewNoteAfterUpdate.getProposedText());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReview().getId());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getStartIndex());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getEndIndex());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getModeratorTypoReviewStatusType());

            Assertions.assertThat(newsUserReviewNote).isEqualToIgnoringGivenFields(newsUserReviewNoteAfterUpdate,
                    "moderator", "updatedAt", "newsUserReview", "proposedText", "startIndex", "endIndex", "news");
        });
    }
}
