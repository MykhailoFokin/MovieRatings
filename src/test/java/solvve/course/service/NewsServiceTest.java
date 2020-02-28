package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsRepository;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from news",
        " delete from portal_user",
        " delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NewsServiceTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);

        NewsReadDTO readDTO = newsService.getNews(news.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(news, "publisherId");
        Assertions.assertThat(readDTO.getPublisherId()).isEqualTo(news.getPublisher().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsWrongId() {
        newsService.getNews(UUID.randomUUID());
    }

    @Test
    public void testCreateNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();

        NewsCreateDTO create = new NewsCreateDTO();
        create.setPublisherId(portalUser.getId());
        create.setTopic("Main_News");
        create.setDescription("Our main news are absent today!");
        create.setPublished(testObjectsFactory.createInstant(9));
        NewsReadDTO read = newsService.createNews(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        News news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(news, "publisherId");
        Assertions.assertThat(read.getPublisherId()).isEqualTo(news.getPublisher().getId());
    }

    @Test
    public void testPatchNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);

        NewsPatchDTO patch = new NewsPatchDTO();
        patch.setPublisherId(portalUser.getId());
        patch.setTopic("Main_News");
        patch.setDescription("Our main news are absent today!");
        patch.setPublished(testObjectsFactory.createInstant(9));
        NewsReadDTO read = newsService.patchNews(news.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToIgnoringGivenFields(read, "publisher");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(read.getPublisherId());
    }

    @Test
    public void testPatchNewsEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);

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

        Assertions.assertThat(news).isEqualToIgnoringGivenFields(newsAfterUpdate, "publisher");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(newsAfterUpdate.getPublisher().getId());
    }

    @Test
    public void testDeleteNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);

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
        News news = testObjectsFactory.createNews(portalUser);

        NewsPutDTO put = new NewsPutDTO();
        put.setPublisherId(portalUser.getId());
        put.setTopic("Main_News");
        put.setDescription("Our main news are absent today!");
        put.setPublished(testObjectsFactory.createInstant(9));
        NewsReadDTO read = newsService.updateNews(news.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToIgnoringGivenFields(read, "publisher");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(read.getPublisherId());
    }

    @Test
    public void testPutNewsEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        News news = testObjectsFactory.createNews(portalUser);

        NewsPutDTO put = new NewsPutDTO();
        NewsReadDTO read = newsService.updateNews(news.getId(), put);

        Assert.assertNotNull(read.getPublisherId());
        Assert.assertNull(read.getTopic());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getPublished());

        News newsAfterUpdate = newsRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsAfterUpdate.getPublisher().getId());
        Assert.assertNull(newsAfterUpdate.getTopic());
        Assert.assertNull(newsAfterUpdate.getDescription());
        Assert.assertNull(newsAfterUpdate.getPublished());

        Assertions.assertThat(news).isEqualToIgnoringGivenFields(newsAfterUpdate, "publisher", "topic","description",
                "published","updatedAt");
        Assertions.assertThat(news.getPublisher().getId()).isEqualTo(newsAfterUpdate.getPublisher().getId());
    }
}
