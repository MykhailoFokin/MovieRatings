package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleReview;
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleSpoilerDataService {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RoleReviewService roleReviewService;

    @Transactional(readOnly = true)
    public RoleSpoilerDataReadDTO getRoleSpoilerData(UUID id) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);
        return translationService.toRead(roleSpoilerData);
    }

    public RoleSpoilerDataReadDTO createRoleSpoilerData(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = translationService.toEntity(create);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    public RoleSpoilerDataReadDTO patchRoleSpoilerData(UUID id, RoleSpoilerDataPatchDTO patch) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);

        translationService.patchEntity(patch, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    public void deleteRoleSpoilerData(UUID id) {
        roleSpoilerDataRepository.delete(getRoleSpoilerDataRequired(id));
    }

    public RoleSpoilerDataReadDTO updateRoleSpoilerData(UUID id, RoleSpoilerDataPutDTO put) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);

        translationService.updateEntity(put, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    @Transactional(readOnly = true)
    public List<RoleSpoilerDataReadDTO> getRoleReviewSpoilerData(UUID roleReviewId) {
        roleReviewService.getRoleReview(roleReviewId);

        List<RoleSpoilerData> roleSpoilerDataList =
                roleSpoilerDataRepository.findByRoleReviewIdOrderByIdAsc(roleReviewId);
        return roleSpoilerDataList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    public RoleSpoilerDataReadDTO createRoleReviewSpoilerData(UUID roleReviewId, RoleSpoilerDataCreateDTO create) {
        RoleReview roleReview = translationService.ReadDTOtoEntity(roleReviewService.getRoleReview(roleReviewId));

        RoleSpoilerData roleSpoilerData = translationService.toEntity(create);
        roleSpoilerData.setRoleReviewId(roleReview);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    public RoleSpoilerDataReadDTO patchRoleReviewSpoilerData(UUID roleReviewId, UUID id,
                                                             RoleSpoilerDataPatchDTO patch) {
        RoleSpoilerData roleSpoilerData = getRoleReviewSpoilerDataRequired(roleReviewId, id);

        translationService.patchEntity(patch, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    public void deleteRoleReviewSpoilerData(UUID roleReviewId, UUID id) {
        roleSpoilerDataRepository.delete(getRoleReviewSpoilerDataRequired(roleReviewId, id));
    }

    public RoleSpoilerDataReadDTO updateRoleReviewSpoilerData(UUID roleReviewId, UUID id, RoleSpoilerDataPutDTO put) {
        RoleSpoilerData roleSpoilerData = getRoleReviewSpoilerDataRequired(roleReviewId, id);

        translationService.updateEntity(put, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.toRead(roleSpoilerData);
    }

    private RoleSpoilerData getRoleSpoilerDataRequired(UUID id) {
        return roleSpoilerDataRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleSpoilerData.class, id);
        });
    }

    private RoleSpoilerData getRoleReviewSpoilerDataRequired(UUID roleReviewId, UUID id) {
        return roleSpoilerDataRepository.findByRoleReviewIdAndId(roleReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleSpoilerData.class, roleReviewId, id);
        });
    }
}
