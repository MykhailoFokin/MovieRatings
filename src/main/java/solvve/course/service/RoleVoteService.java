package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVote;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
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
        RoleVote roleVote = getRoleVoteRequired(id);
        return toRead(roleVote);
    }

    private RoleVoteReadDTO toRead(RoleVote roleVote) {
        RoleVoteReadDTO dto = new RoleVoteReadDTO();
        dto.setId(roleVote.getId());
        dto.setUserId(roleVote.getUserId());
        dto.setRoleId(roleVote.getRoleId());
        dto.setRating(roleVote.getRating());
        return dto;
    }

    public RoleVoteReadDTO createRoleVote(RoleVoteCreateDTO create) {
        RoleVote roleVote = new RoleVote();
        roleVote.setUserId(create.getUserId());
        roleVote.setRoleId(create.getRoleId());
        roleVote.setRating(create.getRating());

        roleVote = roleVoteRepository.save(roleVote);
        return toRead(roleVote);
    }

    public RoleVoteReadDTO patchRoleVote(UUID id, RoleVotePatchDTO patch) {
        RoleVote roleVote = getRoleVoteRequired(id);

        if (patch.getUserId()!=null) {
            roleVote.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleVote.setRoleId(patch.getRoleId());
        }
        if (patch.getRating()!=null) {
            roleVote.setRating(patch.getRating());
        }
        roleVote = roleVoteRepository.save(roleVote);
        return toRead(roleVote);
    }

    private RoleVote getRoleVoteRequired(UUID id) {
        return roleVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleVote.class, id);
        });
    }

    public void deleteRoleVote(UUID id) {
        roleVoteRepository.delete(getRoleVoteRequired(id));
    }
}
