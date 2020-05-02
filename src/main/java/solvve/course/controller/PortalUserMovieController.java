package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.CurrentUser;
import solvve.course.dto.MovieReadDTO;
import solvve.course.service.PortalUserMovieService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portal-user/{portalUserId}/movies")
public class PortalUserMovieController {

    @Autowired
    private PortalUserMovieService portalUserMovieService;

    @CurrentUser
    @GetMapping("/{startFrom}/{startTo}")
    public List<MovieReadDTO> getMovies(@PathVariable UUID portalUserId,
                                        @PathVariable Instant startFrom,
                                        @PathVariable Instant startTo) {
        return portalUserMovieService.getPortalUserMovies(portalUserId, startFrom, startTo);
    }

    @CurrentUser
    @GetMapping("/recommendations")
    public List<MovieReadDTO> getRecommendedMovies(@PathVariable UUID portalUserId) {
        return portalUserMovieService.getPortalUserRecommendedMovies(portalUserId);
    }
}
