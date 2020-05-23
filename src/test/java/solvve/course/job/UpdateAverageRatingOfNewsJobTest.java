package solvve.course.job;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Movie;
import solvve.course.domain.News;
import solvve.course.domain.PortalUser;
import solvve.course.repository.NewsRepository;
import solvve.course.service.NewsService;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from news_feedback",
        "delete from news",
        "delete from portal_user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UpdateAverageRatingOfNewsJobTest {

    @Autowired
    private UpdateAverageRatingOfNewsJob updateAverageRatingOfNewsJob;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private NewsRepository newsRepository;

    @SpyBean
    private NewsService newsService;

    @Test
    public void testUpdateAverageRatingOfNews() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNewsWithoutRating(portalUser1, movie);
        testObjectsFactory.createNewsFeedback(portalUser1, news, Boolean.TRUE);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser2, news, Boolean.FALSE);

        updateAverageRatingOfNewsJob.updateAverageRatingOfNews();

        news = newsRepository.findById(news.getId()).get();
        Assert.assertEquals(0.5, news.getNewsRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testNewsUpdatedIndependently() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNewsWithoutRating(portalUser, movie);
        testObjectsFactory.createNewsFeedback(portalUser, news, Boolean.TRUE);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser2, news, Boolean.FALSE);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocationOnMock -> {
            if (failedId[0] == null) {
                failedId[0] = invocationOnMock.getArgument(0);
                throw new RuntimeException();
            }
            return invocationOnMock.callRealMethod();
        }).when(newsService).updateAverageRatingOfNews(Mockito.any());

        updateAverageRatingOfNewsJob.updateAverageRatingOfNews();

        for (News m : newsRepository.findAll()) {
            if (m.getId().equals(failedId[0])) {
                Assert.assertNull(m.getNewsRating());
            } else {
                Assert.assertNotNull(m.getNewsRating());
            }
        }
    }
}
