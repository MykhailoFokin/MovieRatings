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
@Sql(statements = "delete from role_review_feedback; delete from role_review; delete from portal_user; delete from user_types; delete from role; delete from persons; ", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleReviewFeedbackRepositoryTest {

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private PersonsRepository personsRepository;

    @Test
    public void testSave() {
        Persons persons = new Persons();
        persons = personsRepository.save(persons);

        Role role = new Role();
        //role.setId(UUID.randomUUID());
        role.setTitle("Role Test");
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");
        role.setPersonId(persons);
        role = roleRepository.save(role);

        UserTypes userTypes = new UserTypes();
        userTypes.setUserGroup(UserGroupType.USER);
        userTypes = userTypesRepository.save(userTypes);

        PortalUser portalUser = new PortalUser();
        portalUser.setLogin("Login");
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setUserType(userTypes);
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

        RoleReviewFeedback r = new RoleReviewFeedback();
        r.setRoleId(role);
        r.setRoleReviewId(roleReview);
        r.setUserId(portalUser);
        r = roleReviewFeedbackRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleReviewFeedbackRepository.findById(r.getId()).isPresent());
    }
}
