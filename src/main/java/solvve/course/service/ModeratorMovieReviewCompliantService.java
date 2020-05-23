package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.exception.LinkageCorruptedEntityException;
import solvve.course.repository.MovieReviewCompliantRepository;
import solvve.course.repository.MovieReviewRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ModeratorMovieReviewCompliantService extends AbstractService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Transactional(readOnly = true)
    public List<MovieReviewCompliantReadDTO> getCreatedMovieReviewCompliants(UUID moderatorId) {
        List<MovieReviewCompliant> movieReviewCompliants =
                movieReviewCompliantRepository.findByModeratedStatusAndModeratorIdOrderByCreatedAt(
                        UserModeratedStatusType.CREATED, moderatorId);
        return movieReviewCompliants.stream().map(e ->
                translationService.translate(e, MovieReviewCompliantReadDTO.class)).collect(Collectors.toList());
    }

    public MovieReviewCompliantReadDTO patchMovieReviewCompliantByModeratedStatus(UUID id,
                                                                                  MovieReviewCompliantPatchDTO patch,
                                                                                  UUID moderatorId) {
        repositoryHelper.validateIFExists(PortalUser.class, moderatorId);
        MovieReviewCompliant movieReviewCompliant = repositoryHelper.getByIdRequired(MovieReviewCompliant.class, id);

        translationService.map(patch, movieReviewCompliant);
        movieReviewCompliant = movieReviewCompliantRepository.save(movieReviewCompliant);

        return translationService.translate(movieReviewCompliant, MovieReviewCompliantReadDTO.class);
    }

    public void deleteMovieReviewByCompliantByModerator(UUID moderatorId, UUID id) {
        MovieReviewCompliant movieReviewCompliant = repositoryHelper.getByIdRequired(MovieReviewCompliant.class, id);
        if (movieReviewCompliant.getModerator().getId().equals(moderatorId)) {
            movieReviewRepository.delete(movieReviewCompliant.getMovieReview());
        } else {
            throw new LinkageCorruptedEntityException(MovieReviewCompliant.class, id);
        }
    }
}
