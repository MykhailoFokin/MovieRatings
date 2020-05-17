package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserTypoRequest;
import solvve.course.dto.UserTypoRequestCreateDTO;
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
    public List<UserTypoRequestReadDTO> getUserTypoRequests(UUID newsId, UUID requesterId) {
        List<UserTypoRequest> userTypoRequests = getUserTypoRequestsRequired(newsId, requesterId);
        return userTypoRequests.stream().map(e ->
                translationService.translate(e, UserTypoRequestReadDTO.class)).collect(Collectors.toList());
    }

    public UserTypoRequestReadDTO createUserTypoRequest(UUID newsId,
                                                        UUID requesterId,
                                                        UserTypoRequestCreateDTO create) {
        UserTypoRequest userTypoRequest = translationService.translate(create, UserTypoRequest.class);
        userTypoRequest.setNews(repositoryHelper.getReferenceIfExists(News.class, newsId));
        userTypoRequest.setRequester(repositoryHelper.getReferenceIfExists(PortalUser.class, requesterId));
        userTypoRequest = userTypoRequestRepository.save(userTypoRequest);

        return translationService.translate(userTypoRequest, UserTypoRequestReadDTO.class);
    }

    public void deleteUserTypoRequest(UUID newsId,
                                      UUID requesterId,
                                      UUID id) {
        userTypoRequestRepository.delete(getUserTypoRequestRequired(newsId, requesterId, id));
    }

    private UserTypoRequest getUserTypoRequestRequired(UUID newsId, UUID requesterId, UUID id) {
        return userTypoRequestRepository.findByNewsIdAndRequesterIdAndId(newsId, requesterId, id).orElseThrow(() -> {
            throw new EntityNotFoundException(UserTypoRequest.class, newsId, requesterId, id);
        });
    }

    private List<UserTypoRequest> getUserTypoRequestsRequired(UUID newsId, UUID requesterId) {
        return userTypoRequestRepository.findByNewsIdAndRequesterIdOrderByCreatedAt(newsId, requesterId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(UserTypoRequest.class, newsId, requesterId);
                });
    }
}
