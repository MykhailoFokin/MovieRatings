package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewRepository;

import java.util.UUID;

@Service
public class RoleReviewService {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleReviewReadDTO getRoleReview(UUID id) {
        RoleReview roleReview = getRoleReviewRequired(id);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public RoleReviewReadDTO createRoleReview(RoleReviewCreateDTO create) {
        RoleReview roleReview = translationService.translate(create, RoleReview.class);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public RoleReviewReadDTO patchRoleReview(UUID id, RoleReviewPatchDTO patch) {
        RoleReview roleReview = getRoleReviewRequired(id);

        translationService.map(patch, roleReview);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public void deleteRoleReview(UUID id) {
        roleReviewRepository.delete(getRoleReviewRequired(id));
    }

    public RoleReviewReadDTO updateRoleReview(UUID id, RoleReviewPutDTO put) {
        RoleReview roleReview = getRoleReviewRequired(id);

        translationService.updateEntity(put, roleReview);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    private RoleReview getRoleReviewRequired(UUID id) {
        return roleReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReview.class, id);
        });
    }
}
