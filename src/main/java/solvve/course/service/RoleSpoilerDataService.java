package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.UUID;

@Service
public class RoleSpoilerDataService {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public RoleSpoilerDataReadDTO getRoleSpoilerData(UUID id) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public RoleSpoilerDataReadDTO createRoleSpoilerData(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = translationService.translate(create, RoleSpoilerData.class);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public RoleSpoilerDataReadDTO patchRoleSpoilerData(UUID id, RoleSpoilerDataPatchDTO patch) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);

        translationService.map(patch, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    public void deleteRoleSpoilerData(UUID id) {
        roleSpoilerDataRepository.delete(getRoleSpoilerDataRequired(id));
    }

    public RoleSpoilerDataReadDTO updateRoleSpoilerData(UUID id, RoleSpoilerDataPutDTO put) {
        RoleSpoilerData roleSpoilerData = getRoleSpoilerDataRequired(id);

        translationService.updateEntity(put, roleSpoilerData);

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return translationService.translate(roleSpoilerData, RoleSpoilerDataReadDTO.class);
    }

    private RoleSpoilerData getRoleSpoilerDataRequired(UUID id) {
        return roleSpoilerDataRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleSpoilerData.class, id);
        });
    }
}
