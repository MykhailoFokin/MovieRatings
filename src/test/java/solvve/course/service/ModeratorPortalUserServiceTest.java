package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.repository.PortalUserRepository;

import java.util.List;

public class ModeratorPortalUserServiceTest extends BaseTest {

    @Autowired
    private ModeratorPortalUserService moderatorPortalUserService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testGetMovieReviewCompliant() {
        PortalUser contentManager = testObjectsFactory.createPortalUser(UserConfidenceType.NORMAL);
        PortalUser portalUser = testObjectsFactory.createPortalUser(UserConfidenceType.BLOCKED);

        List<PortalUserReadDTO> readDTO =
                moderatorPortalUserService.getBlockedPortalUsers(contentManager.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(portalUser.getId());
    }

    @Test
    public void testPatchMovieReviewCompliant() {
        PortalUser contentManager = testObjectsFactory.createPortalUser(UserConfidenceType.NORMAL);
        PortalUser portalUser = testObjectsFactory.createPortalUser(UserConfidenceType.NORMAL);

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        patch.setUserConfidence(UserConfidenceType.BLOCKED);
        PortalUserReadDTO read = moderatorPortalUserService.blockUnblockPortalUser(contentManager.getId(),
                portalUser.getId(), patch);

        Assert.assertEquals(patch.getUserConfidence(), read.getUserConfidence());

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assert.assertEquals(portalUser.getUserConfidence(), read.getUserConfidence());
    }
}
