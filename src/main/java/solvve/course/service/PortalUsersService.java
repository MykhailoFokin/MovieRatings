package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUsers;
import solvve.course.dto.PortalUsersCreateDTO;
import solvve.course.dto.PortalUsersReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUsersRepository;

import java.util.UUID;

@Service
public class PortalUsersService {

    @Autowired
    private PortalUsersRepository portalUsersRepository;

    @Transactional(readOnly = true)
    public PortalUsersReadDTO getPortalUsers(UUID id) {
        PortalUsers portalUsers = portalUsersRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(PortalUsers.class, id);
        });
        return toRead(portalUsers);
    }

    private PortalUsersReadDTO toRead(PortalUsers portalUsers) {
        PortalUsersReadDTO dto = new PortalUsersReadDTO();
        dto.setId(portalUsers.getId());
        dto.setLogin(portalUsers.getLogin());
        dto.setSurname(portalUsers.getSurname());
        dto.setName(portalUsers.getName());
        dto.setMiddleName(portalUsers.getMiddleName());
        dto.setUserType(portalUsers.getUserType());
        dto.setUserConfidence(portalUsers.getUserConfidence());
        return dto;
    }

    public PortalUsersReadDTO createPortalUsers(PortalUsersCreateDTO create) {
        PortalUsers portalUsers = new PortalUsers();
        portalUsers.setLogin(create.getLogin());
        portalUsers.setSurname(create.getSurname());
        portalUsers.setName(create.getName());
        portalUsers.setMiddleName(create.getMiddleName());
        portalUsers.setUserType(create.getUserType());
        portalUsers.setUserConfidence(create.getUserConfidence());

        portalUsers = portalUsersRepository.save(portalUsers);
        return toRead(portalUsers);
    }
}
