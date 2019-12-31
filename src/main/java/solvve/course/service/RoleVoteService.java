package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVote;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleVoteRepository;

import java.util.UUID;

@Service
public class RoleVoteService {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Transactional(readOnly = true)
    public RoleVoteReadDTO getRoleVote(UUID id) {
        RoleVote roleVote = roleVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleVote.class, id);
        });
        return toRead(roleVote);
    }

    private RoleVoteReadDTO toRead(RoleVote roleVote) {
        RoleVoteReadDTO dto = new RoleVoteReadDTO();
        dto.setId(roleVote.getId());
        dto.setUserId(roleVote.getUserId());
        dto.setRoleId(roleVote.getRoleId());
        dto.setRating(roleVote.getRating());
        dto.setDescription(roleVote.getDescription());
        dto.setSpoilerStartIndex(roleVote.getSpoilerStartIndex());
        dto.setSpoilerEndIndex(roleVote.getSpoilerEndIndex());
        dto.setModeratedStatus(roleVote.getModeratedStatus());
        dto.setModeratorId(roleVote.getModeratorId());
        return dto;
    }

    public RoleVoteReadDTO createRoleVote(RoleVoteCreateDTO create) {
        RoleVote roleVote = new RoleVote();
        roleVote.setUserId(create.getUserId());
        roleVote.setRoleId(create.getRoleId());
        roleVote.setRating(create.getRating());
        roleVote.setDescription(create.getDescription());
        roleVote.setSpoilerStartIndex(create.getSpoilerStartIndex());
        roleVote.setSpoilerEndIndex(create.getSpoilerEndIndex());
        roleVote.setModeratedStatus(create.getModeratedStatus());
        roleVote.setModeratorId(create.getModeratorId());

        roleVote = roleVoteRepository.save(roleVote);
        return toRead(roleVote);
    }
}
