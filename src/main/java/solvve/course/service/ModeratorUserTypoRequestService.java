package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkageCorruptedEntityException;
import solvve.course.repository.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ModeratorUserTypoRequestService extends AbstractService {

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserTypoRequestReadDTO> getModeratorUserTypoRequests(UUID moderatorId) {
        List<UserTypoRequest> userTypoRequests = getUserTypoRequestsRequired(moderatorId);
        return userTypoRequests.stream().map(userTypoRequest -> translationService.translate(userTypoRequest,
                UserTypoRequestReadDTO.class)).collect(Collectors.toList());
    }

    public UserTypoRequestReadDTO patchUserTypoRequestByModerator(UUID moderatorId, UUID userTypoRequestId,
                                                          UserTypoRequestPatchDTO patch) {
        UserTypoRequest userTypoRequest = repositoryHelper.getByIdRequired(UserTypoRequest.class, userTypoRequestId);

        translationService.map(patch, userTypoRequest);
        userTypoRequest = userTypoRequestRepository.save(userTypoRequest);

        return translationService.translate(userTypoRequest, UserTypoRequestReadDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserTypoRequestReadDTO fixNewsTypo(UUID portalUserId, UUID userTypoRequestId,
                                                                    UserTypoRequestPutDTO put) {
        repositoryHelper.validateIFExists(PortalUser.class, portalUserId);

        UserTypoRequest userTypoRequest = repositoryHelper.getByIdRequired(UserTypoRequest.class, userTypoRequestId);

        checkAlreadyFixedUserTypoRequest(userTypoRequest.getModeratorTypoReviewStatusType(),
                put.getModeratorTypoReviewStatusType(), userTypoRequestId);

        if (userTypoRequest.getNews() != null) {
            fixNewsByUserTypoRequest(userTypoRequest.getNews().getId(), userTypoRequest.getSourceText(),
                    put.getApprovedText());
        } else if (userTypoRequest.getMovie() != null) {
            fixMovieByUserTypoRequest(userTypoRequest.getMovie().getId(), userTypoRequest.getSourceText(),
                    put.getApprovedText());
        } else if (userTypoRequest.getRole() != null) {
            fixRoleByUserTypoRequest(userTypoRequest.getRole().getId(), userTypoRequest.getSourceText(),
                    put.getApprovedText());
        } else {
            throw new LinkageCorruptedEntityException(UserTypoRequest.class, userTypoRequestId);
        }
        put.setFixAppliedDate(Instant.now());
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        translationService.updateEntity(put, userTypoRequest);
        userTypoRequest = userTypoRequestRepository.save(userTypoRequest);

        updateSameUserTypoRequestWithSameStatus(userTypoRequest);

        return translationService.translate(userTypoRequest, UserTypoRequestReadDTO.class);
    }

    private List<UserTypoRequest> getUserTypoRequestsRequired(UUID moderatorId) {
        return userTypoRequestRepository.findUserTypoRequestsByModeratorOrRequiredAttention(moderatorId,
                List.of(ModeratorTypoReviewStatusType.IN_REVIEW, ModeratorTypoReviewStatusType.NEED_TO_FIX));
    }

    private void checkAlreadyFixedUserTypoRequest(ModeratorTypoReviewStatusType entityModeratorTypoReviewStatusType,
                                                 ModeratorTypoReviewStatusType newModeratorTypoReviewStatusType,
                                                 UUID id) {
        if (entityModeratorTypoReviewStatusType == ModeratorTypoReviewStatusType.FIXED
                && newModeratorTypoReviewStatusType == ModeratorTypoReviewStatusType.FIXED) {
            throw new LinkageCorruptedEntityException(UserTypoRequest.class, id);
        }
    }

    private void updateSameUserTypoRequestWithSameStatus(UserTypoRequest userTypoRequest) {
        List<UUID> userTypoRequestsIds;
        if (userTypoRequest.getNews() != null) {
            userTypoRequestsIds =
                    userTypoRequestRepository.findUserTypoRequestsOnNewsRequiredAttentionBySourceText(
                            userTypoRequest.getNews().getId(),
                            userTypoRequest.getSourceText(),
                            List.of(ModeratorTypoReviewStatusType.NEED_TO_FIX));
        } else if (userTypoRequest.getMovie() != null) {
            userTypoRequestsIds =
                    userTypoRequestRepository.findUserTypoRequestsOnMovieRequiredAttentionBySourceText(
                            userTypoRequest.getMovie().getId(),
                            userTypoRequest.getSourceText(),
                            List.of(ModeratorTypoReviewStatusType.NEED_TO_FIX));
        } else if (userTypoRequest.getRole() != null) {
            userTypoRequestsIds =
                    userTypoRequestRepository.findUserTypoRequestsOnRoleRequiredAttentionBySourceText(
                            userTypoRequest.getRole().getId(),
                            userTypoRequest.getSourceText(),
                            List.of(ModeratorTypoReviewStatusType.NEED_TO_FIX));
        } else {
            throw new LinkageCorruptedEntityException(UserTypoRequest.class, userTypoRequest.getId());
        }

        if (!userTypoRequestsIds.isEmpty()) {
            userTypoRequestRepository.updateUserTypoRequestOfSameContentAsObsolete(userTypoRequest.getFixAppliedDate(),
                    ModeratorTypoReviewStatusType.FIXED, userTypoRequest.getModerator().getId(), userTypoRequestsIds);
        }
    }

    private void fixNewsByUserTypoRequest(UUID newsId, String sourceText, String approvedText) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> {
            throw new EntityNotFoundException(News.class, newsId);
        });
        if (news.getDescription().indexOf(sourceText) == -1) {
            throw new LinkageCorruptedEntityException(News.class, newsId);
        } else {
            news.setDescription(news.getDescription().replace(sourceText, approvedText));
        }
        newsRepository.save(news);
    }

    private void fixMovieByUserTypoRequest(UUID movieId, String sourceText, String approvedText) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> {
            throw new EntityNotFoundException(Movie.class, movieId);
        });
        if (movie.getDescription().indexOf(sourceText) == -1) {
            throw new LinkageCorruptedEntityException(Movie.class, movieId);
        } else {
            movie.setDescription(movie.getDescription().replace(sourceText, approvedText));
        }
        movieRepository.save(movie);
    }

    private void fixRoleByUserTypoRequest(UUID roleId, String sourceText, String approvedText) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {
            throw new EntityNotFoundException(News.class, roleId);
        });
        if (role.getDescription().indexOf(sourceText) == -1) {
            throw new LinkageCorruptedEntityException(News.class, roleId);
        } else {
            role.setDescription(role.getDescription().replace(sourceText, approvedText));
        }
        roleRepository.save(role);
    }
}
