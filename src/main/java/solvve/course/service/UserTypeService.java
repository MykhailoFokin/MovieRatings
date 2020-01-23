package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional(readOnly = true)
    public UserTypeReadDTO getUserTypes(UUID id) {
        UserType userType = getUserTypesRequired(id);
        return toRead(userType);
    }

    private UserTypeReadDTO toRead(UserType userType) {
        UserTypeReadDTO dto = new UserTypeReadDTO();
        dto.setId(userType.getId());
        dto.setUserGroup(userType.getUserGroup());
        dto.setGrants(userType.getGrants());
        return dto;
    }

    public UserTypeReadDTO createUserTypes(UserTypeCreateDTO create) {
        UserType userType = new UserType();
        userType.setUserGroup(create.getUserGroup());
        userType.setGrants(create.getGrants());

        userType = userTypeRepository.save(userType);
        return toRead(userType);
    }

    public UserTypeReadDTO patchUserTypes(UUID id, UserTypePatchDTO patch) {
        UserType userType = getUserTypesRequired(id);

        if (patch.getUserGroup()!=null) {
            userType.setUserGroup(patch.getUserGroup());
        }
        if (patch.getGrants()!=null) {
            userType.setGrants(patch.getGrants());
        }
        userType = userTypeRepository.save(userType);
        return toRead(userType);
    }

    private UserType getUserTypesRequired(UUID id) {
        return userTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserType.class, id);
        });
    }

    public void deleteUserTypes(UUID id) {
        userTypeRepository.delete(getUserTypesRequired(id));
    }
}
