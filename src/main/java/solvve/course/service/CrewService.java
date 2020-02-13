package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Crew;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public CrewReadExtendedDTO getCrew(UUID id) {
        Crew crew = getCrewRequired(id);
        return translationService.toReadExtended(crew);
    }

    public CrewReadDTO createCrew(CrewCreateDTO create) {
        Crew crew = translationService.toEntity(create);

        crew = crewRepository.save(crew);
        return translationService.toRead(crew);
    }

    public CrewReadDTO patchCrew(UUID id, CrewPatchDTO patch) {
        Crew crew = getCrewRequired(id);

        translationService.patchEntity(patch, crew);

        crew = crewRepository.save(crew);
        return translationService.toRead(crew);
    }

    public void deleteCrew(UUID id) {
        crewRepository.delete(getCrewRequired(id));
    }

    public CrewReadDTO updateCrew(UUID id, CrewPutDTO put) {
        Crew crew = getCrewRequired(id);

        translationService.updateEntity(put, crew);

        crew = crewRepository.save(crew);
        return translationService.toRead(crew);
    }

    public List<CrewReadDTO> getCrews(CrewFilter filter) {
        List<Crew> crews = crewRepository.findByFilter(filter);
        return crews.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    private Crew getCrewRequired(UUID id) {
        return crewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Crew.class, id);
        });
    }
}
