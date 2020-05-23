package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsFeedback;
import solvve.course.dto.*;
import solvve.course.repository.NewsFeedbackRepository;

import java.util.UUID;

@Service
public class NewsFeedbackService extends AbstractService {

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Autowired
    private NewsService newsService;

    @Transactional(readOnly = true)
    public NewsFeedbackReadDTO getNewsFeedback(UUID id) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    public NewsFeedbackReadDTO createNewsFeedback(NewsFeedbackCreateDTO create) {
        NewsFeedback newsFeedback = translationService.translate(create, NewsFeedback.class);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);

        newsService.updateAverageRatingOfNews(newsFeedback.getNews().getId());

        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    public NewsFeedbackReadDTO patchNewsFeedback(UUID id, NewsFeedbackPatchDTO patch) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);

        translationService.map(patch, newsFeedback);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);
        if (!newsFeedback.getIsLiked().equals(patch.getIsLiked())) {
            newsService.updateAverageRatingOfNews(newsFeedback.getNews().getId());
        }
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    @Transactional
    public void deleteNewsFeedback(UUID id) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);
        newsFeedbackRepository.delete(newsFeedback);
        newsService.updateAverageRatingOfNews(newsFeedback.getNews().getId());
    }

    public NewsFeedbackReadDTO updateNewsFeedback(UUID id, NewsFeedbackPutDTO put) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);

        translationService.updateEntity(put, newsFeedback);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);
        if (newsFeedback.getIsLiked() != null && !newsFeedback.getIsLiked().equals(put.getIsLiked())) {
            newsService.updateAverageRatingOfNews(newsFeedback.getNews().getId());
        }
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }
}
