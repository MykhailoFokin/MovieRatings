package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVoteFeedback;
import solvve.course.dto.RoleVoteFeedbackCreateDTO;
import solvve.course.dto.RoleVoteFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleVoteFeedbackRepository;

import java.util.UUID;

@Service
public class RoleVoteFeedbackService {

    @Autowired
    private RoleVoteFeedbackRepository roleVoteFeedbackRepository;

    @Transactional(readOnly = true)
    public RoleVoteFeedbackReadDTO getRoleVoteFeedback(UUID id) {
        RoleVoteFeedback roleVoteFeedback = roleVoteFeedbackRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleVoteFeedback.class, id);
        });
        return toRead(roleVoteFeedback);
    }

    private RoleVoteFeedbackReadDTO toRead(RoleVoteFeedback roleVoteFeedback) {
        RoleVoteFeedbackReadDTO dto = new RoleVoteFeedbackReadDTO();
        dto.setId(roleVoteFeedback.getId());
        dto.setUserId(roleVoteFeedback.getUserId());
        dto.setRoleId(roleVoteFeedback.getRoleId());
        dto.setRoleVoteId(roleVoteFeedback.getRoleVoteId());
        dto.setIsLiked(roleVoteFeedback.getIsLiked());
        return dto;
    }

    public RoleVoteFeedbackReadDTO createRoleVoteFeedback(RoleVoteFeedbackCreateDTO create) {
        RoleVoteFeedback roleVoteFeedback = new RoleVoteFeedback();
        roleVoteFeedback.setUserId(create.getUserId());
        roleVoteFeedback.setRoleId(create.getRoleId());
        roleVoteFeedback.setRoleVoteId(create.getRoleVoteId());
        roleVoteFeedback.setIsLiked(create.getIsLiked());

        roleVoteFeedback = roleVoteFeedbackRepository.save(roleVoteFeedback);
        return toRead(roleVoteFeedback);
    }
}
