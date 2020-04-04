package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Crew;
import solvve.course.dto.*;
import solvve.course.repository.CrewRepository;

import java.util.UUID;

@Service
public class CrewService extends AbstractService {

    @Autowired
    private CrewRepository crewRepository;

    @Transactional(readOnly = true)
    public CrewReadExtendedDTO getCrew(UUID id) {
        Crew crew = repositoryHelper.getByIdRequired(Crew.class, id);
        return translationService.translate(crew, CrewReadExtendedDTO.class);
    }

    public CrewReadDTO createCrew(CrewCreateDTO create) {
        Crew crew = translationService.translate(create, Crew.class);

        crew = crewRepository.save(crew);
        return translationService.translate(crew, CrewReadDTO.class);
    }

    public CrewReadDTO patchCrew(UUID id, CrewPatchDTO patch) {
        Crew crew = repositoryHelper.getByIdRequired(Crew.class, id);

        translationService.map(patch, crew);

        crew = crewRepository.save(crew);
        return translationService.translate(crew, CrewReadDTO.class);
    }

    public void deleteCrew(UUID id) {
        crewRepository.delete(repositoryHelper.getByIdRequired(Crew.class, id));
    }

    public CrewReadDTO updateCrew(UUID id, CrewPutDTO put) {
        Crew crew = repositoryHelper.getByIdRequired(Crew.class, id);

        translationService.updateEntity(put, crew);

        crew = crewRepository.save(crew);
        return translationService.translate(crew, CrewReadDTO.class);
    }

    public PageResult<CrewReadDTO> getCrews(CrewFilter filter, Pageable pageable) {
        Page<Crew> crews = crewRepository.findByFilter(filter, pageable);
        return translationService.toPageResult(crews, CrewReadDTO.class);
    }
}
