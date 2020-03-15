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
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.exception.UnprocessableEntityException;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.NewsUserReviewNoteRepository;
import solvve.course.utils.TestObjectsFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
public class PortalUserNewsUserReviewNoteServiceTest {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private PortalUserNewsUserReviewNoteService portalUserNewsUserReviewNoteService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testGetNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 0, 14, "Ich reise viel",
                NewsUserReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        List<NewsUserReviewNoteReadDTO> readDTO =
                portalUserNewsUserReviewNoteService.getModeratorUserReviewNotes(portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(newsUserReviewNote.getId());
    }

    @Test
    public void testPatchPortalUserNewsUserReviewNote() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser1,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern");
        NewsUserReview newsUserReview1 = testObjectsFactory.createNewsUserReview(portalUser1, news, portalUser1,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote1 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview1, 0, 14, "Ich reise viel",
                NewsUserReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        NewsUserReview newsUserReview2 = testObjectsFactory.createNewsUserReview(portalUser2, news, portalUser1,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote2 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview2, 0, 14, "Ivh reide vie;",
                NewsUserReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        NewsUserReview newsUserReview3 = testObjectsFactory.createNewsUserReview(portalUser2, news, portalUser1,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote3 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview3, 0, 14, "Ivh reide vie;",
                NewsUserReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setApprovedText("Ich reise viel");
        patch.setStartIndex(0);
        patch.setEndIndex(14);
        patch.setNewsUserReviewStatusType(NewsUserReviewStatusType.FIXED);
        patch.setModeratorId(portalUser2.getId());

        StringBuilder sb = new StringBuilder(news.getDescription());
        sb.replace(newsUserReviewNote1.getStartIndex(), newsUserReviewNote1.getEndIndex(),
                newsUserReviewNote1.getProposedText());

        NewsUserReviewNoteReadDTO read =
                portalUserNewsUserReviewNoteService.patchPortalUserNewsUserReviewNote(portalUser1.getId(),
                        newsUserReviewNote1.getId(), patch);
        News newsAfterUpdate = newsRepository.findById(news.getId()).get();

        Assertions.assertThat(newsAfterUpdate.getDescription()).isEqualTo(sb.toString());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(portalUser2.getId());
        Assertions.assertThat(read.getNewsUserReviewStatusType()).isEqualTo(patch.getNewsUserReviewStatusType());

        Assert.assertFalse(getNewsUserReviewNotesWithSameIndexes(news.getId(), read.getNewsUserReviewStatusType()
                , List.of(newsUserReviewNote2.getId(), newsUserReviewNote3.getId())));
    }

    @Test
    public void testPatchPortalUserNewsUserReviewNoteInFixedStatus() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern");
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                NewsUserReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 0, 14, "Ivh reide vie;",
                NewsUserReviewStatusType.FIXED, news,
                "Ich reise viel, ich reise gern. Fern und nah und nah und fern");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setNewsUserReviewStatusType(NewsUserReviewStatusType.FIXED);
        patch.setApprovedText("new approved text");

        Assertions.assertThatThrownBy(()-> portalUserNewsUserReviewNoteService.patchPortalUserNewsUserReviewNote(portalUser.getId(),
                newsUserReviewNote.getId(), patch)).isInstanceOf(UnprocessableEntityException.class);
    }

    private Boolean getNewsUserReviewNotesWithSameIndexes(UUID newsId,
                                                          NewsUserReviewStatusType newsUserReviewStatusType,
                                                          List<UUID> newsUserReviewNotesIds) {
        Query query = entityManager.createQuery("select count(e) from NewsUserReviewNote e"
                + " where e.news.id = :newsId and e.newsUserReviewStatusType != :newsUserReviewStatusType"
                + " and e.id in (:newsUserReviewNotes)");
        query.setParameter("newsId", newsId);
        query.setParameter("newsUserReviewStatusType", newsUserReviewStatusType);
        query.setParameter("newsUserReviewNotes", newsUserReviewNotesIds);
        return ((Number) query.getSingleResult()).intValue() > 0;
    }
}
