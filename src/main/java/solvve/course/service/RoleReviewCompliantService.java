package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
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
        RoleReviewCompliant roleReviewCompliant = roleReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, id);
        });
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
}
