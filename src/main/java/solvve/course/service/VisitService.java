package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Visit;
import solvve.course.domain.VisitStatus;
import solvve.course.dto.VisitCreateDTO;
import solvve.course.dto.VisitPatchDTO;
import solvve.course.dto.VisitReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.VisitRepository;

import java.util.UUID;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    @Transactional(readOnly = true)
    public VisitReadDTO getVisit(UUID id) {
        Visit visit = getVisitRequired(id);
        return toRead(visit);
    }

    private VisitReadDTO toRead(Visit visit) {
        VisitReadDTO dto = new VisitReadDTO();
        dto.setId(visit.getId());
        dto.setMasterId(visit.getMasterId());
        dto.setUserId(visit.getUserId());
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(VisitStatus.FINISHED);
        return dto;
    }

    public VisitReadDTO createVisit(VisitCreateDTO create) {
        Visit visit = new Visit();
        visit.setUserId(create.getUserId());
        visit.setMasterId(create.getMasterId());
        visit.setStartAt(create.getStartAt());
        visit.setFinishAt(create.getFinishAt());
        visit.setStatus(create.getStatus());

        visit = visitRepository.save(visit);
        return toRead(visit);
    }

    public VisitReadDTO patchVisit(UUID id, VisitPatchDTO patch) {
        Visit visit = getVisitRequired(id);

        if (patch.getUserId()!=null) {
            visit.setUserId(patch.getUserId());
        }
        if (patch.getMasterId()!=null) {
            visit.setMasterId(patch.getMasterId());
        }
        if (patch.getStartAt()!=null) {
            visit.setStartAt(patch.getStartAt());
        }
        if (patch.getFinishAt()!=null) {
            visit.setFinishAt(patch.getFinishAt());
        }
        if (patch.getStatus()!=null) {
            visit.setStatus(patch.getStatus());
        }
        visit = visitRepository.save(visit);
        return toRead(visit);
    }

    private Visit getVisitRequired(UUID id) {
        return visitRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Visit.class, id);
        });
    }

    public void deleteVisit(UUID id) {
        visitRepository.delete(getVisitRequired(id));
    }
}
