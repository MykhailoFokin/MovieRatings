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
    private PortalUserService portalUserService;

    @Transactional(readOnly = true)
    public VisitReadExtendedDTO getVisit(UUID id) {
        Visit visit = getVisitRequired(id);
        return toReadExtended(visit);
    }

    private VisitReadExtendedDTO toReadExtended(Visit visit) {
        VisitReadExtendedDTO dto = new VisitReadExtendedDTO();
        dto.setId(visit.getId());
        dto.setMasterId(toRead(visit.getMasterId()));
        dto.setUserId(toRead(visit.getUserId()));
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(VisitStatus.FINISHED);
        return dto;
    }

    public VisitReadExtendedDTO createVisit(VisitCreateDTO create) {
        Visit visit = new Visit();
        visit.setUserId(create.getUserId());
        visit.setMasterId(create.getMasterId());
        visit.setStartAt(create.getStartAt());
        visit.setFinishAt(create.getFinishAt());
        visit.setStatus(create.getStatus());

        visit = visitRepository.save(visit);
        return toReadExtended(visit);
    }

    private VisitReadDTO toRead(Visit visit) {
        VisitReadDTO dto = new VisitReadDTO();
        dto.setId(visit.getId());
        dto.setMasterId(visit.getMasterId().getId());
        dto.setUserId(visit.getUserId().getId());
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(VisitStatus.FINISHED);
        return dto;
    }

    public VisitReadExtendedDTO patchVisit(UUID id, VisitPatchDTO patch) {
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
        return toReadExtended(visit);
    }

    private Visit getVisitRequired(UUID id) {
        return visitRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Visit.class, id);
        });
    }

    public void deleteVisit(UUID id) {
        visitRepository.delete(getVisitRequired(id));
    }

    private PortalUserReadDTO toRead(PortalUser portalUser) {
        PortalUserReadDTO dto = new PortalUserReadDTO();
        dto.setId(portalUser.getId());
        dto.setLogin(portalUser.getLogin());
        dto.setSurname(portalUser.getSurname());
        dto.setName(portalUser.getName());
        dto.setMiddleName(portalUser.getMiddleName());
        dto.setUserType(portalUser.getUserType());
        dto.setUserConfidence(portalUser.getUserConfidence());
        return dto;
    }

    private MasterReadDTO toRead(Master master) {
        MasterReadDTO dto = new MasterReadDTO();
        dto.setId(master.getId());
        dto.setName(master.getName());
        dto.setPhone(master.getPhone());
        dto.setAbout(master.getAbout());
        return dto;
    }
}
