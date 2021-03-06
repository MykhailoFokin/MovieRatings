package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.repository.RoleReviewFeedbackRepository;

import java.util.UUID;

@Service
public class RoleReviewFeedbackService extends AbstractService {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Transactional(readOnly = true)
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(UUID id) {
        RoleReviewFeedback roleReviewFeedback = repositoryHelper.getByIdRequired(RoleReviewFeedback.class, id);
        return translationService.translate(roleReviewFeedback, RoleReviewFeedbackReadDTO.class);
    }

    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = translationService.translate(create, RoleReviewFeedback.class);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.translate(roleReviewFeedback, RoleReviewFeedbackReadDTO.class);
    }

    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(UUID id, RoleReviewFeedbackPatchDTO patch) {
        RoleReviewFeedback roleReviewFeedback = repositoryHelper.getByIdRequired(RoleReviewFeedback.class, id);

        translationService.map(patch, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.translate(roleReviewFeedback, RoleReviewFeedbackReadDTO.class);
    }

    public void deleteRoleReviewFeedback(UUID id) {
        roleReviewFeedbackRepository.delete(repositoryHelper.getByIdRequired(RoleReviewFeedback.class, id));
    }

    public RoleReviewFeedbackReadDTO updateRoleReviewFeedback(UUID id, RoleReviewFeedbackPutDTO put) {
        RoleReviewFeedback roleReviewFeedback = repositoryHelper.getByIdRequired(RoleReviewFeedback.class, id);

        translationService.updateEntity(put, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.translate(roleReviewFeedback, RoleReviewFeedbackReadDTO.class);
    }
}
