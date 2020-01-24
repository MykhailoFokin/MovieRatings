package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from news; delete from portal_user; delete from user_type;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NewsServiceTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private News createNews(PortalUser portalUser) {
        News news = new News();
        news.setId(UUID.randomUUID());
        news.setUserId(portalUser);
        news.setTopic("Main_News");
        news.setDescription("Our main news are absent today!");
        news.setPublished(Instant.now());
        return newsRepository.save(news);
    }

    private PortalUser createPortalUser() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setLogin("Login");
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setUserType(userType);
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    @Transactional
    @Test
    public void testGetNews() {
        PortalUser portalUser = createPortalUser();
        News news = createNews(portalUser);

        NewsReadDTO readDTO = newsService.getNews(news.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(news);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsWrongId() {
        newsService.getNews(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateNews() {
        PortalUser portalUser = createPortalUser();

        NewsCreateDTO create = new NewsCreateDTO();
        create.setUserId(portalUser);
        create.setTopic("Main_News");
        create.setDescription("Our main news are absent today!");
        create.setPublished(Instant.now());
        NewsReadDTO read = newsService.createNews(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        News news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(news);
    }

    @Transactional
    @Test
    public void testPatchNews() {
        PortalUser portalUser = createPortalUser();
        News news = createNews(portalUser);

        NewsPatchDTO patch = new NewsPatchDTO();
        patch.setUserId(portalUser);
        patch.setTopic("Main_News");
        patch.setDescription("Our main news are absent today!");
        patch.setPublished(Instant.now());
        NewsReadDTO read = newsService.patchNews(news.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchNewsEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        News news = createNews(portalUser);

        NewsPatchDTO patch = new NewsPatchDTO();
        NewsReadDTO read = newsService.patchNews(news.getId(), patch);

        Assert.assertNotNull(read.getUserId());
        Assert.assertNotNull(read.getTopic());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getPublished());

        News newsAfterUpdate = newsRepository.findById(read.getId()).get();

        Assert.assertNotNull(newsAfterUpdate.getUserId());
        Assert.assertNotNull(newsAfterUpdate.getTopic());
        Assert.assertNotNull(newsAfterUpdate.getDescription());
        Assert.assertNotNull(newsAfterUpdate.getPublished());

        Assertions.assertThat(news).isEqualToComparingFieldByField(newsAfterUpdate);
    }

    @Test
    public void testDeleteNews() {
        PortalUser portalUser = createPortalUser();
        News news = createNews(portalUser);

        newsService.deleteNews(news.getId());
        Assert.assertFalse(newsRepository.existsById(news.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNewsNotFound() {
        newsService.deleteNews(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutNews() {
        PortalUser portalUser = createPortalUser();
        News news = createNews(portalUser);

        NewsPutDTO put = new NewsPutDTO();
        put.setUserId(portalUser);
        put.setTopic("Main_News");
        put.setDescription("Our main news are absent today!");
        put.setPublished(Instant.now());
        NewsReadDTO read = newsService.putNews(news.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(news).isEqualToComparingFieldByField(read);
    }
}
