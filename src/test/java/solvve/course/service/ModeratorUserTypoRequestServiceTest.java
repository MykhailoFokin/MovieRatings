package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.UserTypoRequestPatchDTO;
import solvve.course.dto.UserTypoRequestPutDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.exception.UnprocessableEntityException;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.UserTypoRequestRepository;

import java.time.Instant;
import java.util.List;

public class ModeratorUserTypoRequestServiceTest extends BaseTest {

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    @Autowired
    private ModeratorUserTypoRequestService moderatorUserTypoRequestService;

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void testGetModeratorUserTypoRequest() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        UserTypoRequest userTypoRequest = testObjectsFactory.createUserTypoRequest(portalUser, portalUser, null, null,
                Instant.now(), "Text", "proposedText", ModeratorTypoReviewStatusType.NEED_TO_FIX, news,
                "sourceText");
        List<UserTypoRequest> userTypoRequests = List.of(userTypoRequest);

        List<UserTypoRequestReadDTO> readDTO =
                moderatorUserTypoRequestService.getModeratorUserTypoRequests(portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(userTypoRequest.getId());
    }

    @Test
    public void testPatchModeratorUserTypoRequestInFixedStatus() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern");
        UserTypoRequest userTypoRequest = testObjectsFactory.createUserTypoRequest(portalUser, portalUser, null, null,
                Instant.now(), "Text", "proposedText", ModeratorTypoReviewStatusType.NEED_TO_FIX, news,
                "sourceText");

        UserTypoRequestPatchDTO patch = new UserTypoRequestPatchDTO();
        patch.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        patch.setApprovedText("new approved text");

        moderatorUserTypoRequestService.patchUserTypoRequestByModerator(portalUser.getId(),
                userTypoRequest.getId(), patch);

        UserTypoRequest userTypoRequestAfterPatch = userTypoRequestRepository.findById(userTypoRequest.getId()).get();
        Assert.assertEquals(patch.getApprovedText(), userTypoRequestAfterPatch.getApprovedText());
    }

    @Test
    public void testUpdateModeratorUserTypoRequestInFixedStatus() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern");
        UserTypoRequest userTypoRequest = testObjectsFactory.createUserTypoRequest(portalUser, portalUser, null, null,
                Instant.now(), "Text", "proposedText", ModeratorTypoReviewStatusType.NEED_TO_FIX, news,
                "sourceText");

        UserTypoRequestPutDTO put = new UserTypoRequestPutDTO();
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        put.setApprovedText("new approved text");

        Assertions.assertThatThrownBy(()->
                moderatorUserTypoRequestService.fixNewsTypo(portalUser.getId(),
                        userTypoRequest.getId(), put)).isInstanceOf(UnprocessableEntityException.class);
    }
}
