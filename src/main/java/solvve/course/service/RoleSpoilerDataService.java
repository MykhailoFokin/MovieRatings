package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.UUID;

@Service
public class RoleSpoilerDataService extends AbstractService {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Transactional(readOnly = true)
    public RoleSpoilerDataReadDTO getRoleSpoilerData(UUID id) {
        RoleSpoilerData roleSpoilerData = repositoryHelper.getByIdRequired(RoleSpoilerData.class, id);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public RoleSpoilerDataReadDTO createRoleSpoilerData(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = translationService.translate(create, RoleSpoilerData.class);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public RoleSpoilerDataReadDTO patchRoleSpoilerData(UUID id, RoleSpoilerDataPatchDTO patch) {
        RoleSpoilerData roleSpoilerData = repositoryHelper.getByIdRequired(RoleSpoilerData.class, id);

        translationService.map(patch, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public void deleteRoleSpoilerData(UUID id) {
        roleSpoilerDataRepository.delete(repositoryHelper.getByIdRequired(RoleSpoilerData.class, id));
    }

    public RoleSpoilerDataReadDTO updateRoleSpoilerData(UUID id, RoleSpoilerDataPutDTO put) {
        RoleSpoilerData roleSpoilerData = repositoryHelper.getByIdRequired(RoleSpoilerData.class, id);

        translationService.updateEntity(put, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }
}
