package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypePutDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public UserTypeReadDTO getUserTypes(UUID id) {
        UserType userType = getUserTypesRequired(id);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public UserTypeReadDTO createUserTypes(UserTypeCreateDTO create) {
        UserType userType = translationService.translate(create, UserType.class);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public UserTypeReadDTO patchUserTypes(UUID id, UserTypePatchDTO patch) {
        UserType userType = getUserTypesRequired(id);

        translationService.map(patch, userType);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public void deleteUserTypes(UUID id) {
        userTypeRepository.delete(getUserTypesRequired(id));
    }

    public UserTypeReadDTO updateUserTypes(UUID id, UserTypePutDTO put) {
        UserType userType = getUserTypesRequired(id);

        translationService.updateEntity(put, userType);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    private UserType getUserTypesRequired(UUID id) {
        return userTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserType.class, id);
        });
    }
}
