package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Visit;
import solvve.course.domain.VisitStatus;
import solvve.course.dto.*;
import solvve.course.repository.VisitRepository;

import java.util.UUID;

@Service
public class VisitService extends AbstractService {

    @Autowired
    private VisitRepository visitRepository;

    @Transactional(readOnly = true)
    public VisitReadExtendedDTO getVisit(UUID id) {
        Visit visit = repositoryHelper.getByIdRequired(Visit.class, id);
        return translationService.translate(visit, VisitReadExtendedDTO.class);
    }

    public VisitReadDTO createVisit(VisitCreateDTO create) {
        Visit visit = translationService.translate(create, Visit.class);
        visit.setStatus(VisitStatus.SCHEDULED);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public VisitReadDTO patchVisit(UUID id, VisitPatchDTO patch) {
        Visit visit = repositoryHelper.getByIdRequired(Visit.class, id);

        translationService.map(patch, visit);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public void deleteVisit(UUID id) {
        visitRepository.delete(repositoryHelper.getByIdRequired(Visit.class, id));
    }

    public VisitReadDTO updateVisit(UUID id, VisitPutDTO put) {
        Visit visit = repositoryHelper.getByIdRequired(Visit.class, id);

        translationService.updateEntity(put, visit);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public PageResult<VisitReadDTO> getVisits(VisitFilter filter, Pageable pageable) {
        Page<Visit> visits = visitRepository.findByFilter(filter, pageable);
        return translationService.toPageResult(visits, VisitReadDTO.class);
    }
}
