package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.MovieRolesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie/{movieId}/roles")
public class MovieRolesController {

    @Autowired
    private MovieRolesService movieRolesService;

    @PublicAccess
    @GetMapping
    public List<RoleReadDTO> getMovieRoles(
            @PathVariable UUID movieId) {
        return movieRolesService.getMovieRoles(movieId);
    }
}
