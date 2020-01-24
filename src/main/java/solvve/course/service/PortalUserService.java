package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.PortalUser;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserPutDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@Service
public class PortalUserService {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public PortalUserReadDTO getPortalUser(UUID id) {
        PortalUser portalUser = getPortalUserRequired(id);
        return translationService.toRead(portalUser);
    }

    public PortalUserReadDTO createPortalUser(PortalUserCreateDTO create) {
        PortalUser portalUser = translationService.toEntity(create);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.toRead(portalUser);
    }

    public PortalUserReadDTO patchPortalUser(UUID id, PortalUserPatchDTO patch) {
        PortalUser portalUser = getPortalUserRequired(id);

        translationService.patchEntity(patch, portalUser);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.toRead(portalUser);
    }

    private PortalUser getPortalUserRequired(UUID id) {
        return portalUserRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(PortalUser.class, id);
        });
    }

    public void deletePortalUser(UUID id) {
        portalUserRepository.delete(getPortalUserRequired(id));
    }

    public PortalUserReadDTO putPortalUser(UUID id, PortalUserPutDTO put) {
        PortalUser portalUser = getPortalUserRequired(id);

        translationService.putEntity(put, portalUser);

        portalUser = portalUserRepository.save(portalUser);
        return translationService.toRead(portalUser);
    }
}
