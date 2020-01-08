package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.UUID;

@Service
public class RoleSpoilerDataService {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Transactional(readOnly = true)
    public RoleSpoilerDataReadDTO getRoleSpoilerData(UUID id) {
        RoleSpoilerData roleSpoilerData = roleSpoilerDataRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(RoleSpoilerData.class, id);
        });
        return toRead(roleSpoilerData);
    }

    private RoleSpoilerDataReadDTO toRead(RoleSpoilerData roleSpoilerData) {
        RoleSpoilerDataReadDTO dto = new RoleSpoilerDataReadDTO();
        dto.setId(roleSpoilerData.getId());
        dto.setRoleReviewId(roleSpoilerData.getRoleReviewId());
        dto.setStartIndex(roleSpoilerData.getStartIndex());
        dto.setEndIndex(roleSpoilerData.getEndIndex());
        return dto;
    }

    public RoleSpoilerDataReadDTO createRoleSpoilerData(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReviewId(create.getRoleReviewId());
        roleSpoilerData.setStartIndex(create.getStartIndex());
        roleSpoilerData.setEndIndex(create.getEndIndex());

        roleSpoilerData = roleSpoilerDataRepository.save(roleSpoilerData);
        return toRead(roleSpoilerData);
    }
}
