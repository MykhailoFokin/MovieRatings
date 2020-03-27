package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Visit;
import solvve.course.domain.VisitStatus;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.VisitRepository;

import java.util.UUID;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public VisitReadExtendedDTO getVisit(UUID id) {
        Visit visit = getVisitRequired(id);
        return translationService.translate(visit, VisitReadExtendedDTO.class);
    }

    public VisitReadDTO createVisit(VisitCreateDTO create) {
        Visit visit = translationService.translate(create, Visit.class);
        visit.setStatus(VisitStatus.SCHEDULED);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public VisitReadDTO patchVisit(UUID id, VisitPatchDTO patch) {
        Visit visit = getVisitRequired(id);

        translationService.map(patch, visit);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public void deleteVisit(UUID id) {
        visitRepository.delete(getVisitRequired(id));
    }

    public VisitReadDTO updateVisit(UUID id, VisitPutDTO put) {
        Visit visit = getVisitRequired(id);

        translationService.updateEntity(put, visit);

        visit = visitRepository.save(visit);
        return translationService.translate(visit, VisitReadDTO.class);
    }

    public PageResult<VisitReadDTO> getVisits(VisitFilter filter, Pageable pageable) {
        Page<Visit> visits = visitRepository.findByFilter(filter, pageable);
        return translationService.toPageResult(visits, VisitReadDTO.class);
    }

    private Visit getVisitRequired(UUID id) {
        return visitRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Visit.class, id);
        });
    }
}
