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
import solvve.course.domain.*;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypePutDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypeRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from user_type"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserTypeServiceTest {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetUserTypes() {
        UserType userType = testObjectsFactory.createUserType();

        testObjectsFactory.inTransaction(()-> {
            UserTypeReadDTO readDTO = userTypeService.getUserTypes(userType.getId());
            Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(userType,"portalUserId");
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserTypesWrongId() {
        userTypeService.getUserTypes(UUID.randomUUID());
    }

    @Test
    public void testCreateUserTypes() {
        UserTypeCreateDTO create = new UserTypeCreateDTO();
        create.setUserGroup(UserGroupType.USER);
        UserTypeReadDTO read = userTypeService.createUserTypes(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserType userType = userTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(userType,"portalUserId");
    }

    @Test
    public void testPatchUserTypes() {
        UserType userType = testObjectsFactory.createUserType();

        UserTypePatchDTO patch = new UserTypePatchDTO();
        patch.setUserGroup(UserGroupType.USER);
        UserTypeReadDTO read = userTypeService.patchUserTypes(userType.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        userType = userTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(userType).isEqualToIgnoringGivenFields(read,"userGrants","portalUser");
    }

    @Test
    public void testPatchUserTypesEmptyPatch() {
        UserType userType = testObjectsFactory.createUserType();

        UserTypePatchDTO patch = new UserTypePatchDTO();
        UserTypeReadDTO read = userTypeService.patchUserTypes(userType.getId(), patch);

        Assert.assertNotNull(read.getUserGroup());

        UserType userTypeAfterUpdate = userTypeRepository.findById(read.getId()).get();

        Assert.assertNotNull(userTypeAfterUpdate.getUserGroup());

        Assertions.assertThat(userType).isEqualToIgnoringGivenFields(userTypeAfterUpdate, "userGrants");
    }

    @Test
    public void testDeleteUserTypes() {
        UserType userType = testObjectsFactory.createUserType();

        userTypeService.deleteUserTypes(userType.getId());
        Assert.assertFalse(userTypeRepository.existsById(userType.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUserTypesNotFound() {
        userTypeService.deleteUserTypes(UUID.randomUUID());
    }

    @Test
    public void testPutUserTypes() {
        UserType userType = testObjectsFactory.createUserType();

        UserTypePutDTO put = new UserTypePutDTO();
        put.setUserGroup(UserGroupType.USER);
        UserTypeReadDTO read = userTypeService.updateUserTypes(userType.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        userType = userTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(userType).isEqualToIgnoringGivenFields(read,"userGrants","portalUser");
    }

    @Test
    public void testPutUserTypesEmptyPut() {
        UserType userType = testObjectsFactory.createUserType();

        UserTypePutDTO put = new UserTypePutDTO();
        UserTypeReadDTO read = userTypeService.updateUserTypes(userType.getId(), put);

        Assert.assertNull(read.getUserGroup());

        testObjectsFactory.inTransaction(() -> {
            UserType userTypeAfterUpdate = userTypeRepository.findById(read.getId()).get();

            Assert.assertNull(userTypeAfterUpdate.getUserGroup());

            Assertions.assertThat(userType).isEqualToComparingOnlyGivenFields(userTypeAfterUpdate,"id");
        });
    }
}
