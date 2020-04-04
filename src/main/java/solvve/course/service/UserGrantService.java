package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserGrant;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.repository.UserGrantRepository;

import java.util.UUID;

@Service
public class UserGrantService extends AbstractService {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Transactional(readOnly = true)
    public UserGrantReadDTO getGrants(UUID id) {
        UserGrant userGrant = repositoryHelper.getByIdRequired(UserGrant.class, id);
        return translationService.translate(userGrant, UserGrantReadDTO.class);
    }

    public UserGrantReadDTO createGrants(UserGrantCreateDTO create) {
        UserGrant userGrant = translationService.translate(create, UserGrant.class);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.translate(userGrant, UserGrantReadDTO.class);
    }

    public UserGrantReadDTO patchGrants(UUID id, UserGrantPatchDTO patch) {
        UserGrant userGrant = repositoryHelper.getByIdRequired(UserGrant.class, id);

        translationService.map(patch, userGrant);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.translate(userGrant, UserGrantReadDTO.class);
    }

    public void deleteGrants(UUID id) {
        userGrantRepository.delete(repositoryHelper.getByIdRequired(UserGrant.class, id));
    }

    public UserGrantReadDTO updateGrants(UUID id, UserGrantPutDTO put) {
        UserGrant userGrant = repositoryHelper.getByIdRequired(UserGrant.class, id);

        translationService.updateEntity(put, userGrant);

        userGrant = userGrantRepository.save(userGrant);
        return translationService.translate(userGrant, UserGrantReadDTO.class);
    }
}
