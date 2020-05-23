package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.Role;
import solvve.course.dto.RoleReadDTO;
import solvve.course.repository.RoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieRolesService extends AbstractService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleReadDTO> getMovieRoles(UUID movieId) {
        List<Role> roles = roleRepository.findByMovieId(movieId);

        return roles.stream().map(e -> translationService.translate(e, RoleReadDTO.class))
                .collect(Collectors.toList());
    }
}
