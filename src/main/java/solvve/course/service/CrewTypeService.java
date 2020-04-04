package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.CrewType;
import solvve.course.dto.*;
import solvve.course.repository.CrewTypeRepository;

import java.util.UUID;

@Service
public class CrewTypeService extends AbstractService {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Transactional(readOnly = true)
    public CrewTypeReadDTO getCrewType(UUID id) {
        CrewType crewType = repositoryHelper.getByIdRequired(CrewType.class, id);
        return translationService.translate(crewType, CrewTypeReadDTO.class);
    }

    public CrewTypeReadDTO createCrewType(CrewTypeCreateDTO create) {
        CrewType crewType = translationService.translate(create, CrewType.class);

        crewType = crewTypeRepository.save(crewType);
        return translationService.translate(crewType, CrewTypeReadDTO.class);
    }

    public CrewTypeReadDTO patchCrewType(UUID id, CrewTypePatchDTO patch) {
        CrewType crewType = repositoryHelper.getByIdRequired(CrewType.class, id);

        translationService.map(patch, crewType);

        crewType = crewTypeRepository.save(crewType);
        return translationService.translate(crewType, CrewTypeReadDTO.class);
    }

    public void deleteCrewType(UUID id) {
        crewTypeRepository.delete(repositoryHelper.getByIdRequired(CrewType.class, id));
    }

    public CrewTypeReadDTO updateCrewType(UUID id, CrewTypePutDTO put) {
        CrewType crewType = repositoryHelper.getByIdRequired(CrewType.class, id);

        translationService.updateEntity(put, crewType);

        crewType = crewTypeRepository.save(crewType);
        return translationService.translate(crewType, CrewTypeReadDTO.class);
    }

    public PageResult<CrewTypeReadDTO> getCrewTypes(CrewTypeFilter filter, Pageable pageable) {
        Page<CrewType> crewTypes = crewTypeRepository.findByFilter(filter, pageable);
        return translationService.toPageResult(crewTypes, CrewTypeReadDTO.class);
    }
}
