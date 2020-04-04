package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.domain.RoleReview;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortalUserRoleReviewService extends AbstractService {

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Transactional(readOnly = true)
    public List<RoleReviewReadDTO> getPortalUserRoleReview(UUID portalUserId) {
        List<RoleReview> roleReviews = getPortalUserRoleReviewsRequired(portalUserId);
        return roleReviews.stream().map(e ->
                translationService.translate(e, RoleReviewReadDTO.class)).collect(Collectors.toList());
    }

    public RoleReviewReadDTO createPortalUserRoleReview(UUID portalUserId,
                                                        RoleReviewCreateDTO create) {
        RoleReview roleReview = translationService.translate(create, RoleReview.class);
        roleReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, portalUserId));
        roleReview = roleReviewRepository.save(roleReview);

        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public RoleReviewReadDTO patchPortalUserRoleReview(UUID portalUserId, UUID id,
                                                       RoleReviewPatchDTO patch) {
        RoleReview roleReview = getPortalUserRoleReviewRequired(portalUserId, id);
        translationService.map(patch, roleReview);
        roleReview = roleReviewRepository.save(roleReview);

        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    public void deletePortalUserRoleReview(UUID portalUserId, UUID id) {
        roleReviewRepository.delete(getPortalUserRoleReviewRequired(portalUserId, id));
    }

    public RoleReviewReadDTO updatePortalUserRoleReview(UUID portalUserId, UUID id,
                                                        RoleReviewPutDTO put) {
        RoleReview roleReview = getPortalUserRoleReviewRequired(portalUserId, id);
        translationService.updateEntity(put, roleReview);
        roleReview = roleReviewRepository.save(roleReview);

        return translationService.translate(roleReview, RoleReviewReadDTO.class);
    }

    private RoleReview getPortalUserRoleReviewRequired(UUID portalUserId, UUID id) {
        return roleReviewRepository.findByPortalUserIdAndId(portalUserId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReview.class, portalUserId, id);
        });
    }

    private List<RoleReview> getPortalUserRoleReviewsRequired(UUID portalUserId) {
        return roleReviewRepository.findByPortalUserIdOrderById(portalUserId).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReview.class, portalUserId);
        });
    }
}
