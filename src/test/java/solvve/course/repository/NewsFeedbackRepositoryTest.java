package solvve.course.repository;

import io.swagger.models.auth.In;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.News;
import solvve.course.domain.PortalUser;

public class NewsFeedbackRepositoryTest extends BaseTest {

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Test
    public void testCalcAverageMarkOfNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNewsWithoutRating(portalUser, movie);
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser1, news, Boolean.TRUE);
        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser2, news, Boolean.FALSE);
        Double newsRating =
                newsFeedbackRepository.countByNewsIdAndIsLikedTrue(news.getId()).doubleValue() /
                        newsFeedbackRepository.countByNewsId(news.getId());

        Assert.assertEquals(0.5, newsRating, Double.MIN_NORMAL);
    }

    @Test
    public void testCalcLikesCountOfNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNewsWithoutRating(portalUser, movie);
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser1, news, Boolean.TRUE);
        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser2, news, Boolean.FALSE);

        Integer likesCount = 2;
        Assert.assertEquals(likesCount, newsFeedbackRepository.countByNewsId(news.getId()));
    }
}
