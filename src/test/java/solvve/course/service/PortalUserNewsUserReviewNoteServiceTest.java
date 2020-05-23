package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.exception.LinkageCorruptedEntityException;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.NewsUserReviewNoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

public class PortalUserNewsUserReviewNoteServiceTest extends BaseTest {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private PortalUserNewsUserReviewNoteService portalUserNewsUserReviewNoteService;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testGetNewsUserReviewNote() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 0, 14, "Ich reise viel",
                ModeratorTypoReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        List<NewsUserReviewNoteReadDTO> readDTO =
                portalUserNewsUserReviewNoteService.getModeratorUserReviewNotes(portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(newsUserReviewNote.getId());
    }

    @Test
    public void testPatchPortalUserNewsUserReviewNote() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser1,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern", movie);
        NewsUserReview newsUserReview1 = testObjectsFactory.createNewsUserReview(portalUser1, news, portalUser1,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote1 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview1, 0, 14, "Ich reise viel",
                ModeratorTypoReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        NewsUserReview newsUserReview2 = testObjectsFactory.createNewsUserReview(portalUser2, news, portalUser1,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote2 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview2, 0, 14, "Ivh reide vie;",
                ModeratorTypoReviewStatusType.IN_REVIEW, news,
                "Ivh reide vie;");

        NewsUserReview newsUserReview3 = testObjectsFactory.createNewsUserReview(portalUser2, news, portalUser1,
                ModeratorTypoReviewStatusType.NEED_TO_FIX);
        NewsUserReviewNote newsUserReviewNote3 = testObjectsFactory.createNewsUserReviewNote(portalUser1,
                newsUserReview3, 0, 14, "Ivh reide vie;",
                ModeratorTypoReviewStatusType.NEED_TO_FIX, news,
                "Ivh reide vie;");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setApprovedText("Ich reise viel");
        patch.setStartIndex(0);
        patch.setEndIndex(14);
        patch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        patch.setModeratorId(portalUser2.getId());

        StringBuilder sb = new StringBuilder(news.getDescription());
        sb.replace(newsUserReviewNote1.getStartIndex(), newsUserReviewNote1.getEndIndex(),
                newsUserReviewNote1.getProposedText());

        NewsUserReviewNoteReadDTO read =
                portalUserNewsUserReviewNoteService.patchPortalUserNewsUserReviewNote(portalUser1.getId(),
                        newsUserReviewNote1.getId(), patch);
        News newsAfterUpdate = newsRepository.findById(news.getId()).get();

        Assertions.assertThat(newsAfterUpdate.getDescription()).isEqualTo(sb.toString());
        Assertions.assertThat(read.getModeratorId()).isEqualTo(patch.getModeratorId());
        Assertions.assertThat(read.getModeratorTypoReviewStatusType())
                .isEqualTo(patch.getModeratorTypoReviewStatusType());

        Assert.assertFalse(getNewsUserReviewNotesWithSameIndexes(news.getId(), read.getModeratorTypoReviewStatusType()
                , List.of(newsUserReviewNote2.getId(), newsUserReviewNote3.getId())));
    }

    @Test
    public void testPatchPortalUserNewsUserReviewNoteInFixedStatus() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern", movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 0, 14, "Ivh reide vie;",
                ModeratorTypoReviewStatusType.FIXED, news,
                "Ich reise viel, ich reise gern. Fern und nah und nah und fern");

        NewsUserReviewNotePatchDTO patch = new NewsUserReviewNotePatchDTO();
        patch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        patch.setApprovedText("new approved text");

        Assertions.assertThatThrownBy(()->
                portalUserNewsUserReviewNoteService.patchPortalUserNewsUserReviewNote(portalUser.getId(),
                newsUserReviewNote.getId(), patch)).isInstanceOf(LinkageCorruptedEntityException.class);
    }

    @Test
    public void testUpdatePortalUserNewsUserReviewNoteInFixedStatus() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern", movie);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        NewsUserReviewNote newsUserReviewNote = testObjectsFactory.createNewsUserReviewNote(portalUser,
                newsUserReview, 0, 14, "Ivh reide vie;",
                ModeratorTypoReviewStatusType.FIXED, news,
                "Ich reise viel, ich reise gern. Fern und nah und nah und fern");

        NewsUserReviewNotePutDTO put = new NewsUserReviewNotePutDTO();
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        put.setApprovedText("new approved text");

        Assertions.assertThatThrownBy(()->
                portalUserNewsUserReviewNoteService.updatePortalUserNewsUserReviewNote(portalUser.getId(),
                        newsUserReviewNote.getId(), put)).isInstanceOf(LinkageCorruptedEntityException.class);
    }

    private Boolean getNewsUserReviewNotesWithSameIndexes(UUID newsId,
                                                          ModeratorTypoReviewStatusType moderatorTypoReviewStatusType,
                                                          List<UUID> newsUserReviewNotesIds) {
        Query query = entityManager.createQuery("select count(e) from NewsUserReviewNote e"
                + " where e.news.id = :newsId and e.moderatorTypoReviewStatusType != :moderatorTypoReviewStatusType"
                + " and e.id in (:newsUserReviewNotes)");
        query.setParameter("newsId", newsId);
        query.setParameter("moderatorTypoReviewStatusType", moderatorTypoReviewStatusType);
        query.setParameter("newsUserReviewNotes", newsUserReviewNotesIds);
        return ((Number) query.getSingleResult()).intValue() > 0;
    }
}
