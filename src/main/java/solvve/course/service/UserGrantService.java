package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserGrant;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserGrantRepository;

import java.util.UUID;

@Service
public class UserGrantService {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public UserGrantReadDTO getGrants(UUID id) {
        UserGrant userGrant = getGrantsRequired(id);
        return translationService.toRead(userGrant);
    }

    public UserGrantReadDTO createGrants(UserGrantCreateDTO create) {
        UserGrant userGrant = translationService.toEntity(create);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.toRead(userGrant);
    }

    public UserGrantReadDTO patchGrants(UUID id, UserGrantPatchDTO patch) {
        UserGrant userGrant = getGrantsRequired(id);

        translationService.patchEntity(patch, userGrant);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.toRead(userGrant);
    }

    private UserGrant getGrantsRequired(UUID id) {
        return userGrantRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserGrant.class, id);
        });
    }

    public void deleteGrants(UUID id) {
        userGrantRepository.delete(getGrantsRequired(id));
    }

    public UserGrantReadDTO putGrants(UUID id, UserGrantPutDTO put) {
        UserGrant userGrant = getGrantsRequired(id);

        translationService.putEntity(put, userGrant);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.toRead(userGrant);
    }
}
