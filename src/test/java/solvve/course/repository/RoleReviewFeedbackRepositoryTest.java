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
        Role role = testObjectsFactory.createRole(person);
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
        Role role = testObjectsFactory.createRole(person);
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
    public void testModifiedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback entity = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = roleReviewFeedbackRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewFeedback entity = testObjectsFactory.createRoleReviewFeedback(portalUser, role, roleReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setIsLiked(false);
        roleReviewFeedbackRepository.save(entity);
        entity = roleReviewFeedbackRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
