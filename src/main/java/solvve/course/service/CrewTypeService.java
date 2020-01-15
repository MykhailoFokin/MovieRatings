package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewTypeRepository;

import java.util.UUID;

@Service
public class CrewTypeService {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Transactional(readOnly = true)
    public CrewTypeReadDTO getCrewType(UUID id) {
        CrewType crewType = getCrewTypeRequired(id);
        return toRead(crewType);
    }

    private CrewTypeReadDTO toRead(CrewType crewType) {
        CrewTypeReadDTO dto = new CrewTypeReadDTO();
        dto.setId(crewType.getId());
        dto.setName(crewType.getName());
        return dto;
    }

    public CrewTypeReadDTO createCrewType(CrewTypeCreateDTO create) {
        CrewType crewType = new CrewType();
        crewType.setName(create.getName());

        crewType = crewTypeRepository.save(crewType);
        return toRead(crewType);
    }

    public CrewTypeReadDTO patchCrewType(UUID id, CrewTypePatchDTO patch) {
        CrewType crewType = getCrewTypeRequired(id);

        if (patch.getName()!=null) {
            crewType.setName(patch.getName());
        }
        crewType = crewTypeRepository.save(crewType);
        return toRead(crewType);
    }

    private CrewType getCrewTypeRequired(UUID id) {
        return crewTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(CrewType.class, id);
        });
    }

    public void deleteCrewType(UUID id) {
        crewTypeRepository.delete(getCrewTypeRequired(id));
    }
}
