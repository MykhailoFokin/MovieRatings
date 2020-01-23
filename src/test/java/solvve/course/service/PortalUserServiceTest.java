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
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserServiceTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private UserTypesService userTypesService;

    private UserTypes userTypes;

    private PortalUser createPortalUser() {
        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userTypes);
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        return portalUserRepository.save(portalUser);
    }

    @Before
    public void setup() throws Exception {
        if (userTypes==null) {
            userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);
        }
    }

    @Transactional
    @Test
    public void testGetPortalUsers() {
        PortalUser portalUser = createPortalUser();

        PortalUserReadDTO readDTO = portalUserService.getPortalUser(portalUser.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(portalUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUsersWrongId() {
        portalUserService.getPortalUser(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreatePortalUsers() {
        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setUserType(userTypes);
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        create.setLogin("Login");
        create.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.createPortalUser(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        PortalUser portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(portalUser);
    }

    @Transactional
    @Test
    public void testPatchPortalUser() {
        PortalUser portalUser = createPortalUser();

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        patch.setUserType(userTypes);
        patch.setSurname("Surname");
        patch.setName("Name");
        patch.setMiddleName("MiddleName");
        patch.setLogin("Login");
        patch.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        portalUser = portalUserRepository.findById(read.getId()).get();
        Assertions.assertThat(portalUser).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchPortalUserEmptyPatch() {
        PortalUser portalUser = createPortalUser();

        PortalUserPatchDTO patch = new PortalUserPatchDTO();
        PortalUserReadDTO read = portalUserService.patchPortalUser(portalUser.getId(), patch);

        Assert.assertNotNull(read.getUserType());
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

        Assertions.assertThat(portalUser).isEqualToComparingFieldByField(portalUserAfterUpdate);
    }

    @Test
    public void testDeletePortalUser() {
        PortalUser portalUser = createPortalUser();

        portalUserService.deletePortalUser(portalUser.getId());
        Assert.assertFalse(portalUserRepository.existsById(portalUser.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePortalUserNotFound() {
        portalUserService.deletePortalUser(UUID.randomUUID());
    }
}
