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
import solvve.course.repository.RepositoryHelper;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleReviewSpoilerDataService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Transactional(readOnly = true)
    public List<RoleSpoilerDataReadDTO> getRoleReviewSpoilerData(UUID roleReviewId) {
        List<RoleSpoilerData> roleSpoilerDatas =
                roleSpoilerDataRepository.findByRoleReviewIdOrderByIdAsc(roleReviewId).orElseThrow(() -> {
                    throw new EntityNotFoundException(RoleSpoilerData.class, roleReviewId);
                });

        return roleSpoilerDatas.stream().map(e ->
                translationService.translate(e, RoleSpoilerDataReadDTO.class)).collect(Collectors.toList());
    }

    public RoleSpoilerDataReadDTO createRoleReviewSpoilerData(UUID roleReviewId, RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = translationService.translate(create, RoleSpoilerData.class);
        roleSpoilerData.setRoleReview(repositoryHelper.getReferenceIfExists(RoleReview.class, roleReviewId));
        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);

        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public RoleSpoilerDataReadDTO patchRoleReviewSpoilerData(UUID roleReviewId, UUID id,
                                                             RoleSpoilerDataPatchDTO patch) {
        RoleSpoilerData roleSpoilerData = getRoleReviewSpoilerDataRequired(roleReviewId, id);
        translationService.map(patch, roleSpoilerData);
        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);

        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public void deleteRoleReviewSpoilerData(UUID roleReviewId, UUID id) {
        roleSpoilerDataRepository.delete(getRoleReviewSpoilerDataRequired(roleReviewId, id));
    }

    public RoleSpoilerDataReadDTO updateRoleReviewSpoilerData(UUID roleReviewId, UUID id, RoleSpoilerDataPutDTO put) {
        RoleSpoilerData roleSpoilerData = getRoleReviewSpoilerDataRequired(roleReviewId, id);
        translationService.updateEntity(put, roleSpoilerData);
        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);

        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    private RoleSpoilerData getRoleReviewSpoilerDataRequired(UUID roleReviewId, UUID id) {
        return roleSpoilerDataRepository.findByRoleReviewIdAndId(roleReviewId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleSpoilerData.class, roleReviewId, id);
        });
    }
}
