package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.domain.ModeratorTypoReviewStatusType;
import solvve.course.domain.NewsUserReview;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.domain.PortalUser;
import solvve.course.dto.*;
import solvve.course.exception.LinkageCorruptedEntityException;
import solvve.course.repository.NewsUserReviewNoteRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortalUserNewsUserReviewNoteService extends AbstractService {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private NewsUserReviewService newsUserReviewService;

    @Transactional(readOnly = true)
    public List<NewsUserReviewNoteReadDTO> getModeratorUserReviewNotes(UUID moderatorId) {
        List<NewsUserReviewNote> newsUserReviewNotes = getNewsUserReviewNotesRequired(moderatorId);
        return newsUserReviewNotes.stream().map(e ->
                translationService.translate(e, NewsUserReviewNoteReadDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public NewsUserReviewNoteReadDTO patchPortalUserNewsUserReviewNote(UUID portalUserId, UUID newsUserReviewNoteId,
                                                                   NewsUserReviewNotePatchDTO patch) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        NewsUserReviewNote newsUserReviewNote =
                repositoryHelper.getByIdRequired(NewsUserReviewNote.class, newsUserReviewNoteId);
        if (patch.getModeratorId() != null) {
            newsUserReviewNote.setModerator(null);
        }
        checkAlreadyFixedUserReviewNote(newsUserReviewNote.getModeratorTypoReviewStatusType(),
                patch.getModeratorTypoReviewStatusType(), newsUserReviewNoteId);

        translationService.map(patch, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        UUID newsUserReviewId = newsUserReviewNote.getNewsUserReview().getId();

        fixNewsByUserReviewNote(newsUserReviewNote.getNews().getId(), patch.getStartIndex(), patch.getEndIndex(),
                patch.getApprovedText());

        if (newsUserReviewNote.getModeratorTypoReviewStatusType() == ModeratorTypoReviewStatusType.FIXED) {
            transactionTemplate.executeWithoutResult(status -> {
                if (!repositoryHelper.validateIfExistsNotNewsUserReviewStatus(NewsUserReview.class, newsUserReviewId,
                        ModeratorTypoReviewStatusType.FIXED)) {
                    NewsUserReviewPatchDTO newsUserReviewPatch = new NewsUserReviewPatchDTO();
                    newsUserReviewPatch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
                    newsUserReviewService.patchNewsUserReview(newsUserReviewId, newsUserReviewPatch);
                }
            });
        }

        updateSameNotesWithSameStatus(newsUserReviewNote);

        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public NewsUserReviewNoteReadDTO updatePortalUserNewsUserReviewNote(UUID portalUserId, UUID newsUserReviewNoteId,
                                                                    NewsUserReviewNotePutDTO put) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        NewsUserReviewNote newsUserReviewNote =
                repositoryHelper.getByIdRequired(NewsUserReviewNote.class, newsUserReviewNoteId);

        checkAlreadyFixedUserReviewNote(newsUserReviewNote.getModeratorTypoReviewStatusType(),
                put.getModeratorTypoReviewStatusType(), newsUserReviewNoteId);

        translationService.updateEntity(put, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        UUID newsUserReviewId = newsUserReviewNote.getNewsUserReview().getId();

        fixNewsByUserReviewNote(newsUserReviewNote.getNews().getId(), put.getStartIndex(), put.getEndIndex(),
                put.getApprovedText());

        if (newsUserReviewNote.getModeratorTypoReviewStatusType() == ModeratorTypoReviewStatusType.FIXED) {
            transactionTemplate.executeWithoutResult(status -> {
                if (!repositoryHelper.validateIfExistsNotNewsUserReviewStatus(NewsUserReview.class, newsUserReviewId,
                        ModeratorTypoReviewStatusType.FIXED)) {
                    NewsUserReviewPatchDTO newsUserReviewPatch = new NewsUserReviewPatchDTO();
                    newsUserReviewPatch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
                    newsUserReviewService.patchNewsUserReview(newsUserReviewId, newsUserReviewPatch);
                }
            });
        }

        updateSameNotesWithSameStatus(newsUserReviewNote);

        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    private List<NewsUserReviewNote> getNewsUserReviewNotesRequired(UUID moderatorId) {
        return newsUserReviewNoteRepository.findUserReviewNotesByModeratorOrRequiredAttention(moderatorId,
                List.of(ModeratorTypoReviewStatusType.IN_REVIEW, ModeratorTypoReviewStatusType.NEED_TO_FIX));
    }

    private void checkAlreadyFixedUserReviewNote(ModeratorTypoReviewStatusType entityModeratorTypoReviewStatusType,
                                                 ModeratorTypoReviewStatusType newModeratorTypoReviewStatusType,
                                                 UUID id) {
        if (entityModeratorTypoReviewStatusType == ModeratorTypoReviewStatusType.FIXED
                && newModeratorTypoReviewStatusType == ModeratorTypoReviewStatusType.FIXED) {
            throw new LinkageCorruptedEntityException(NewsUserReviewNote.class, id);
        }
    }

    private void fixNewsByUserReviewNote(UUID newsId, Integer startIndex, Integer endIndex, String approvedText) {
        NewsReadDTO news = newsService.getNews(newsId);
        StringBuilder str = new StringBuilder(news.getDescription());
        str.replace(startIndex, endIndex, approvedText);
        NewsPatchDTO newsPatchDTO = new NewsPatchDTO();
        newsPatchDTO.setDescription(str.toString());
        newsService.patchNews(newsId, newsPatchDTO);
    }

    private void updateSameNotesWithSameStatus(NewsUserReviewNote newsUserReviewNote) {
        List<NewsUserReviewNote> newsUserReviewNotes =
                newsUserReviewNoteRepository.findByNewsIdAndStartIndexAndEndIndex(
                        newsUserReviewNote.getNewsUserReview().getNews().getId(),
                        newsUserReviewNote.getStartIndex(),
                        newsUserReviewNote.getEndIndex()).get();
        List<UUID> newsUserReviewNotesIds =
                newsUserReviewNotes.stream().map(NewsUserReviewNote::getId).collect(Collectors.toList());

        newsUserReviewNoteRepository.updateNewsUserReviewNoteWithSameNote(newsUserReviewNote.getNews().getId(),
                newsUserReviewNote.getModeratorTypoReviewStatusType(), newsUserReviewNote.getModerator().getId(),
                newsUserReviewNote.getApprovedText(), newsUserReviewNote.getUpdatedAt(), newsUserReviewNotesIds);
    }
}
