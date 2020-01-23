package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Grant;
import solvve.course.dto.GrantCreateDTO;
import solvve.course.dto.GrantPatchDTO;
import solvve.course.dto.GrantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GrantRepository;

import java.util.UUID;

@Service
public class GrantService {

    @Autowired
    private GrantRepository grantRepository;

    @Transactional(readOnly = true)
    public GrantReadDTO getGrants(UUID id) {
        Grant grant = getGrantsRequired(id);
        return toRead(grant);
    }

    private GrantReadDTO toRead(Grant grant) {
        GrantReadDTO dto = new GrantReadDTO();
        dto.setId(grant.getId());
        dto.setUserTypeId(grant.getUserTypeId());
        dto.setUserPermission(grant.getUserPermission());
        dto.setObjectName(grant.getObjectName());
        dto.setGrantedBy(grant.getGrantedBy());
        return dto;
    }

    public GrantReadDTO createGrants(GrantCreateDTO create) {
        Grant grant = new Grant();
        grant.setUserTypeId(create.getUserTypeId());
        grant.setUserPermission(create.getUserPermission());
        grant.setObjectName(create.getObjectName());
        grant.setGrantedBy(create.getGrantedBy());

        grant = grantRepository.save(grant);
        return toRead(grant);
    }

    public GrantReadDTO patchGrants(UUID id, GrantPatchDTO patch) {
        Grant grant = getGrantsRequired(id);

        if (patch.getUserTypeId()!=null) {
            grant.setUserTypeId(patch.getUserTypeId());
        }
        if (patch.getObjectName()!=null) {
            grant.setObjectName(patch.getObjectName());
        }
        if (patch.getUserPermission()!=null) {
            grant.setUserPermission(patch.getUserPermission());
        }
        if (patch.getGrantedBy()!=null) {
            grant.setGrantedBy(patch.getGrantedBy());
        }
        grant = grantRepository.save(grant);
        return toRead(grant);
    }

    private Grant getGrantsRequired(UUID id) {
        return grantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Grant.class, id);
        });
    }

    public void deleteGrants(UUID id) {
        grantRepository.delete(getGrantsRequired(id));
    }
}
