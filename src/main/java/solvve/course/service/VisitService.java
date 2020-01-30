package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Master;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Visit;
import solvve.course.domain.VisitStatus;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.VisitRepository;

import java.util.Optional;
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
        return translationService.toReadExtended(visit);
    }

    public VisitReadDTO createVisit(VisitCreateDTO create) {
        Visit visit = translationService.toEntity(create);

        visit = visitRepository.save(visit);
        return translationService.toRead(visit);
    }

    public VisitReadDTO patchVisit(UUID id, VisitPatchDTO patch) {
        Visit visit = getVisitRequired(id);

        translationService.patchEntity(patch, visit);

        visit = visitRepository.save(visit);
        return translationService.toRead(visit);
    }

    public void deleteVisit(UUID id) {
        visitRepository.delete(getVisitRequired(id));
    }

    public VisitReadDTO putVisit(UUID id, VisitPutDTO put) {
        Visit visit = getVisitRequired(id);

        translationService.putEntity(put, visit);

        visit = visitRepository.save(visit);
        return translationService.toRead(visit);
    }

    private Visit getVisitRequired(UUID id) {
        return visitRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Visit.class, id);
        });
    }
}
