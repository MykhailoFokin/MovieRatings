package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.UserTypoRequestRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class ModeratorUserTypoRequestServiceTest extends BaseTest {

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    @Autowired
    private ModeratorUserTypoRequestService moderatorUserTypoRequestService;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testGetModeratorUserTypoRequest() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);
        NewsUserReview newsUserReview = testObjectsFactory.createNewsUserReview(portalUser, news, portalUser,
                ModeratorTypoReviewStatusType.IN_REVIEW);
        List<UserTypoRequest> userTypoRequests =
                List.of(testObjectsFactory.generateFlatEntityWithoutId(UserTypoRequest.class));

        List<UserTypoRequestReadDTO> readDTO =
                moderatorUserTypoRequestService.getModeratorUserTypoRequests(portalUser.getId());
    }
}
