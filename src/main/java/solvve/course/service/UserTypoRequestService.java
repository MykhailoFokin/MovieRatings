package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.ModeratorTypoReviewStatusType;
import solvve.course.domain.UserTypoRequest;
import solvve.course.dto.UserTypoRequestCreateDTO;
import solvve.course.dto.UserTypoRequestPatchDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypoRequestRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserTypoRequestService extends AbstractService {

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    @Transactional(readOnly = true)
    public List<UserTypoRequestReadDTO> getUserTypoRequests() {
        List<UserTypoRequest> userTypoRequests = userTypoRequestRepository
                .findByModeratorTypoReviewStatusTypeOrderByCreatedAt(ModeratorTypoReviewStatusType.NEED_TO_FIX);
        return userTypoRequests.stream().map(e ->
                translationService.translate(e, UserTypoRequestReadDTO.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserTypoRequestReadDTO> getUserTypoRequestsByNewsAndRequester(UUID newsId, UUID requesterId) {
        List<UserTypoRequest> userTypoRequests = getUserTypoRequestsByNewsAndRequesterRequired(newsId, requesterId);
        return userTypoRequests.stream().map(e ->
                translationService.translate(e, UserTypoRequestReadDTO.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserTypoRequestReadDTO> getUserTypoRequestsForNews(UUID newsId) {
        List<UserTypoRequest> userTypoRequests = getNewsUserTypoRequestsRequired(newsId);
        return userTypoRequests.stream().map(e ->
                translationService.translate(e, UserTypoRequestReadDTO.class)).collect(Collectors.toList());
    }

    public UserTypoRequestReadDTO createUserTypoRequest(UUID newsId,
                                                        UserTypoRequestCreateDTO create) {
        UserTypoRequest userTypoRequest = translationService.translate(create, UserTypoRequest.class);
        userTypoRequest.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.NEED_TO_FIX);
        userTypoRequest = userTypoRequestRepository.save(userTypoRequest);

        return translationService.translate(userTypoRequest, UserTypoRequestReadDTO.class);
    }

    public void deleteUserTypoRequest(UUID newsId,
                                      UUID requesterId,
                                      UUID id) {
        userTypoRequestRepository.delete(getUserTypoRequestRequired(newsId, requesterId, id));
    }

    public UserTypoRequestReadDTO patchUserTypoRequest(UUID newsId, UUID userTypoRequestId,
                                                       UserTypoRequestPatchDTO patch) {
        UserTypoRequest userTypoRequest = repositoryHelper.getByIdRequired(UserTypoRequest.class, userTypoRequestId);

        translationService.map(patch, userTypoRequest);
        userTypoRequest = userTypoRequestRepository.save(userTypoRequest);

        return translationService.translate(userTypoRequest, UserTypoRequestReadDTO.class);
    }

    private UserTypoRequest getUserTypoRequestRequired(UUID newsId, UUID requesterId, UUID id) {
        return userTypoRequestRepository.findByNewsIdAndRequesterIdAndId(newsId, requesterId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserTypoRequest.class, newsId, requesterId, id);
        });
    }

    private List<UserTypoRequest> getUserTypoRequestsByNewsAndRequesterRequired(UUID newsId, UUID requesterId) {
        return userTypoRequestRepository.findByNewsIdAndRequesterIdOrderByCreatedAt(newsId, requesterId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(UserTypoRequest.class, newsId, requesterId);
                });
    }

    private List<UserTypoRequest> getNewsUserTypoRequestsRequired(UUID newsId) {
        return userTypoRequestRepository.findByRequestedOnNews(newsId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(UserTypoRequest.class, newsId);
                });
    }
}
