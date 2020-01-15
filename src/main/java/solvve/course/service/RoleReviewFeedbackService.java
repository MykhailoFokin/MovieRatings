package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewFeedbackRepository;

import java.util.UUID;

@Service
public class RoleReviewFeedbackService {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Transactional(readOnly = true)
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(UUID id) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);
        return toRead(roleReviewFeedback);
    }

    private RoleReviewFeedbackReadDTO toRead(RoleReviewFeedback roleReviewFeedback) {
        RoleReviewFeedbackReadDTO dto = new RoleReviewFeedbackReadDTO();
        dto.setId(roleReviewFeedback.getId());
        dto.setUserId(roleReviewFeedback.getUserId());
        dto.setRoleId(roleReviewFeedback.getRoleId());
        dto.setRoleReviewId(roleReviewFeedback.getRoleReviewId());
        dto.setIsLiked(roleReviewFeedback.getIsLiked());
        return dto;
    }

    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(create.getUserId());
        roleReviewFeedback.setRoleId(create.getRoleId());
        roleReviewFeedback.setRoleReviewId(create.getRoleReviewId());
        roleReviewFeedback.setIsLiked(create.getIsLiked());

        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return toRead(roleReviewFeedback);
    }

    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(UUID id, RoleReviewFeedbackPatchDTO patch) {
        RoleReviewFeedback roleReviewFeedback = getRoleReviewFeedbackRequired(id);

        if (patch.getUserId()!=null) {
            roleReviewFeedback.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReviewFeedback.setRoleId(patch.getRoleId());
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewFeedback.setRoleReviewId(patch.getRoleReviewId());
        }
        if (patch.getIsLiked()!=null) {
            roleReviewFeedback.setIsLiked(patch.getIsLiked());
        }
        roleReviewFeedback = roleReviewFeedbackRepository.save(roleReviewFeedback);
        return toRead(roleReviewFeedback);
    }

    private RoleReviewFeedback getRoleReviewFeedbackRequired(UUID id) {
        return roleReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, id);
        });
    }

    public void deleteRoleReviewFeedback(UUID id) {
        roleReviewFeedbackRepository.delete(getRoleReviewFeedbackRequired(id));
    }
}
