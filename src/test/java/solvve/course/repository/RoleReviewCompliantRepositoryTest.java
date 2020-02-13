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
@Sql(statements = {"delete from role_review_compliant",
        "delete from role_review",
        "delete from portal_user",
        "delete from user_type",
        "delete from role",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleReviewCompliantRepositoryTest {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testSave() {
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewCompliant r = new RoleReviewCompliant();
        r.setUserId(portalUser);
        r.setRoleId(role);
        r.setRoleReviewId(roleReview);
        r.setDescription("Just punish him!");
        r.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        r.setModeratorId(portalUser);
        r = roleReviewCompliantRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleReviewCompliantRepository.findById(r.getId()).isPresent());
    }

    @Test
    public void testCteatedAtIsSet() {
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant entity = testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = roleReviewCompliantRepository.findById(entity.getId()).get();

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
        RoleReviewCompliant entity = testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = roleReviewCompliantRepository.findById(entity.getId()).get();

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
        RoleReviewCompliant entity = testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setDescription("NewNameTest");
        roleReviewCompliantRepository.save(entity);
        entity = roleReviewCompliantRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
