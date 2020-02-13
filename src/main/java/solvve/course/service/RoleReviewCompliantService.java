package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReviewCompliant;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewCompliantRepository;

import java.util.UUID;

@Service
public class RoleReviewCompliantService {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(UUID id) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);
        return translationService.toRead(roleReviewCompliant);
    }

    public RoleReviewCompliantReadDTO createRoleReviewCompliant(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = translationService.toEntity(create);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(UUID id, RoleReviewCompliantPatchDTO patch) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);

        translationService.patchEntity(patch, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    public void deleteRoleReviewCompliant(UUID id) {
        roleReviewCompliantRepository.delete(getRoleReviewCompliantRequired(id));
    }

    public RoleReviewCompliantReadDTO updateRoleReviewCompliant(UUID id, RoleReviewCompliantPutDTO put) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);

        translationService.updateEntity(put, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    private RoleReviewCompliant getRoleReviewCompliantRequired(UUID id) {
        return roleReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, id);
        });
    }
}
