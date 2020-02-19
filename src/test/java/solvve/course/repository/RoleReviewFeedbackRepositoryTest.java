package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from role_review_feedback",
        "delete from role_review",
        "delete from portal_user",
        "delete from user_type",
        "delete from role",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleReviewFeedbackRepositoryTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewFeedback r = new RoleReviewFeedback();
        r.setRoleId(role);
        r.setRoleReviewId(roleReview);
        r.setUserId(portalUser);
        r = roleReviewFeedbackRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleReviewFeedbackRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback entity = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleReviewFeedbackRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback entity = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = roleReviewFeedbackRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback entity = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setIsLiked(false);
        roleReviewFeedbackRepository.save(entity);
        entity = roleReviewFeedbackRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
