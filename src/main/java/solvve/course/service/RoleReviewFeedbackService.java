package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewFeedback;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
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
        RoleReviewFeedback roleReviewFeedback = roleReviewFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewFeedback.class, id);
        });
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
}
