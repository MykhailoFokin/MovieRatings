package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewFeedbackRepository;

import java.util.UUID;

@Service
public class RoleReviewFeedbackService {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(UUID id) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);
        return translationService.toRead(roleReviewFeedback);
    }

    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = translationService.toEntity(create);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(UUID id, RoleReviewFeedbackPatchDTO patch) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);

        translationService.patchEntity(patch, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    private RoleReviewFeedback getRoleReviewFeedbackRequired(UUID id) {
        return roleReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, id);
        });
    }

    public void deleteRoleReviewFeedback(UUID id) {
        roleReviewFeedbackRepository.delete(getRoleReviewFeedbackRequired(id));
    }

    public RoleReviewFeedbackReadDTO putRoleReviewFeedback(UUID id, RoleReviewFeedbackPutDTO put) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);

        translationService.putEntity(put, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }
}
