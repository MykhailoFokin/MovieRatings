package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVote;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
import solvve.course.dto.RoleVotePutDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleVoteRepository;

import java.util.UUID;

@Service
public class RoleVoteService {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleVoteReadDTO getRoleVote(UUID id) {
        RoleVote roleVote = getRoleVoteRequired(id);
        return translationService.toRead(roleVote);
    }

    public RoleVoteReadDTO createRoleVote(RoleVoteCreateDTO create) {
        RoleVote roleVote = translationService.toEntity(create);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.toRead(roleVote);
    }

    public RoleVoteReadDTO patchRoleVote(UUID id, RoleVotePatchDTO patch) {
        RoleVote roleVote = getRoleVoteRequired(id);

        translationService.patchEntity(patch, roleVote);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.toRead(roleVote);
    }

    private RoleVote getRoleVoteRequired(UUID id) {
        return roleVoteRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleVote.class, id);
        });
    }

    public void deleteRoleVote(UUID id) {
        roleVoteRepository.delete(getRoleVoteRequired(id));
    }

    public RoleVoteReadDTO updateRoleVote(UUID id, RoleVotePutDTO put) {
        RoleVote roleVote = getRoleVoteRequired(id);

        translationService.updateEntity(put, roleVote);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.toRead(roleVote);
    }
}
