package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserPutDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.ArrayList;
import java.util.UUID;

public class PortalUserServiceTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    public void testGetPortalUsers() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserReadDTO readDTO = portalUserService.getPortalUser(portalUser.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(portalUser,"userTypeId","userRoleIds");
        Assertions.assertThat(readDTO.getUserTypeId())
                .isEqualToComparingFieldByField(portalUser.getUserType().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUsersWrongId() {
        portalUserService.getPortalUser(UUID.randomUUID());
    }

    @Test
    public void testCreatePortalUsers() {
        PortalUserCreateDTO create = testObjectsFactory.createPortalUserCreateDTO();
        create.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.createPortalUser(create);
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(read, "userTypeId", "userRoleIds");

        PortalUser portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(portalUser,"userTypeId", "userRoleIds");
        Assertions.assertThat(read.getUserTypeId()).isEqualTo(portalUser.getUserType().getId());
    }

    @Test
    public void testPatchPortalUser() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        PortalUserPatchDTO patch = testObjectsFactory.createPortalUserPatchDTO();
        patch.setUserRoleIds(null);
        patch.setUserTypeId(userType.getId());
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(read,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks"
                ,"roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator","userRoles");
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
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator","userRoles");
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

        PortalUserPutDTO put = testObjectsFactory.createPortalUserPutDTO();
        put.setUserTypeId(userType.getId());
        put.setUserRoleIds(null);
        PortalUserReadDTO read = portalUserService.updatePortalUser(portalUser.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(read,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks",
                "roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator","userRoles");
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
        Assert.assertNotNull(read.getLogin());
        Assert.assertNull(read.getUserConfidence());

        PortalUser portalUserAfterUpdate = portalUserRepository.findById(read.getId()).get();

        Assert.assertNotNull(portalUserAfterUpdate.getUserType().getId());
        Assert.assertNull(portalUserAfterUpdate.getSurname());
        Assert.assertNull(portalUserAfterUpdate.getName());
        Assert.assertNull(portalUserAfterUpdate.getMiddleName());
        Assert.assertNotNull(portalUserAfterUpdate.getLogin());
        Assert.assertNull(portalUserAfterUpdate.getUserConfidence());
    }

    @Test
    public void testPatchPortalUserAddNewRole() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        UserRole userRole = userRoleRepository.findByUserGroupType(UserGroupType.ADMIN);

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        patch.getUserRoleIds().add(userRole.getId());
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToIgnoringGivenFields(read,
                "userType", "userGrants","movieReview","movieReviewModerator",
                "movieReviewCompliants","movieReviewCompliantsModerator","movieReviewFeedbacks"
                ,"roleReviews","roleReviewsModerator","roleReviewCompliants",
                "roleReviewCompliantsModerator","roleReviewFeedbacks","movieVotes","news","roleVotes","visits",
                "newsUserReviews","newsUserReviewNotes","newsUserReviewsModerator","userRoles");
        Assertions.assertThat(portalUser.getUserType().getId()).isEqualTo(read.getUserTypeId());
    }
}
