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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserGrantRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from user_grant","delete from portal_user","delete from user_type"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserGrantServiceTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private UserGrantService userGrantService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private UserGrant createGrants(UserType userType, PortalUser portalUser) {
        UserGrant userGrant = new UserGrant();
        userGrant.setId(UUID.randomUUID());
        userGrant.setUserTypeId(userType);
        userGrant.setObjectName("Movie");
        userGrant.setUserPermission(UserPermType.READ);
        userGrant.setGrantedBy(portalUser);
        return userGrantRepository.save(userGrant);
    }

    private UserType createUserType() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);
        return userType;
    }

    private PortalUser createPortalUser(UserType userType) {
        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userType);
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);
        return portalUser;
    }

    @Transactional
    @Test
    public void testGetGrants() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        UserGrantReadDTO readDTO = userGrantService.getGrants(userGrant.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(userGrant,"userTypeId", "grantedBy");
        Assertions.assertThat(readDTO.getUserTypeId()).isEqualTo(userGrant.getUserTypeId().getId());
        Assertions.assertThat(readDTO.getGrantedBy()).isEqualTo(userGrant.getGrantedBy().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGrantsWrongId() {
        userGrantService.getGrants(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateGrants() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);

        UserGrantCreateDTO create = new UserGrantCreateDTO();
        create.setUserTypeId(userType.getId());
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);
        create.setGrantedBy(portalUser.getId());
        UserGrantReadDTO read = userGrantService.createGrants(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserGrant userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(userGrant,"userTypeId", "grantedBy");
        Assertions.assertThat(read.getUserTypeId()).isEqualTo(userGrant.getUserTypeId().getId());
        Assertions.assertThat(read.getGrantedBy()).isEqualTo(userGrant.getGrantedBy().getId());
    }

    @Transactional
    @Test
    public void testPatchGrants() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        UserGrantPatchDTO patch = new UserGrantPatchDTO();
        patch.setUserTypeId(userType.getId());
        patch.setObjectName("Movie");
        patch.setUserPermission(UserPermType.READ);
        patch.setGrantedBy(portalUser.getId());
        UserGrantReadDTO read = userGrantService.patchGrants(userGrant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(userGrant).isEqualToIgnoringGivenFields(read,"userTypeId", "grantedBy");
        Assertions.assertThat(userGrant.getUserTypeId().getId()).isEqualTo(read.getUserTypeId());
        Assertions.assertThat(userGrant.getGrantedBy().getId()).isEqualTo(read.getGrantedBy());
    }

    @Transactional
    @Test
    public void testPatchGrantsEmptyPatch() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        UserGrantPatchDTO patch = new UserGrantPatchDTO();
        UserGrantReadDTO read = userGrantService.patchGrants(userGrant.getId(), patch);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getObjectName());
        Assert.assertNotNull(read.getUserPermission());
        Assert.assertNotNull(read.getGrantedBy());

        UserGrant userGrantAfterUpdate = userGrantRepository.findById(read.getId()).get();

        Assert.assertNotNull(userGrantAfterUpdate.getUserTypeId());
        Assert.assertNotNull(userGrantAfterUpdate.getObjectName());
        Assert.assertNotNull(userGrantAfterUpdate.getUserPermission());
        Assert.assertNotNull(userGrantAfterUpdate.getGrantedBy());

        Assertions.assertThat(userGrant).isEqualToComparingFieldByField(userGrantAfterUpdate);
    }

    @Test
    public void testDeleteGrants() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        userGrantService.deleteGrants(userGrant.getId());
        Assert.assertFalse(userGrantRepository.existsById(userGrant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGrantsNotFound() {
        userGrantService.deleteGrants(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutGrants() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        UserGrantPutDTO put = new UserGrantPutDTO();
        put.setUserTypeId(userType.getId());
        put.setObjectName("Movie");
        put.setUserPermission(UserPermType.READ);
        put.setGrantedBy(portalUser.getId());
        UserGrantReadDTO read = userGrantService.putGrants(userGrant.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        userGrant = userGrantRepository.findById(read.getId()).get();
        Assertions.assertThat(userGrant).isEqualToIgnoringGivenFields(read,"userTypeId", "grantedBy");
        Assertions.assertThat(userGrant.getUserTypeId().getId()).isEqualTo(read.getUserTypeId());
        Assertions.assertThat(userGrant.getGrantedBy().getId()).isEqualTo(read.getGrantedBy());
    }

    @Transactional
    @Test
    public void testPutGrantsEmptyPut() {
        UserType userType = createUserType();
        PortalUser portalUser = createPortalUser(userType);
        UserGrant userGrant = createGrants(userType, portalUser);

        UserGrantPutDTO put = new UserGrantPutDTO();
        UserGrantReadDTO read = userGrantService.putGrants(userGrant.getId(), put);

        Assert.assertNull(read.getUserTypeId());
        Assert.assertNull(read.getObjectName());
        Assert.assertNull(read.getUserPermission());
        Assert.assertNull(read.getGrantedBy());

        UserGrant userGrantAfterUpdate = userGrantRepository.findById(read.getId()).get();

        Assert.assertNull(userGrantAfterUpdate.getUserTypeId().getId());
        Assert.assertNull(userGrantAfterUpdate.getObjectName());
        Assert.assertNull(userGrantAfterUpdate.getUserPermission());
        Assert.assertNull(userGrantAfterUpdate.getGrantedBy().getId());

        Assertions.assertThat(userGrant).isEqualToComparingFieldByField(userGrantAfterUpdate);
    }
}
