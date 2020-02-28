package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.CrewType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewTypeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public void deleteCrewType(UUID id) {
        crewTypeRepository.delete(getCrewTypeRequired(id));
    }

    public CrewTypeReadDTO updateCrewType(UUID id, CrewTypePutDTO put) {
        CrewType crewType = getCrewTypeRequired(id);

        translationService.updateEntity(put, crewType);

        crewType = crewTypeRepository.save(crewType);
        return translationService.toRead(crewType);
    }

    public List<CrewTypeReadDTO> getCrewTypes(CrewTypeFilter filter) {
        List<CrewType> crewTypes = crewTypeRepository.findByFilter(filter);
        return crewTypes.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    private CrewType getCrewTypeRequired(UUID id) {
        return crewTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(CrewType.class, id);
        });
    }
}
