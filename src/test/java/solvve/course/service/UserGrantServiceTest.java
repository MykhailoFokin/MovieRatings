package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserGrantRepository;

import java.util.UUID;

public class UserGrantServiceTest extends BaseTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private UserGrantService userGrantService;

    @Test
    public void testGetGrants() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        UserGrantReadDTO readDTO = userGrantService.getGrants(userGrant.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(userGrant,
                "userTypeId", "grantedById");
        Assertions.assertThat(readDTO.getUserTypeId()).isEqualTo(userGrant.getUserType().getId());
        Assertions.assertThat(readDTO.getGrantedById()).isEqualTo(userGrant.getGrantedBy().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGrantsWrongId() {
        userGrantService.getGrants(UUID.randomUUID());
    }

    @Test
    public void testCreateGrants() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);

        UserGrantCreateDTO create = testObjectsFactory.createUserGrantCreateDTO();
        create.setUserTypeId(userType.getId());
        create.setGrantedById(portalUser.getId());
        UserGrantReadDTO read = userGrantService.createGrants(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserGrant userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(userGrant,
                "userTypeId", "grantedById");
        Assertions.assertThat(read.getUserTypeId()).isEqualTo(userGrant.getUserType().getId());
        Assertions.assertThat(read.getGrantedById()).isEqualTo(userGrant.getGrantedBy().getId());
    }

    @Test
    public void testPatchGrants() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        UserGrantPatchDTO patch = testObjectsFactory.createUserGrantPatchDTO();
        patch.setUserTypeId(userType.getId());
        patch.setGrantedById(portalUser.getId());
        UserGrantReadDTO read = userGrantService.patchGrants(userGrant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(userGrant).isEqualToIgnoringGivenFields(read,
                "userType", "grantedBy");
        Assertions.assertThat(userGrant.getUserType().getId()).isEqualTo(read.getUserTypeId());
        Assertions.assertThat(userGrant.getGrantedBy().getId()).isEqualTo(read.getGrantedById());
    }

    @Test
    public void testPatchGrantsEmptyPatch() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        UserGrantPatchDTO patch = new UserGrantPatchDTO();
        UserGrantReadDTO read = userGrantService.patchGrants(userGrant.getId(), patch);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getObjectName());
        Assert.assertNotNull(read.getUserPermission());
        Assert.assertNotNull(read.getGrantedById());

        UserGrant userGrantAfterUpdate = userGrantRepository.findById(read.getId()).get();

        Assert.assertNotNull(userGrantAfterUpdate.getUserType());
        Assert.assertNotNull(userGrantAfterUpdate.getObjectName());
        Assert.assertNotNull(userGrantAfterUpdate.getUserPermission());
        Assert.assertNotNull(userGrantAfterUpdate.getGrantedBy());

        Assertions.assertThat(userGrant).isEqualToIgnoringGivenFields(userGrantAfterUpdate,
                "userType", "grantedBy");
        Assertions.assertThat(userGrant.getUserType().getId())
                .isEqualTo(userGrantAfterUpdate.getUserType().getId());
        Assertions.assertThat(userGrant.getGrantedBy().getId())
                .isEqualTo(userGrantAfterUpdate.getGrantedBy().getId());
    }

    @Test
    public void testDeleteGrants() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        userGrantService.deleteGrants(userGrant.getId());
        Assert.assertFalse(userGrantRepository.existsById(userGrant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGrantsNotFound() {
        userGrantService.deleteGrants(UUID.randomUUID());
    }

    @Test
    public void testPutGrants() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        UserGrantPutDTO put = testObjectsFactory.createUserGrantPutDTO();
        put.setUserTypeId(userType.getId());
        put.setGrantedById(portalUser.getId());
        UserGrantReadDTO read = userGrantService.updateGrants(userGrant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(userGrant).isEqualToIgnoringGivenFields(read,
                "userType", "grantedBy");
        Assertions.assertThat(userGrant.getUserType().getId()).isEqualTo(read.getUserTypeId());
        Assertions.assertThat(userGrant.getGrantedBy().getId()).isEqualTo(read.getGrantedById());
    }

    @Test
    public void testPutGrantsEmptyPut() {
        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.createPortalUser(userType);
        UserGrant userGrant = testObjectsFactory.createGrants(userType, portalUser);

        UserGrantPutDTO put = new UserGrantPutDTO();
        UserGrantReadDTO read = userGrantService.updateGrants(userGrant.getId(), put);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getObjectName());
        Assert.assertNotNull(read.getUserPermission());
        Assert.assertNotNull(read.getGrantedById());

        UserGrant userGrantAfterUpdate = userGrantRepository.findById(read.getId()).get();

        Assert.assertNotNull(userGrantAfterUpdate.getUserType().getId());
        Assert.assertNotNull(userGrantAfterUpdate.getObjectName());
        Assert.assertNotNull(userGrantAfterUpdate.getUserPermission());
        Assert.assertNotNull(userGrantAfterUpdate.getGrantedBy().getId());

        Assertions.assertThat(userGrant).isEqualToComparingOnlyGivenFields(userGrantAfterUpdate, "id");
        Assertions.assertThat(userGrant.getUserType().getId())
                .isEqualTo(userGrantAfterUpdate.getUserType().getId());
        Assertions.assertThat(userGrant.getGrantedBy().getId())
                .isEqualTo(userGrantAfterUpdate.getGrantedBy().getId());
    }
}
