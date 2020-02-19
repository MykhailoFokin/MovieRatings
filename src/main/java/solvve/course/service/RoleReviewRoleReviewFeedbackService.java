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
import solvve.course.repository.RepositoryHelper;
import solvve.course.repository.RoleReviewFeedbackRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleReviewRoleReviewFeedbackService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Transactional(readOnly = true)
    public List<RoleReviewFeedbackReadDTO> getRoleReviewRoleReviewFeedback(UUID roleReviewId) {
        List<RoleReviewFeedback> roleReviewFeedbackList = getRoleReviewRoleReviewFeedbacksRequired(roleReviewId);
        return roleReviewFeedbackList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public RoleReviewFeedbackReadDTO createRoleReviewRoleReviewFeedback(UUID roleReviewId,
                                                                        RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = translationService.toEntity(create);
        roleReviewFeedback.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, roleReviewId));
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
