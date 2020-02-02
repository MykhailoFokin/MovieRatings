package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;

import java.util.UUID;

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
    private RoleRepository roleRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSave() {
        Person person = new Person();
        person.setName("Name");
        person = personRepository.save(person);

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");
        role.setPersonId(person);
        role = roleRepository.save(role);

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

        RoleReview roleReview = new RoleReview();
        roleReview.setId(UUID.randomUUID());
        roleReview.setUserId(portalUser);
        roleReview.setRoleId(role);
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReview.setModeratorId(portalUser);
        roleReview = roleReviewRepository.save(roleReview);

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
}
