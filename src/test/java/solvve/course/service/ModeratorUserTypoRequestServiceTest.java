package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.LinkageCorruptedEntityException;
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
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);
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
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern", movie);
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
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser,
                "Ivh reide vie;, ich reise gern. Fern und nah und nah und fern", movie);
        UserTypoRequest userTypoRequest = testObjectsFactory.createUserTypoRequest(portalUser, portalUser, null, null,
                Instant.now(), "Text", "proposedText", ModeratorTypoReviewStatusType.NEED_TO_FIX, news,
                "sourceText");

        UserTypoRequestPutDTO put = new UserTypoRequestPutDTO();
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        put.setApprovedText("new approved text");

        Assertions.assertThatThrownBy(()->
                moderatorUserTypoRequestService.fixNewsTypo(portalUser.getId(),
                        userTypoRequest.getId(), put)).isInstanceOf(LinkageCorruptedEntityException.class);
    }

    @Test
    public void testPatchAndUpdateModeratorUserTypoRequestWithEmptyProposedText() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        String newsTopic = "Movie Alien released";
        String description = "In the distant future, the crew of the commercial spaceship Nostrom are on " +
                "their way home when they pick up a distress call from a distant moon. The crew are under obligation " +
                "to investigate and the spaceship descends on the moon afterwards. After a rough landing, three crew " +
                "members leave the spaceship to explore the area on the moon. At the same time as they discover a " +
                "hive colony of some unknown creature, the ship's computer deciphers the message to be a warning, not" +
                " a distress call. When one of the eggs is disturbed, the crew realizes that they are not alone on " +
                "the spaceship and they must deal with the consequences.";
        News news = testObjectsFactory.createNews(portalUser, newsTopic, description, movie, Instant.now());

        UserTypoRequest userTypoRequest = testObjectsFactory.createUserTypoRequest(null,
                portalUser,
                null,
                null,
                null,
                null,
                null,
                ModeratorTypoReviewStatusType.NEED_TO_FIX,
                news,
                "Nostrom");

        UserTypoRequestPatchDTO userTypoRequestPatchDTO = new UserTypoRequestPatchDTO();
        userTypoRequestPatchDTO.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.IN_REVIEW);
        userTypoRequestPatchDTO.setModeratorId(portalUser.getId());

        UserTypoRequestReadDTO read =
                moderatorUserTypoRequestService.patchUserTypoRequestByModerator(portalUser.getId(),
                userTypoRequest.getId(),
                userTypoRequestPatchDTO);

        Assertions.assertThat(read.getModeratorId()).isEqualTo(userTypoRequestPatchDTO.getModeratorId());
        Assertions.assertThat(read.getModeratorTypoReviewStatusType())
                .isEqualTo(userTypoRequestPatchDTO.getModeratorTypoReviewStatusType());

        UserTypoRequestPutDTO put = new UserTypoRequestPutDTO();
        put.setApprovedText("Nostromo");
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        put.setFixAppliedDate(Instant.now());

        UserTypoRequestReadDTO readAfterFix = moderatorUserTypoRequestService.fixNewsTypo(portalUser.getId(),
                userTypoRequest.getId(), put);

        Assertions.assertThat(readAfterFix.getFixAppliedDate()).isEqualTo(put.getFixAppliedDate());
        Assertions.assertThat(readAfterFix.getModeratorTypoReviewStatusType())
                .isEqualTo(put.getModeratorTypoReviewStatusType());
        Assertions.assertThat(readAfterFix.getApprovedText()).isEqualTo(put.getApprovedText());

        News newsAfterFix = newsRepository.findById(news.getId()).get();

        Assertions.assertThat(newsAfterFix.getDescription()).contains(put.getApprovedText());
    }

    @Test
    public void testUpdateModeratorUserTypoRequestWithDuplicateRequestExist() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        String newsTopic = "Movie Alien released";
        String description = "In the distant future, the crew of the commercial spaceship Nostrom are on " +
                "their way home when they pick up a distress call from a distant moon. The crew are under obligation " +
                "to investigate and the spaceship descends on the moon afterwards. After a rough landing, three crew " +
                "members leave the spaceship to explore the area on the moon. At the same time as they discover a " +
                "hive colony of some unknown creature, the ship's computer deciphers the message to be a warning, not" +
                " a distress call. When one of the eggs is disturbed, the crew realizes that they are not alone on " +
                "the spaceship and they must deal with the consequences.";
        News news = testObjectsFactory.createNews(portalUser, newsTopic, description, movie, Instant.now());

        UserTypoRequest userTypoRequest1 = testObjectsFactory.createUserTypoRequest(portalUser,
                portalUser,
                null,
                null,
                null,
                null,
                null,
                ModeratorTypoReviewStatusType.NEED_TO_FIX,
                news,
                "Nostrom");

        UserTypoRequest userTypoRequest2 = testObjectsFactory.createUserTypoRequest(null,
                portalUser,
                null,
                null,
                null,
                null,
                null,
                ModeratorTypoReviewStatusType.NEED_TO_FIX,
                news,
                "Nostrom");

        UserTypoRequestPutDTO put = new UserTypoRequestPutDTO();
        put.setApprovedText("Nostromo");
        put.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        put.setFixAppliedDate(Instant.now());

        moderatorUserTypoRequestService.fixNewsTypo(portalUser.getId(),
                userTypoRequest1.getId(), put);

        UserTypoRequest userTypoRequestAfterFix = userTypoRequestRepository.findById(userTypoRequest2.getId()).get();

        Assertions.assertThat(userTypoRequestAfterFix.getModeratorTypoReviewStatusType())
                .isEqualTo(put.getModeratorTypoReviewStatusType());
    }
}
