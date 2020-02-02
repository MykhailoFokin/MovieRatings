package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Master;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MasterRepository;

import java.util.UUID;

@Service
public class MasterService {

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public MasterReadExtendedDTO getMaster(UUID id) {
        Master master = getMasterRequired(id);
        return translationService.toReadExtended(master);
    }

    public MasterReadDTO createMaster(MasterCreateDTO create) {
        Master master = translationService.toEntity(create);

        master = masterRepository.save(master);
        return translationService.toRead(master);
    }

    public MasterReadDTO patchMaster(UUID id, MasterPatchDTO patch) {
        Master master = getMasterRequired(id);

        translationService.patchEntity(patch, master);

        master = masterRepository.save(master);
        return translationService.toRead(master);
    }

    private Master getMasterRequired(UUID id) {
        return masterRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Master.class, id);
        });
    }

    public void deleteMaster(UUID id) {
        masterRepository.delete(getMasterRequired(id));
    }

    public MasterReadDTO putMaster(UUID id, MasterPutDTO put) {
        Master master = getMasterRequired(id);

        translationService.putEntity(put, master);

        master = masterRepository.save(master);
        return translationService.toRead(master);
    }
}
