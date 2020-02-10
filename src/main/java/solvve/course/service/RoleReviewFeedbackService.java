package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewFeedbackRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleReviewFeedbackService {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RoleReviewService roleReviewService;

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

    public void deleteRoleReviewFeedback(UUID id) {
        roleReviewFeedbackRepository.delete(getRoleReviewFeedbackRequired(id));
    }

    public RoleReviewFeedbackReadDTO updateRoleReviewFeedback(UUID id, RoleReviewFeedbackPutDTO put) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);

        translationService.updateEntity(put, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    @Transactional(readOnly = true)
    public List<RoleReviewFeedbackReadDTO> getRoleReviewRoleReviewFeedback(UUID roleReviewId) {
        List<RoleReviewFeedback> roleReviewFeedbackList = getRoleReviewRoleReviewFeedbacksRequired(roleReviewId);
        return roleReviewFeedbackList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public RoleReviewFeedbackReadDTO createRoleReviewRoleReviewFeedback(UUID roleReviewId,
                                                                        RoleReviewFeedbackCreateDTO create) {
        RoleReview roleReview = translationService.ReadDTOtoEntity(roleReviewService.getRoleReview(roleReviewId));

        RoleReviewFeedback roleReviewFeedback = translationService.toEntity(create);
        roleReviewFeedback.setRoleReviewId(roleReview);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    public RoleReviewFeedbackReadDTO patchRoleReviewRoleReviewFeedback(UUID roleReviewId, UUID id,
                                                                       RoleReviewFeedbackPatchDTO patch) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewRoleReviewFeedbackRequired(roleReviewId, id);

        translationService.patchEntity(patch, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    public void deleteRoleReviewRoleReviewFeedback(UUID roleReviewId, UUID id) {
        roleReviewFeedbackRepository.delete(getRoleReviewRoleReviewFeedbackRequired(roleReviewId, id));
    }

    public RoleReviewFeedbackReadDTO updateRoleReviewRoleReviewFeedback(UUID roleReviewId, UUID id,
                                                                     RoleReviewFeedbackPutDTO put) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewRoleReviewFeedbackRequired(roleReviewId, id);

        translationService.updateEntity(put, roleReviewFeedback);

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return translationService.toRead(roleReviewFeedback);
    }

    private RoleReviewFeedback getRoleReviewFeedbackRequired(UUID id) {
        return roleReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, id);
        });
    }

    private RoleReviewFeedback getRoleReviewRoleReviewFeedbackRequired(UUID roleReviewId, UUID id) {
        return roleReviewFeedbackRepository.findByRoleReviewIdAndId(roleReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, roleReviewId, id);
        });
    }

    private List<RoleReviewFeedback> getRoleReviewRoleReviewFeedbacksRequired(UUID roleReviewId) {
        return roleReviewFeedbackRepository.findByRoleReviewIdOrderById(roleReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, roleReviewId);
        });
    }
}
