package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsFeedbackRepository;
import solvve.course.repository.NewsRepository;

import java.util.UUID;

public class NewsServiceTest extends BaseTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Test
    public void testGetNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsReadDTO readDTO = newsService.getNews(news.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(news, "publisherId","movieId");
        Assertions.assertThat(readDTO.getPublisherId()).isEqualTo(news.getPublisher().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(news.getMovie().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsWrongId() {
        newsService.getNews(UUID.randomUUID());
    }

    @Test
    public void testCreateNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();

        NewsCreateDTO create = testObjectsFactory.createNewsCreateDTO();
        create.setPublisherId(portalUser.getId());
        create.setMovieId(movie.getId());
        NewsReadDTO read = newsService.createNews(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        News news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(news, "publisherId","movieId");
        Assertions.assertThat(read.getPublisherId()).isEqualTo(news.getPublisher().getId());
        Assertions.assertThat(read.getMovieId()).isEqualTo(news.getMovie().getId());
    }

    @Test
    public void testPatchNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsPatchDTO patch = testObjectsFactory.createNewsPatchDTO();
        patch.setPublisherId(portalUser.getId());
        patch.setMovieId(movie.getId());
        NewsReadDTO read = newsService.patchNews(news.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToIgnoringGivenFields(read, "publisher",
                "newsUserReviews", "newsUserReviewNotes", "userTypoRequests", "newsFeedbacks", "movie");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(read.getPublisherId());
        Assertions.assertThat(news.getMovie().getId()).isEqualTo(read.getMovieId());
    }

    @Test
    public void testPatchNewsEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsPatchDTO patch = new NewsPatchDTO();
        NewsReadDTO read = newsService.patchNews(news.getId(), patch);

        Assert.assertNotNull(read.getPublisherId());
        Assert.assertNotNull(read.getTopic());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getPublished());

        News newsAfterUpdate = newsRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsAfterUpdate.getPublisher());
        Assert.assertNotNull(newsAfterUpdate.getTopic());
        Assert.assertNotNull(newsAfterUpdate.getDescription());
        Assert.assertNotNull(newsAfterUpdate.getPublished());

        Assertions.assertThat(news).isEqualToIgnoringGivenFields(newsAfterUpdate, "publisher",
                "newsUserReviews", "newsUserReviewNotes", "userTypoRequests", "newsFeedbacks", "movie");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(newsAfterUpdate.getPublisher().getId());
        Assertions.assertThat(news.getMovie().getId()).isEqualTo(newsAfterUpdate.getMovie().getId());
    }

    @Test
    public void testDeleteNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        newsService.deleteNews(news.getId());
        Assert.assertFalse(newsRepository.existsById(news.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNewsNotFound() {
        newsService.deleteNews(UUID.randomUUID());
    }

    @Test
    public void testPutNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsPutDTO put = testObjectsFactory.createNewsPutDTO();
        put.setPublisherId(portalUser.getId());
        put.setMovieId(movie.getId());
        NewsReadDTO read = newsService.updateNews(news.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToIgnoringGivenFields(read, "publisher",
                "newsUserReviews", "newsUserReviewNotes", "userTypoRequests", "newsFeedbacks", "movie");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(read.getPublisherId());
        Assertions.assertThat(news.getMovie().getId()).isEqualTo(read.getMovieId());
    }

    @Test
    public void testPutNewsEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsPutDTO put = new NewsPutDTO();
        NewsReadDTO read = newsService.updateNews(news.getId(), put);

        Assert.assertNotNull(read.getPublisherId());
        Assert.assertNotNull(read.getTopic());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNull(read.getPublished());

        News newsAfterUpdate = newsRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsAfterUpdate.getPublisher().getId());
        Assert.assertNotNull(newsAfterUpdate.getTopic());
        Assert.assertNotNull(newsAfterUpdate.getDescription());
        Assert.assertNull(newsAfterUpdate.getPublished());
        Assert.assertNull(newsAfterUpdate.getMovie());

        Assertions.assertThat(news).isEqualToIgnoringGivenFields(newsAfterUpdate, "publisher", "topic","description",
                "published", "updatedAt", "newsUserReviews", "newsUserReviewNotes", "userTypoRequests",
                "newsFeedbacks", "movie");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(newsAfterUpdate.getPublisher().getId());
    }

    @Test
    public void testUpdateAverageRatingOfNews() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        News news = testObjectsFactory.createNews(portalUser1, movie);
        testObjectsFactory.createNewsFeedback(portalUser1, news, false);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createNewsFeedback(portalUser2, news, true);

        newsService.updateAverageRatingOfNews(news.getId());
        news = newsRepository.findById(news.getId()).get();
        Assert.assertEquals(0.5, news.getNewsRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testUpdateAverageRatingOfNewsEmptyNews() {
        Assertions.assertThatThrownBy(()-> newsService.updateAverageRatingOfNews(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void testGetNewsWithMovieThatHaveManyRoles() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        Person person1 = testObjectsFactory.createPerson();
        Person person2 = testObjectsFactory.createPerson();
        Role role1 = testObjectsFactory.createRole(person1, movie);
        Role role2 = testObjectsFactory.createRole(person2, movie);
        News news = testObjectsFactory.createNews(portalUser, movie);

        NewsReadDTO readDTO = newsService.getNews(news.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(news, "publisherId","movieId");
        Assertions.assertThat(readDTO.getPublisherId()).isEqualTo(news.getPublisher().getId());
        Assertions.assertThat(readDTO.getMovieId()).isEqualTo(news.getMovie().getId());
    }
}
