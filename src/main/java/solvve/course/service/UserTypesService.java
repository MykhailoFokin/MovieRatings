package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserTypes;
import solvve.course.dto.UserTypesCreateDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@Service
public class UserTypesService {

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Transactional(readOnly = true)
    public UserTypesReadDTO getUserTypes(UUID id) {
        UserTypes userTypes = userTypesRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserTypes.class, id);
        });
        return toRead(userTypes);
    }

    private UserTypesReadDTO toRead(UserTypes userTypes) {
        UserTypesReadDTO dto = new UserTypesReadDTO();
        dto.setId(userTypes.getId());
        dto.setUserGroup(userTypes.getUserGroup());
        return dto;
    }

    public UserTypesReadDTO createUserTypes(UserTypesCreateDTO create) {
        UserTypes userTypes = new UserTypes();
        userTypes.setUserGroup(create.getUserGroup());

        userTypes = userTypesRepository.save(userTypes);
        return toRead(userTypes);
    }
}
