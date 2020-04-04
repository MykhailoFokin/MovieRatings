package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypePutDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@Service
public class UserTypeService extends AbstractService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional(readOnly = true)
    public UserTypeReadDTO getUserTypes(UUID id) {
        UserType userType = repositoryHelper.getByIdRequired(UserType.class, id);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public UserTypeReadDTO createUserTypes(UserTypeCreateDTO create) {
        UserType userType = translationService.translate(create, UserType.class);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public UserTypeReadDTO patchUserTypes(UUID id, UserTypePatchDTO patch) {
        UserType userType = repositoryHelper.getByIdRequired(UserType.class, id);

        translationService.map(patch, userType);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }

    public void deleteUserTypes(UUID id) {
        userTypeRepository.delete(repositoryHelper.getByIdRequired(UserType.class, id));
    }

    public UserTypeReadDTO updateUserTypes(UUID id, UserTypePutDTO put) {
        UserType userType = repositoryHelper.getByIdRequired(UserType.class, id);

        translationService.updateEntity(put, userType);

        userType = userTypeRepository.save(userType);
        return translationService.translate(userType, UserTypeReadDTO.class);
    }
}
