package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import solvve.course.BaseTest;
import solvve.course.domain.Movie;
import solvve.course.domain.News;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserType;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NewsRepositoryTest extends BaseTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void testSave() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        Movie movie = testObjectsFactory.createMovie();

        News r = new News();
        r.setPublisher(portalUser);
        r.setTopic("Topic");
        r.setDescription("Desc");
        r.setMovie(movie);
        r = newsRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(newsRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCreatedAtIsSet() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        Movie movie = testObjectsFactory.createMovie();
        News entity = testObjectsFactory.createNews(portalUser, movie);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = newsRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        Movie movie = testObjectsFactory.createMovie();
        News entity = testObjectsFactory.createNews(portalUser, movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = newsRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsModified() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        Movie movie = testObjectsFactory.createMovie();
        News entity = testObjectsFactory.createNews(portalUser, movie);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setTopic("NewNameTest");
        entity = newsRepository.save(entity);
        UUID entityId = entity.getId();

        testObjectsFactory.inTransaction(() -> {
            News entityAfterReload = newsRepository.findById(entityId).get();

            Instant updatedAtAfterReload = entityAfterReload.getUpdatedAt();
            Assert.assertNotNull(updatedAtAfterReload);
            Assert.assertTrue(updatedAtBeforeReload.isBefore(updatedAtAfterReload));
        });
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveNewsValidation() {
        News entity = new News();
        newsRepository.save(entity);
    }

    @Test
    public void testGetIdsOfNews() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Movie movie = testObjectsFactory.createMovie();
        Set<UUID> expectedIdsOfNews = new HashSet<>();
        expectedIdsOfNews.add(testObjectsFactory.createNews(portalUser, movie).getId());
        expectedIdsOfNews.add(testObjectsFactory.createNews(portalUser, movie).getId());

        testObjectsFactory.inTransaction(()-> {
            Assert.assertEquals(expectedIdsOfNews, newsRepository.getIdsOfNews().collect(Collectors.toSet()));
        });
    }
}
