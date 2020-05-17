package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsFeedback;
import solvve.course.dto.NewsFeedbackCreateDTO;
import solvve.course.dto.NewsFeedbackPatchDTO;
import solvve.course.dto.NewsFeedbackPutDTO;
import solvve.course.dto.NewsFeedbackReadDTO;
import solvve.course.repository.NewsFeedbackRepository;

import java.util.UUID;

@Service
public class NewsFeedbackService extends AbstractService {

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Transactional(readOnly = true)
    public NewsFeedbackReadDTO getNewsFeedback(UUID id) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    public NewsFeedbackReadDTO createNewsFeedback(NewsFeedbackCreateDTO create) {
        NewsFeedback newsFeedback = translationService.translate(create, NewsFeedback.class);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    public NewsFeedbackReadDTO patchNewsFeedback(UUID id, NewsFeedbackPatchDTO patch) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);

        translationService.map(patch, newsFeedback);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }

    public void deleteNewsFeedback(UUID id) {
        newsFeedbackRepository.delete(repositoryHelper.getByIdRequired(NewsFeedback.class, id));
    }

    public NewsFeedbackReadDTO updateNewsFeedback(UUID id, NewsFeedbackPutDTO put) {
        NewsFeedback newsFeedback = repositoryHelper.getByIdRequired(NewsFeedback.class, id);

        translationService.updateEntity(put, newsFeedback);

        newsFeedback = newsFeedbackRepository.save(newsFeedback);
        return translationService.translate(newsFeedback, NewsFeedbackReadDTO.class);
    }
}
