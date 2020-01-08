package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from news", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NewsServiceTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    private PortalUserReadDTO portalUserReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Before
    public void setup() {
        if (portalUserReadDTO ==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            PortalUser portalUser = new PortalUser();
            portalUser.setLogin("Login");
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setUserType(userTypes.getId());
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
            portalUserReadDTO = portalUserService.getPortalUsers(portalUser.getId());
        }
    }

    @Test
    public void testGetNews() {
        News news = new News();
        news.setId(UUID.randomUUID());
        news.setUserId(portalUserReadDTO.getId());
        news.setTopic("Main_News");
        news.setDescription("Our main news are absent today!");
        news.setPublished(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        news = newsRepository.save(news);

        NewsReadDTO readDTO = newsService.getNews(news.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(news);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNewsWrongId() {
        newsService.getNews(UUID.randomUUID());
    }

    @Test
    public void testCreateNews() {
        NewsCreateDTO create = new NewsCreateDTO();
        create.setUserId(portalUserReadDTO.getId());
        create.setTopic("Main_News");
        create.setDescription("Our main news are absent today!");
        create.setPublished(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        NewsReadDTO read = newsService.createNews(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        News news = newsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(news);
    }
}
