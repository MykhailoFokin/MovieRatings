package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypePutDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewTypeRepository;

import java.util.UUID;

@Service
public class CrewTypeService {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public CrewTypeReadDTO getCrewType(UUID id) {
        CrewType crewType = getCrewTypeRequired(id);
        return translationService.toRead(crewType);
    }

    public CrewTypeReadDTO createCrewType(CrewTypeCreateDTO create) {
        CrewType crewType = translationService.toEntity(create);

        crewType = crewTypeRepository.save(crewType);
        return translationService.toRead(crewType);
    }

    public CrewTypeReadDTO patchCrewType(UUID id, CrewTypePatchDTO patch) {
        CrewType crewType = getCrewTypeRequired(id);

        translationService.patchEntity(patch, crewType);

        crewType = crewTypeRepository.save(crewType);
        return translationService.toRead(crewType);
    }

    private CrewType getCrewTypeRequired(UUID id) {
        return crewTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(CrewType.class, id);
        });
    }

    public void deleteCrewType(UUID id) {
        crewTypeRepository.delete(getCrewTypeRequired(id));
    }

    public CrewTypeReadDTO putCrewType(UUID id, CrewTypePutDTO put) {
        CrewType crewType = getCrewTypeRequired(id);

        translationService.putEntity(put, crewType);

        crewType = crewTypeRepository.save(crewType);
        return translationService.toRead(crewType);
    }
}
