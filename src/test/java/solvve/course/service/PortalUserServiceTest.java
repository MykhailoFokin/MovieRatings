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
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserType;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserPutDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from portal_user",
        " delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserServiceTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetPortalUsers() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserReadDTO readDTO = portalUserService.getPortalUser(portalUser.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(portalUser,"userTypeId");
        Assertions.assertThat(readDTO.getUserTypeId())
                .isEqualToComparingFieldByField(portalUser.getUserType().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUsersWrongId() {
        portalUserService.getPortalUser(UUID.randomUUID());
    }

    @Test
    public void testCreatePortalUsers() {
        UserType userType = testObjectsFactory.createUserType();

        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setUserTypeId(userType.getId());
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        create.setLogin("Login");
        create.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.createPortalUser(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        PortalUser portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(portalUser,"userTypeId");
        Assertions.assertThat(read.getUserTypeId()).isEqualTo(portalUser.getUserType().getId());
    }

    @Test
    public void testPatchPortalUser() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        patch.setUserTypeId(userType.getId());
        patch.setSurname("Surname");
        patch.setName("Name");
        patch.setMiddleName("MiddleName");
        patch.setLogin("Login");
        patch.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(read,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks"
                ,"roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator");
        Assertions.assertThat(portalUser.getUserType().getId()).isEqualTo(read.getUserTypeId());
    }

    @Test
    public void testPatchPortalUserEmptyPatch() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getSurname());
        Assert.assertNotNull(read.getName());
        Assert.assertNotNull(read.getMiddleName());
        Assert.assertNotNull(read.getLogin());
        Assert.assertNotNull(read.getUserConfidence());

        PortalUser portalUserAfterUpdate = portalUserRepository.findById(read.getId()).get();

        Assert.assertNotNull(portalUserAfterUpdate.getUserType());
        Assert.assertNotNull(portalUserAfterUpdate.getSurname());
        Assert.assertNotNull(portalUserAfterUpdate.getName());
        Assert.assertNotNull(portalUserAfterUpdate.getMiddleName());
        Assert.assertNotNull(portalUserAfterUpdate.getLogin());
        Assert.assertNotNull(portalUserAfterUpdate.getUserConfidence());

        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(portalUserAfterUpdate,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks"
                ,"roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator");
    }

    @Test
    public void testDeletePortalUser() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        portalUserService.deletePortalUser(portalUser.getId());
        Assert.assertFalse(portalUserRepository.existsById(portalUser.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePortalUserNotFound() {
        portalUserService.deletePortalUser(UUID.randomUUID());
    }

    @Test
    public void testPutPortalUser() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserPutDTO put = new PortalUserPutDTO();
        put.setUserTypeId(userType.getId());
        put.setSurname("Surname");
        put.setName("Name");
        put.setMiddleName("MiddleName");
        put.setLogin("Login");
        put.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.updatePortalUser(portalUser.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(read,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks",
                "roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator");
        Assertions.assertThat(portalUser.getUserType().getId()).isEqualTo(read.getUserTypeId());
    }

    @Test
    public void testPutPortalUserEmptyPut() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserPutDTO put = new PortalUserPutDTO();
        PortalUserReadDTO read = portalUserService.updatePortalUser(portalUser.getId(), put);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNull(read.getSurname());
        Assert.assertNull(read.getName());
        Assert.assertNull(read.getMiddleName());
        Assert.assertNull(read.getLogin());
        Assert.assertNull(read.getUserConfidence());

        PortalUser portalUserAfterUpdate = portalUserRepository.findById(read.getId()).get();

        Assert.assertNotNull(portalUserAfterUpdate.getUserType().getId());
        Assert.assertNull(portalUserAfterUpdate.getSurname());
        Assert.assertNull(portalUserAfterUpdate.getName());
        Assert.assertNull(portalUserAfterUpdate.getMiddleName());
        Assert.assertNull(portalUserAfterUpdate.getLogin());
        Assert.assertNull(portalUserAfterUpdate.getUserConfidence());
    }
}
