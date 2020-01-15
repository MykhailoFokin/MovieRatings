package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Grants;
import solvve.course.domain.UserTypes;
import solvve.course.dto.GrantsCreateDTO;
import solvve.course.dto.GrantsPatchDTO;
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
        Grants grants = getGrantsRequired(id);
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

    public GrantsReadDTO patchGrants(UUID id, GrantsPatchDTO patch) {
        Grants grants = getGrantsRequired(id);

        if (patch.getUserTypeId()!=null) {
            grants.setUserTypeId(patch.getUserTypeId());
        }
        if (patch.getObjectName()!=null) {
            grants.setObjectName(patch.getObjectName());
        }
        if (patch.getUserPermission()!=null) {
            grants.setUserPermission(patch.getUserPermission());
        }
        if (patch.getGrantedBy()!=null) {
            grants.setGrantedBy(patch.getGrantedBy());
        }
        grants = grantsRepository.save(grants);
        return toRead(grants);
    }

    private Grants getGrantsRequired(UUID id) {
        return grantsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Grants.class, id);
        });
    }

    public void deleteGrants(UUID id) {
        grantsRepository.delete(getGrantsRequired(id));
    }
}
