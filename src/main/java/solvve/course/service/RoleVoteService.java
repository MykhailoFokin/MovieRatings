package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVote;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
import solvve.course.dto.RoleVotePutDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.repository.RoleVoteRepository;

import java.util.UUID;

@Service
public class RoleVoteService extends AbstractService {

    @Autowired
    private RoleVoteRepository roleVoteRepository;


    @Transactional(readOnly = true)
    public RoleVoteReadDTO getRoleVote(UUID id) {
        RoleVote roleVote = repositoryHelper.getByIdRequired(RoleVote.class, id);
        return translationService.translate(roleVote, RoleVoteReadDTO.class);
    }

    public RoleVoteReadDTO createRoleVote(RoleVoteCreateDTO create) {
        RoleVote roleVote = translationService.translate(create, RoleVote.class);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.translate(roleVote, RoleVoteReadDTO.class);
    }

    public RoleVoteReadDTO patchRoleVote(UUID id, RoleVotePatchDTO patch) {
        RoleVote roleVote = repositoryHelper.getByIdRequired(RoleVote.class, id);

        translationService.map(patch, roleVote);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.translate(roleVote, RoleVoteReadDTO.class);
    }

    public void deleteRoleVote(UUID id) {
        roleVoteRepository.delete(repositoryHelper.getByIdRequired(RoleVote.class, id));
    }

    public RoleVoteReadDTO updateRoleVote(UUID id, RoleVotePutDTO put) {
        RoleVote roleVote = repositoryHelper.getByIdRequired(RoleVote.class, id);

        translationService.updateEntity(put, roleVote);

        roleVote = roleVoteRepository.save(roleVote);
        return translationService.translate(roleVote, RoleVoteReadDTO.class);
    }
}
