package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewCompliantRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleReviewRoleReviewCompliantService extends AbstractService {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Transactional(readOnly = true)
    public List<RoleReviewCompliantReadDTO> getRoleReviewRoleReviewCompliant(UUID roleReviewId) {
        List<RoleReviewCompliant> roleReviewCompliants = getRoleReviewRoleReviewCompliantsRequired(roleReviewId);
        return roleReviewCompliants.stream().map(e ->
                translationService.translate(e, RoleReviewCompliantReadDTO.class)).collect(Collectors.toList());
    }

    public RoleReviewCompliantReadDTO createRoleReviewRoleReviewCompliant(UUID roleReviewId,
                                                                          RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = translationService.translate(create, RoleReviewCompliant.class);
        roleReviewCompliant.setRoleReview(repositoryHelper.getReferenceIfExists(RoleReview.class, roleReviewId));
        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);

        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    public RoleReviewCompliantReadDTO patchRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id,
                                                                         RoleReviewCompliantPatchDTO patch) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewRoleReviewCompliantRequired(roleReviewId, id);
        translationService.map(patch, roleReviewCompliant);
        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);

        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    public void deleteRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id) {
        roleReviewCompliantRepository.delete(getRoleReviewRoleReviewCompliantRequired(roleReviewId, id));
    }

    public RoleReviewCompliantReadDTO updateRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id,
                                                                          RoleReviewCompliantPutDTO put) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewRoleReviewCompliantRequired(roleReviewId, id);
        translationService.updateEntity(put, roleReviewCompliant);
        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);

        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    private RoleReviewCompliant getRoleReviewRoleReviewCompliantRequired(UUID roleReviewId, UUID id) {
        return roleReviewCompliantRepository.findByRoleReviewIdAndId(roleReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, roleReviewId, id);
        });
    }

    private List<RoleReviewCompliant> getRoleReviewRoleReviewCompliantsRequired(UUID roleReviewId) {
        return roleReviewCompliantRepository.findByRoleReviewIdOrderById(roleReviewId).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, roleReviewId);
        });
    }
}
