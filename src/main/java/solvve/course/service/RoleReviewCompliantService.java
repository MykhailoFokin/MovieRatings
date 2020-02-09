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
public class RoleReviewCompliantService {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RoleReviewService roleReviewService;

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

    public RoleReviewCompliantReadDTO putRoleReviewCompliant(UUID id, RoleReviewCompliantPutDTO put) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewCompliantRequired(id);

        translationService.putEntity(put, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    @Transactional(readOnly = true)
    public List<RoleReviewCompliantReadDTO> getRoleReviewRoleReviewCompliant(UUID roleReviewId) {
        List<RoleReviewCompliant> roleReviewCompliantList = getRoleReviewRoleReviewCompliantsRequired(roleReviewId);
        return roleReviewCompliantList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public RoleReviewCompliantReadDTO createRoleReviewRoleReviewCompliant(UUID roleReviewId,
                                                                          RoleReviewCompliantCreateDTO create) {
        RoleReview roleReview = translationService.ReadDTOtoEntity(roleReviewService.getRoleReview(roleReviewId));

        RoleReviewCompliant roleReviewCompliant = translationService.toEntity(create);
        roleReviewCompliant.setRoleReviewId(roleReview);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    public RoleReviewCompliantReadDTO patchRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id,
                                                                         RoleReviewCompliantPatchDTO patch) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewRoleReviewCompliantRequired(roleReviewId, id);

        translationService.patchEntity(patch, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    public void deleteRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id) {
        roleReviewCompliantRepository.delete(getRoleReviewRoleReviewCompliantRequired(roleReviewId, id));
    }

    public RoleReviewCompliantReadDTO putRoleReviewRoleReviewCompliant(UUID roleReviewId, UUID id,
                                                                       RoleReviewCompliantPutDTO put) {
        RoleReviewCompliant roleReviewCompliant = getRoleReviewRoleReviewCompliantRequired(roleReviewId, id);

        translationService.putEntity(put, roleReviewCompliant);

        roleReviewCompliant = roleReviewCompliantRepository.save(roleReviewCompliant);
        return translationService.toRead(roleReviewCompliant);
    }

    private RoleReviewCompliant getRoleReviewCompliantRequired(UUID id) {
        return roleReviewCompliantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleReviewCompliant.class, id);
        });
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
