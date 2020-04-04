package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.dto.NewsUserReviewNoteCreateDTO;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.repository.NewsUserReviewNoteRepository;

import java.util.UUID;

@Service
public class NewsUserReviewNoteService extends AbstractService {

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Transactional(readOnly = true)
    public NewsUserReviewNoteReadDTO getNewsUserReviewNote(UUID id) {
        NewsUserReviewNote newsUserReviewNote = repositoryHelper.getByIdRequired(NewsUserReviewNote.class, id);
        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public NewsUserReviewNoteReadDTO createNewsUserReviewNote(NewsUserReviewNoteCreateDTO create) {
        NewsUserReviewNote newsUserReviewNote = translationService.translate(create, NewsUserReviewNote.class);

        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public NewsUserReviewNoteReadDTO patchNewsUserReviewNote(UUID id, NewsUserReviewNotePatchDTO patch) {
        NewsUserReviewNote newsUserReviewNote = repositoryHelper.getByIdRequired(NewsUserReviewNote.class, id);

        translationService.map(patch, newsUserReviewNote);

        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }

    public void deleteNewsUserReviewNote(UUID id) {
        newsUserReviewNoteRepository.delete(repositoryHelper.getByIdRequired(NewsUserReviewNote.class, id));
    }

    public NewsUserReviewNoteReadDTO updateNewsUserReviewNote(UUID id, NewsUserReviewNotePutDTO put) {
        NewsUserReviewNote newsUserReviewNote = repositoryHelper.getByIdRequired(NewsUserReviewNote.class, id);

        translationService.updateEntity(put, newsUserReviewNote);

        newsUserReviewNote = newsUserReviewNoteRepository.save(newsUserReviewNote);
        return translationService.translate(newsUserReviewNote, NewsUserReviewNoteReadDTO.class);
    }
}
