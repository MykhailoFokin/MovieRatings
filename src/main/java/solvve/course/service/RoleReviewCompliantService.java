package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.repository.RoleReviewCompliantRepository;

import java.util.UUID;

@Service
public class RoleReviewCompliantService extends AbstractService {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Transactional(readOnly = true)
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(UUID id) {
        RoleReviewCompliant roleReviewCompliant = repositoryHelper.getByIdRequired(RoleReviewCompliant.class, id);
        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    public RoleReviewCompliantReadDTO createRoleReviewCompliant(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = translationService.translate(create, RoleReviewCompliant.class);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(UUID id, RoleReviewCompliantPatchDTO patch) {
        RoleReviewCompliant roleReviewCompliant = repositoryHelper.getByIdRequired(RoleReviewCompliant.class, id);

        translationService.map(patch, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }

    public void deleteRoleReviewCompliant(UUID id) {
        roleReviewCompliantRepository.delete(repositoryHelper.getByIdRequired(RoleReviewCompliant.class, id));
    }

    public RoleReviewCompliantReadDTO updateRoleReviewCompliant(UUID id, RoleReviewCompliantPutDTO put) {
        RoleReviewCompliant roleReviewCompliant = repositoryHelper.getByIdRequired(RoleReviewCompliant.class, id);

        translationService.updateEntity(put, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.translate(roleReviewCompliant, RoleReviewCompliantReadDTO.class);
    }
}
