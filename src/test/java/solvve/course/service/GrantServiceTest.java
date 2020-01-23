package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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
import solvve.course.repository.GrantRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from grant; delete from portal_user; delete from user_type;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GrantServiceTest {

    @Autowired
    private GrantRepository grantRepository;

    @Autowired
    private GrantService grantService;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    UserTypeService userTypeService;

    private PortalUser portalUser;

    private UserType userType;

    private Grant createGrants() {
        Grant grant = new Grant();
        grant.setId(UUID.randomUUID());
        grant.setUserTypeId(userType);
        grant.setObjectName("Movie");
        grant.setUserPermission(UserPermType.READ);
        grant.setGrantedBy(portalUser);
        return grantRepository.save(grant);
    }

    @Before
    public void setup() {
        if (portalUser == null) {
            userType = new UserType();
            userType.setUserGroup(UserGroupType.USER);
            userType = userTypeRepository.save(userType);

            portalUser = new PortalUser();
            portalUser.setUserType(userType);
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setLogin("Login");
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
        }
    }

    @Transactional
    @Test
    public void testGetGrants() {
        Grant grant = createGrants();

        GrantReadDTO readDTO = grantService.getGrants(grant.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(grant);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGrantsWrongId() {
        grantService.getGrants(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateGrants() {
        GrantCreateDTO create = new GrantCreateDTO();
        create.setUserTypeId(userType);
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);
        create.setGrantedBy(portalUser);
        GrantReadDTO read = grantService.createGrants(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Grant grant = grantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(grant);
    }

    @Transactional
    @Test
    public void testPatchGrants() {
        Grant grant = createGrants();

        GrantPatchDTO patch = new GrantPatchDTO();
        patch.setUserTypeId(userType);
        patch.setObjectName("Movie");
        patch.setUserPermission(UserPermType.READ);
        patch.setGrantedBy(portalUser);
        GrantReadDTO read = grantService.patchGrants(grant.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        grant = grantRepository.findById(read.getId()).get();
        Assertions.assertThat(grant).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchGrantsEmptyPatch() {
        Grant grant = createGrants();

        GrantPatchDTO patch = new GrantPatchDTO();
        GrantReadDTO read = grantService.patchGrants(grant.getId(), patch);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getObjectName());
        Assert.assertNotNull(read.getUserPermission());
        Assert.assertNotNull(read.getGrantedBy());

        Grant grantAfterUpdate = grantRepository.findById(read.getId()).get();

        Assert.assertNotNull(grantAfterUpdate.getUserTypeId());
        Assert.assertNotNull(grantAfterUpdate.getObjectName());
        Assert.assertNotNull(grantAfterUpdate.getUserPermission());
        Assert.assertNotNull(grantAfterUpdate.getGrantedBy());

        Assertions.assertThat(grant).isEqualToComparingFieldByField(grantAfterUpdate);
    }

    @Test
    public void testDeleteGrants() {
        Grant grant = createGrants();

        grantService.deleteGrants(grant.getId());
        Assert.assertFalse(grantRepository.existsById(grant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGrantsNotFound() {
        grantService.deleteGrants(UUID.randomUUID());
    }
}
