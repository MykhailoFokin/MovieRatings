package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewRepository;

import java.util.UUID;

@Service
public class RoleReviewService {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Transactional(readOnly = true)
    public RoleReviewReadDTO getRoleReview(UUID id) {
        RoleReview roleReview = getRoleReviewRequired(id);
        return toRead(roleReview);
    }

    private RoleReviewReadDTO toRead(RoleReview roleReview) {
        RoleReviewReadDTO dto = new RoleReviewReadDTO();
        dto.setId(roleReview.getId());
        dto.setUserId(roleReview.getUserId());
        dto.setRoleId(roleReview.getRoleId());
        dto.setTextReview(roleReview.getTextReview());
        dto.setModeratedStatus(roleReview.getModeratedStatus());
        dto.setModeratorId(roleReview.getModeratorId());
        return dto;
    }

    public RoleReviewReadDTO createRoleReview(RoleReviewCreateDTO create) {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(create.getUserId());
        roleReview.setRoleId(create.getRoleId());
        roleReview.setTextReview(create.getTextReview());
        roleReview.setModeratedStatus(create.getModeratedStatus());
        roleReview.setModeratorId(create.getModeratorId());

        roleReview = roleReviewRepository.save(roleReview);
        return toRead(roleReview);
    }

    public RoleReviewReadDTO patchRoleReview(UUID id, RoleReviewPatchDTO patch) {
        RoleReview roleReview = getRoleReviewRequired(id);

        if (patch.getUserId()!=null) {
            roleReview.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReview.setRoleId(patch.getRoleId());
        }
        if (patch.getTextReview()!=null) {
            roleReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReview.setModeratorId(patch.getModeratorId());
        }
        roleReview = roleReviewRepository.save(roleReview);
        return toRead(roleReview);
    }

    private RoleReview getRoleReviewRequired(UUID id) {
        return roleReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReview.class, id);
        });
    }

    public void deleteRoleReview(UUID id) {
        roleReviewRepository.delete(getRoleReviewRequired(id));
    }
}
