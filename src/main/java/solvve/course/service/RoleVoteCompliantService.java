package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleVoteCompliant;
import solvve.course.dto.RoleVoteCompliantCreateDTO;
import solvve.course.dto.RoleVoteCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleVoteCompliantRepository;

import java.util.UUID;

@Service
public class RoleVoteCompliantService {

    @Autowired
    private RoleVoteCompliantRepository roleVoteCompliantRepository;

    @Transactional(readOnly = true)
    public RoleVoteCompliantReadDTO getRoleVoteCompliant(UUID id) {
        RoleVoteCompliant roleVoteCompliant = roleVoteCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleVoteCompliant.class, id);
        });
        return toRead(roleVoteCompliant);
    }

    private RoleVoteCompliantReadDTO toRead(RoleVoteCompliant roleVoteCompliant) {
        RoleVoteCompliantReadDTO dto = new RoleVoteCompliantReadDTO();
        dto.setId(roleVoteCompliant.getId());
        dto.setUserId(roleVoteCompliant.getUserId());
        dto.setRoleId(roleVoteCompliant.getRoleId());
        dto.setRoleVoteId(roleVoteCompliant.getRoleVoteId());
        dto.setDescription(roleVoteCompliant.getDescription());
        dto.setModeratedStatus(roleVoteCompliant.getModeratedStatus());
        dto.setModeratorId(roleVoteCompliant.getModeratorId());
        return dto;
    }

    public RoleVoteCompliantReadDTO createRoleVoteCompliant(RoleVoteCompliantCreateDTO create) {
        RoleVoteCompliant roleVoteCompliant = new RoleVoteCompliant();
        roleVoteCompliant.setUserId(create.getUserId());
        roleVoteCompliant.setRoleId(create.getRoleId());
        roleVoteCompliant.setRoleVoteId(create.getRoleVoteId());
        roleVoteCompliant.setDescription(create.getDescription());
        roleVoteCompliant.setModeratedStatus(create.getModeratedStatus());
        roleVoteCompliant.setModeratorId(create.getModeratorId());

        roleVoteCompliant = roleVoteCompliantRepository.save(roleVoteCompliant);
        return toRead(roleVoteCompliant);
    }
}
