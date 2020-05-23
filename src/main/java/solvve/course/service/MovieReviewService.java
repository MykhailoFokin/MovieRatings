package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewRepository;

import java.util.UUID;

@Service
public class MovieReviewService extends AbstractService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private PortalUserService portalUserService;

    @Transactional(readOnly = true)
    public MovieReviewReadDTO getMovieReview(UUID id) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReview.class, id);
        });
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MovieReviewReadDTO createMovieReview(MovieReviewCreateDTO create) {
        MovieReview movieReview = translationService.translate(create, MovieReview.class);
        movieReview.setModeratedStatus(UserModeratedStatusType.CREATED);

        PortalUser portalUser = repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId());
        if (portalUser.getUserConfidence() == UserConfidenceType.TRUSTWORTHY) {
            movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        }

        movieReview = movieReviewRepository.save(movieReview);
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    public MovieReviewReadDTO patchMovieReview(UUID id, MovieReviewPatchDTO patch) {
        MovieReview movieReview = repositoryHelper.getByIdRequired(MovieReview.class, id);

        translationService.map(patch, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        if (movieReview.getModeratedStatus().equals(UserModeratedStatusType.SUCCESS)) {
            portalUserService.updatePortalUserConfidence(movieReview.getPortalUser().getId(),
                    UserConfidenceType.TRUSTWORTHY);
        }
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }

    public void deleteMovieReview(UUID id) {
        movieReviewRepository.delete(repositoryHelper.getByIdRequired(MovieReview.class, id));
    }

    public MovieReviewReadDTO updateMovieReview(UUID id, MovieReviewPutDTO put) {
        MovieReview movieReview = repositoryHelper.getByIdRequired(MovieReview.class, id);

        translationService.updateEntity(put, movieReview);

        movieReview = movieReviewRepository.save(movieReview);
        if (movieReview.getModeratedStatus().equals(UserModeratedStatusType.SUCCESS)) {
            portalUserService.updatePortalUserConfidence(movieReview.getPortalUser().getId(),
                    UserConfidenceType.TRUSTWORTHY);
        }
        return translationService.translate(movieReview, MovieReviewReadDTO.class);
    }
}
