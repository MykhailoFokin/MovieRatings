package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.dto.MovieReadDTO;
import solvve.course.repository.MovieRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortalUserMovieService extends AbstractService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieReadDTO> getPortalUserMovies(UUID portalUserId, Instant startFrom, Instant startTo) {
        return movieRepository.findPortalUserTouchedMoviesInGivenInterval(portalUserId,
                startFrom, startTo).stream().map(e ->
                translationService.translate(e, MovieReadDTO.class)).collect(Collectors.toList());
    }

    public List<MovieReadDTO> getPortalUserRecommendedMovies(UUID portalUserId) {
        return movieRepository.findPortalUserRecommendedMovies(portalUserId)
                .stream().map(e ->
                        translationService.translate(e, MovieReadDTO.class)).collect(Collectors.toList());
    }
}
