package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewCompliantRepository;

import java.util.UUID;

@Service
public class RoleReviewCompliantService {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Transactional(readOnly = true)
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(UUID id) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);
        return toRead(roleReviewCompliant);
    }

    private RoleReviewCompliantReadDTO toRead(RoleReviewCompliant roleReviewCompliant) {
        RoleReviewCompliantReadDTO dto = new RoleReviewCompliantReadDTO();
        dto.setId(roleReviewCompliant.getId());
        dto.setUserId(roleReviewCompliant.getUserId());
        dto.setRoleId(roleReviewCompliant.getRoleId());
        dto.setRoleReviewId(roleReviewCompliant.getRoleReviewId());
        dto.setDescription(roleReviewCompliant.getDescription());
        dto.setModeratedStatus(roleReviewCompliant.getModeratedStatus());
        dto.setModeratorId(roleReviewCompliant.getModeratorId());
        return dto;
    }

    public RoleReviewCompliantReadDTO createRoleReviewCompliant(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(create.getUserId());
        roleReviewCompliant.setRoleId(create.getRoleId());
        roleReviewCompliant.setRoleReviewId(create.getRoleReviewId());
        roleReviewCompliant.setDescription(create.getDescription());
        roleReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        roleReviewCompliant.setModeratorId(create.getModeratorId());

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return toRead(roleReviewCompliant);
    }

    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(UUID id, RoleReviewCompliantPatchDTO patch) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);

        if (patch.getUserId()!=null) {
            roleReviewCompliant.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReviewCompliant.setRoleId(patch.getRoleId());
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewCompliant.setRoleReviewId(patch.getRoleReviewId());
        }
        if (patch.getDescription()!=null) {
            roleReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReviewCompliant.setModeratorId(patch.getModeratorId());
        }
        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return toRead(roleReviewCompliant);
    }

    private RoleReviewCompliant getRoleReviewCompliantRequired(UUID id) {
        return roleReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, id);
        });
    }

    public void deleteRoleReviewCompliant(UUID id) {
        roleReviewCompliantRepository.delete(getRoleReviewCompliantRequired(id));
    }
}
