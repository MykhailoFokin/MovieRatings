package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.domain.NewsUserReview;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.domain.NewsUserReviewStatusType;
import solvve.course.domain.PortalUser;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.UnprocessableEntityException;
import solvve.course.repository.NewsUserReviewNoteRepository;
import solvve.course.repository.RepositoryHelper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortalUserNewsUserReviewNoteService {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Autowired
    private NewsUserReviewService newsUserReviewService;

    @Transactional(readOnly = true)
    public List<NewsUserReviewNoteReadDTO> getModeratorUserReviewNotes(UUID moderatorId) {
        List<NewsUserReviewNote> newsUserReviewNotes = getNewsUserReviewNotesRequired(moderatorId);
        return newsUserReviewNotes.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    @Transactional
    public NewsUserReviewNoteReadDTO patchPortalUserNewsUserReviewNote(UUID portalUserId, UUID newsUserReviewNoteId,
                                                                   NewsUserReviewNotePatchDTO patch) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        NewsUserReviewNote newsUserReviewNote = getUserReviewNoteRequired(newsUserReviewNoteId);

        checkAlreadyFixedUserReviewNote(newsUserReviewNote.getNewsUserReviewStatusType(),
                patch.getNewsUserReviewStatusType(), newsUserReviewNoteId);

        translationService.patchEntity(patch, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        UUID newsUserReviewId = newsUserReviewNote.getNewsUserReview().getId();

        fixNewsByUserReviewNote(newsUserReviewNote.getNews().getId(), patch.getStartIndex(), patch.getEndIndex(),
                patch.getApprovedText());

        if (newsUserReviewNote.getNewsUserReviewStatusType() == NewsUserReviewStatusType.FIXED) {
            transactionTemplate.executeWithoutResult(status -> {
                if (!repositoryHelper.validateIfExistsNotNewsUserReviewStatus(NewsUserReview.class, newsUserReviewId,
                        NewsUserReviewStatusType.FIXED)) {
                    NewsUserReviewPatchDTO newsUserReviewPatch = new NewsUserReviewPatchDTO();
                    newsUserReviewPatch.setNewsUserReviewStatusType(NewsUserReviewStatusType.FIXED);
                    newsUserReviewService.patchNewsUserReview(newsUserReviewId, newsUserReviewPatch);
                }
            });
        }

        updateSameNotesWithSameStatus(newsUserReviewNote);

        return translationService.toRead(newsUserReviewNote);
    }

    public NewsUserReviewNoteReadDTO updatePortalUserNewsUserReviewNote(UUID portalUserId, UUID newsUserReviewNoteId,
                                                                    NewsUserReviewNotePutDTO put) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);
        NewsUserReviewNote newsUserReviewNote = getUserReviewNoteRequired(newsUserReviewNoteId);

        checkAlreadyFixedUserReviewNote(newsUserReviewNote.getNewsUserReviewStatusType(),
                put.getNewsUserReviewStatusType(), newsUserReviewNoteId);

        translationService.updateEntity(put, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        UUID newsUserReviewId = newsUserReviewNote.getNewsUserReview().getId();

        fixNewsByUserReviewNote(put.getNewsId(), put.getStartIndex(), put.getEndIndex(), put.getApprovedText());

        if (newsUserReviewNote.getNewsUserReviewStatusType() == NewsUserReviewStatusType.FIXED) {
            transactionTemplate.executeWithoutResult(status -> {
                if (!repositoryHelper.validateIfExistsNotNewsUserReviewStatus(NewsUserReview.class, newsUserReviewId,
                        NewsUserReviewStatusType.FIXED)) {
                    NewsUserReviewPatchDTO newsUserReviewPatch = new NewsUserReviewPatchDTO();
                    newsUserReviewPatch.setNewsUserReviewStatusType(NewsUserReviewStatusType.FIXED);
                    newsUserReviewService.patchNewsUserReview(newsUserReviewId, newsUserReviewPatch);
                }
            });
        }

        updateSameNotesWithSameStatus(newsUserReviewNote);

        return translationService.toRead(newsUserReviewNote);
    }

    private List<NewsUserReviewNote> getNewsUserReviewNotesRequired(UUID moderatorId) {
        return newsUserReviewNoteRepository.findUserReviewNotesByModeratorOrRequiredAttention(moderatorId,
                List.of(NewsUserReviewStatusType.IN_REVIEW, NewsUserReviewStatusType.NEED_TO_FIX));
    }

    private NewsUserReviewNote getUserReviewNoteRequired(UUID id) {
        return newsUserReviewNoteRepository.findById(id)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(NewsUserReviewNote.class, id);
                });
    }

    private void checkAlreadyFixedUserReviewNote(NewsUserReviewStatusType entityNewsUserReviewStatusType,
                                                 NewsUserReviewStatusType newNewsUserReviewStatusType,
                                                 UUID id) {
        if (entityNewsUserReviewStatusType == NewsUserReviewStatusType.FIXED
                && newNewsUserReviewStatusType == NewsUserReviewStatusType.FIXED) {
            throw new UnprocessableEntityException(NewsUserReviewNote.class, id);
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
                newsUserReviewNote.getNewsUserReviewStatusType(), newsUserReviewNote.getModerator().getId(),
                newsUserReviewNote.getApprovedText(), newsUserReviewNote.getUpdatedAt(), newsUserReviewNotesIds);
    }
}
