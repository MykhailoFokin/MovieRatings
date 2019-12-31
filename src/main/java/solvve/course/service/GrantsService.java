package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Grants;
import solvve.course.dto.GrantsCreateDTO;
import solvve.course.dto.GrantsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GrantsRepository;

import java.util.UUID;

@Service
public class GrantsService {

    @Autowired
    private GrantsRepository grantsRepository;

    @Transactional(readOnly = true)
    public GrantsReadDTO getGrants(UUID id) {
        Grants grants = grantsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Grants.class, id);
        });
        return toRead(grants);
    }

    private GrantsReadDTO toRead(Grants grants) {
        GrantsReadDTO dto = new GrantsReadDTO();
        dto.setId(grants.getId());
        dto.setUserTypeId(grants.getUserTypeId());
        dto.setUserPermission(grants.getUserPermission());
        dto.setObjectName(grants.getObjectName());
        dto.setGrantedBy(grants.getGrantedBy());
        return dto;
    }

    public GrantsReadDTO createGrants(GrantsCreateDTO create) {
        Grants grants = new Grants();
        grants.setUserTypeId(create.getUserTypeId());
        grants.setUserPermission(create.getUserPermission());
        grants.setObjectName(create.getObjectName());
        grants.setGrantedBy(create.getGrantedBy());

        grants = grantsRepository.save(grants);
        return toRead(grants);
    }
}
