package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Crew;
import solvve.course.dto.CrewCreateDTO;
import solvve.course.dto.CrewPatchDTO;
import solvve.course.dto.CrewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewRepository;

import java.util.UUID;

@Service
public class CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Transactional(readOnly = true)
    public CrewReadDTO getCrew(UUID id) {
        Crew crew = getCrewRequired(id);
        return toRead(crew);
    }

    private CrewReadDTO toRead(Crew crew) {
        CrewReadDTO dto = new CrewReadDTO();
        dto.setId(crew.getId());
        dto.setPersonId(crew.getPersonId());
        dto.setCrewType(crew.getCrewType());
        dto.setDescription(crew.getDescription());
        dto.setMovieId(crew.getMovieId());
        return dto;
    }

    public CrewReadDTO createCrew(CrewCreateDTO create) {
        Crew crew = new Crew();
        crew.setPersonId(create.getPersonId());
        crew.setCrewType(create.getCrewType());
        crew.setDescription(create.getDescription());
        crew.setMovieId(create.getMovieId());

        crew = crewRepository.save(crew);
        return toRead(crew);
    }

    public CrewReadDTO patchCrew(UUID id, CrewPatchDTO patch) {
        Crew crew = getCrewRequired(id);

        if (patch.getMovieId()!=null) {
            crew.setMovieId(patch.getMovieId());
        }
        if (patch.getPersonId()!=null) {
            crew.setPersonId(patch.getPersonId());
        }
        if (patch.getCrewType()!=null) {
            crew.setCrewType(patch.getCrewType());
        }
        if (patch.getDescription()!=null) {
            crew.setDescription(patch.getDescription());
        }
        crew = crewRepository.save(crew);
        return toRead(crew);
    }

    private Crew getCrewRequired(UUID id) {
        return crewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Crew.class, id);
        });
    }

    public void deleteCrew(UUID id) {
        crewRepository.delete(getCrewRequired(id));
    }
}
