package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@Service
public class PortalUserService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    public PortalUserReadDTO getPortalUsers(UUID id) {
        PortalUser portalUser = portalUserRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(PortalUser.class, id);
        });
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

    public PortalUserReadDTO createPortalUsers(PortalUserCreateDTO create) {
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
}
