package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@Service
public class PortalUserService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    public PortalUserReadDTO getPortalUser(UUID id) {
        PortalUser portalUser = getPortalUserRequired(id);
        return toRead(portalUser);
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

    public PortalUserReadDTO createPortalUser(PortalUserCreateDTO create) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLogin(create.getLogin());
        portalUser.setSurname(create.getSurname());
        portalUser.setName(create.getName());
        portalUser.setMiddleName(create.getMiddleName());
        portalUser.setUserType(create.getUserType());
        portalUser.setUserConfidence(create.getUserConfidence());

        portalUser = portalUserRepository.save(portalUser);
        return toRead(portalUser);
    }

    public PortalUserReadDTO patchPortalUser(UUID id, PortalUserPatchDTO patch) {
        PortalUser portalUser = getPortalUserRequired(id);

        if (patch.getLogin()!=null) {
            portalUser.setLogin(patch.getLogin());
        }
        if (patch.getSurname()!=null) {
            portalUser.setSurname(patch.getSurname());
        }
        if (patch.getName()!=null) {
            portalUser.setName(patch.getName());
        }
        if (patch.getMiddleName()!=null) {
            portalUser.setMiddleName(patch.getMiddleName());
        }
        if (patch.getUserType()!=null) {
            portalUser.setUserType(patch.getUserType());
        }
        if (patch.getUserConfidence()!=null) {
            portalUser.setUserConfidence(patch.getUserConfidence());
        }
        portalUser = portalUserRepository.save(portalUser);
        return toRead(portalUser);
    }

    private PortalUser getPortalUserRequired(UUID id) {
        return portalUserRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(PortalUser.class, id);
        });
    }

    public void deletePortalUser(UUID id) {
        portalUserRepository.delete(getPortalUserRequired(id));
    }
}
