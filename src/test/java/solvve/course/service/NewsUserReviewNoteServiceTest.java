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
import solvve.course.dto.NewsUserReviewNoteCreateDTO;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsUserReviewNoteRepository;
import solvve.course.utils.TestObjectsFactory;

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
public class NewsUserReviewNoteServiceTest {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private NewsUserReviewNoteService newsUserReviewNoteService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNoteReadDTO readDTO = newsUserReviewNoteService.getNewsUserReviewNote(newsUserReviewNote.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(newsUserReviewNote, "moderatorId",
                "newsUserReviewId", "newsId");
        Assertions.assertThat(readDTO.getModeratorId()).isEqualTo(newsUserReviewNote.getModerator().getId());
        Assertions.assertThat(readDTO.getNewsUserReviewId()).isEqualTo(newsUserReviewNote.getNewsUserReview().getId());
        Assertions.assertThat(readDTO.getNewsId()).isEqualTo(newsUserReviewNote.getNews().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsUserReviewNoteWrongId() {
        newsUserReviewNoteService.getNewsUserReviewNote(UUID.randomUUID());
    }

    @Test
    public void testCreateNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                NewsUserReviewStatusType.IN_REVIEW);

        NewsUserReviewNoteCreateDTO create = new NewsUserReviewNoteCreateDTO();
        create.setNewsUserReviewId(newsUserReview.getId());
        create.setStartIndex(10);
        create.setEndIndex(50);
        create.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        create.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        create.setModeratorId(portalUser.getId());
        create.setNewsId(news.getId());
        create.setSourceText("Ich reise viel");
        NewsUserReviewNoteReadDTO read = newsUserReviewNoteService.createNewsUserReviewNote(create);
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
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setNewsUserReviewId(newsUserReview.getId());
        patch.setStartIndex(10);
        patch.setEndIndex(50);
        patch.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        patch.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        patch.setModeratorId(portalUser.getId());
        patch.setApprovedText("ApprText");
        patch.setSourceText("Ich reise viel");
        patch.setNewsId(news.getId());
        NewsUserReviewNoteReadDTO read = newsUserReviewNoteService.patchNewsUserReviewNote(newsUserReviewNote.getId(), patch);

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
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        NewsUserReviewNoteReadDTO read = newsUserReviewNoteService.patchNewsUserReviewNote(newsUserReviewNote.getId(), patch);

        Assert.assertNotNull(read.getNewsUserReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());
        Assert.assertNotNull(read.getProposedText());
        Assert.assertNotNull(read.getNewsUserReviewStatusType());
        Assert.assertNotNull(read.getModeratorId());

        NewsUserReviewNote newsUserReviewNoteAfterUpdate = newsUserReviewNoteRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReview());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getStartIndex());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getEndIndex());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getProposedText());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReviewStatusType());
        Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getModerator());

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
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        newsUserReviewNoteService.deleteNewsUserReviewNote(newsUserReviewNote.getId());
        Assert.assertFalse(newsUserReviewNoteRepository.existsById(newsUserReviewNote.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNewsUserReviewNoteNotFound() {
        newsUserReviewNoteService.deleteNewsUserReviewNote(UUID.randomUUID());
    }

    @Test
    public void testPutNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();
        put.setNewsUserReviewId(newsUserReview.getId());
        put.setStartIndex(10);
        put.setEndIndex(50);
        put.setProposedText("Ich reise viel, ich reise gern. Fern und nah und nah und fern");
        put.setNewsUserReviewStatusType(NewsUserReviewStatusType.IN_REVIEW);
        put.setModeratorId(portalUser.getId());
        put.setApprovedText("ApprText");
        put.setSourceText("Ich reise viel");
        put.setNewsId(news.getId());
        NewsUserReviewNoteReadDTO read = newsUserReviewNoteService.updateNewsUserReviewNote(newsUserReviewNote.getId(), put);

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
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 10, 50, "Ich reise viel, ich reise gern. Fern und nah und nah und fern",
                NewsUserReviewStatusType.IN_REVIEW, news, "Ich reise viel");

        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();
        NewsUserReviewNoteReadDTO read = newsUserReviewNoteService.updateNewsUserReviewNote(newsUserReviewNote.getId(), put);

        Assert.assertNull(read.getModeratorId());
        Assert.assertNull(read.getProposedText());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());
        Assert.assertNotNull(read.getNewsUserReviewId());
        Assert.assertNotNull(read.getNewsUserReviewStatusType());

        testObjectsFactory.inTransaction(() -> {

            NewsUserReviewNote newsUserReviewNoteAfterUpdate = newsUserReviewNoteRepository.findById(read.getId()).get();

            Assert.assertNull(newsUserReviewNoteAfterUpdate.getModerator());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReview().getId());
            Assert.assertNull(newsUserReviewNoteAfterUpdate.getProposedText());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getStartIndex());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getEndIndex());
            Assert.assertNotNull(newsUserReviewNoteAfterUpdate.getNewsUserReviewStatusType());

            Assertions.assertThat(newsUserReviewNote).isEqualToIgnoringGivenFields(newsUserReviewNoteAfterUpdate,
                    "moderator", "updatedAt", "newsUserReview", "proposedText", "startIndex", "endIndex", "news");
        });
    }
}
