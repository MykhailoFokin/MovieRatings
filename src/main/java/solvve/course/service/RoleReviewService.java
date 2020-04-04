package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.repository.RoleReviewRepository;

import java.util.UUID;

@Service
public class RoleReviewService extends AbstractService {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Transactional(readOnly = true)
    public RoleReviewReadDTO getRoleReview(UUID id) {
        RoleReview roleReview = repositoryHelper.getByIdRequired(RoleReview.class, id);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public RoleReviewReadDTO createRoleReview(RoleReviewCreateDTO create) {
        RoleReview roleReview = translationService.translate(create, RoleReview.class);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public RoleReviewReadDTO patchRoleReview(UUID id, RoleReviewPatchDTO patch) {
        RoleReview roleReview = repositoryHelper.getByIdRequired(RoleReview.class, id);

        translationService.map(patch, roleReview);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public void deleteRoleReview(UUID id) {
        roleReviewRepository.delete(repositoryHelper.getByIdRequired(RoleReview.class, id));
    }

    public RoleReviewReadDTO updateRoleReview(UUID id, RoleReviewPutDTO put) {
        RoleReview roleReview = repositoryHelper.getByIdRequired(RoleReview.class, id);

        translationService.updateEntity(put, roleReview);

        roleReview = roleReviewRepository.save(roleReview);
        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }
}
