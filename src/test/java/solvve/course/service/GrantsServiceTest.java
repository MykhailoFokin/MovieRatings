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
import solvve.course.repository.GrantsRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from grants; delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GrantsServiceTest {

    @Autowired
    private GrantsRepository grantsRepository;

    @Autowired
    private GrantsService grantsService;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired UserTypesService userTypesService;

    private PortalUser portalUser;

    private UserTypes userTypes;

    private Grants createGrants() {
        Grants grants = new Grants();
        grants.setId(UUID.randomUUID());
        grants.setUserTypeId(userTypes);
        grants.setObjectName("Movie");
        grants.setUserPermission(UserPermType.READ);
        grants.setGrantedBy(portalUser);
        return grantsRepository.save(grants);
    }

    @Before
    public void setup() {
        if (portalUser == null) {
            userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            portalUser = new PortalUser();
            portalUser.setUserType(userTypes);
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
        Grants grants = createGrants();

        GrantsReadDTO readDTO = grantsService.getGrants(grants.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(grants);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGrantsWrongId() {
        grantsService.getGrants(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateGrants() {
        GrantsCreateDTO create = new GrantsCreateDTO();
        create.setUserTypeId(userTypes);
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);
        create.setGrantedBy(portalUser);
        GrantsReadDTO read = grantsService.createGrants(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Grants grants = grantsRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(grants);
    }

    @Transactional
    @Test
    public void testPatchGrants() {
        Grants grants = createGrants();

        GrantsPatchDTO patch = new GrantsPatchDTO();
        patch.setUserTypeId(userTypes);
        patch.setObjectName("Movie");
        patch.setUserPermission(UserPermType.READ);
        patch.setGrantedBy(portalUser);
        GrantsReadDTO read = grantsService.patchGrants(grants.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        grants = grantsRepository.findById(read.getId()).get();
        Assertions.assertThat(grants).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchGrantsEmptyPatch() {
        Grants grants = createGrants();

        GrantsPatchDTO patch = new GrantsPatchDTO();
        GrantsReadDTO read = grantsService.patchGrants(grants.getId(), patch);

        Assert.assertNotNull(read.getUserTypeId());
        Assert.assertNotNull(read.getObjectName());
        Assert.assertNotNull(read.getUserPermission());
        Assert.assertNotNull(read.getGrantedBy());

        Grants grantsAfterUpdate = grantsRepository.findById(read.getId()).get();

        Assert.assertNotNull(grantsAfterUpdate.getUserTypeId());
        Assert.assertNotNull(grantsAfterUpdate.getObjectName());
        Assert.assertNotNull(grantsAfterUpdate.getUserPermission());
        Assert.assertNotNull(grantsAfterUpdate.getGrantedBy());

        Assertions.assertThat(grants).isEqualToComparingFieldByField(grantsAfterUpdate);
    }

    @Test
    public void testDeleteGrants() {
        Grants grants = createGrants();

        grantsService.deleteGrants(grants.getId());
        Assert.assertFalse(grantsRepository.existsById(grants.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteGrantsNotFound() {
        grantsService.deleteGrants(UUID.randomUUID());
    }
}
