package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.UserTypoRequestCreateDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.UserTypoRequestRepository;

import java.time.Instant;
import java.util.List;

public class UserTypoRequestServiceTest extends BaseTest {

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    @Autowired
    private UserTypoRequestService userTypoRequestService;

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
                userTypoRequestService.getUserTypoRequestsByNewsAndRequester(news.getId(), portalUser.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(userTypoRequest.getId());
    }

    @Test
    public void testCreateMovie() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        UserTypoRequestCreateDTO create = new UserTypoRequestCreateDTO();
        create.setNewsId(news.getId());
        create.setRequesterId(portalUser.getId());
        create.setSourceText("SourceText");
        UserTypoRequestReadDTO read = userTypoRequestService.createUserTypoRequest(news.getId(), create);
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(read, "requesterId");

        UserTypoRequest userTypoRequest = userTypoRequestRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(userTypoRequest, "requesterId", "moderatorId",
                "newsId", "movieId", "roleId");
    }
}
