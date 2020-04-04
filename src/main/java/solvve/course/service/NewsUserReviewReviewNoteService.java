package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsUserReview;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsUserReviewNoteRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NewsUserReviewReviewNoteService extends AbstractService {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Transactional(readOnly = true)
    public List<NewsUserReviewNoteReadDTO> getNewsUserReviewUserReviewNote(UUID newsId, UUID newsUserReviewId) {
        List<NewsUserReviewNote> newsUserReviewNotes = getNewsUserReviewReviewNotesRequired(newsId, newsUserReviewId);
        return newsUserReviewNotes.stream().map(e ->
                translationService.translate(e, NewsUserReviewNoteReadDTO.class)).collect(Collectors.toList());
    }

    public NewsUserReviewNoteReadDTO createNewsUserReviewReviewNote(UUID newsId, UUID newsUserReviewId,
                                                                         NewsUserReviewNoteCreateDTO create) {
        NewsUserReviewNote newsUserReviewNote = translationService.translate(create, NewsUserReviewNote.class);
        newsUserReviewNote.setNewsUserReview(repositoryHelper.getReferenceIfExists(NewsUserReview.class,
                newsUserReviewId));
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);

        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public NewsUserReviewNoteReadDTO patchNewsUserReviewReviewNote(UUID newsId, UUID newsUserReviewId, UUID id,
                                                                        NewsUserReviewNotePatchDTO patch) {
        NewsUserReviewNote newsUserReviewNote = getNewsUserReviewReviewNoteRequired(newsId, newsUserReviewId, id);

        translationService.map(patch, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);

        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public void deleteNewsUserReviewReviewNote(UUID newsId, UUID newsUserReviewId, UUID id) {
        newsUserReviewNoteRepository.delete(getNewsUserReviewReviewNoteRequired(newsId, newsUserReviewId, id));
    }

    public NewsUserReviewNoteReadDTO updateNewsUserReviewReviewNote(UUID newsId, UUID newsUserReviewId, UUID id,
                                                                         NewsUserReviewNotePutDTO put) {
        NewsUserReviewNote newsUserReviewNote = getNewsUserReviewReviewNoteRequired(newsId, newsUserReviewId, id);

        translationService.updateEntity(put, newsUserReviewNote);
        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);

        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    private NewsUserReviewNote getNewsUserReviewReviewNoteRequired(UUID newsId, UUID newsUserReviewId, UUID id) {
        return newsUserReviewNoteRepository
                .findByNewsUserReviewIdAndIdAndNewsId(newsUserReviewId, id, newsId).orElseThrow(() -> {
                    throw new EntityNotFoundException(NewsUserReviewNote.class, newsUserReviewId, id);
                });
    }

    private List<NewsUserReviewNote> getNewsUserReviewReviewNotesRequired(UUID newsId, UUID newsUserReviewId) {
        return newsUserReviewNoteRepository
                .findByNewsUserReviewIdAndNewsIdOrderById(newsUserReviewId, newsId).orElseThrow(() -> {
                    throw new EntityNotFoundException(NewsUserReviewNote.class, newsUserReviewId);
                });
    }
}
