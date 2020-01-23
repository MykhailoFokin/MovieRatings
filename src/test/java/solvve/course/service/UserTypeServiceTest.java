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
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserType;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypeRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from user_type", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserTypeServiceTest {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserTypeService userTypeService;

    private UserType createUserTypes() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        return userTypeRepository.save(userType);
    }

    @Transactional
    @Test
    public void testGetUserTypes() {
        UserType userType = createUserTypes();

        UserTypeReadDTO readDTO = userTypeService.getUserTypes(userType.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(userType);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserTypesWrongId() {
        userTypeService.getUserTypes(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateUserTypes() {
        UserTypeCreateDTO create = new UserTypeCreateDTO();
        create.setUserGroup(UserGroupType.USER);
        UserTypeReadDTO read = userTypeService.createUserTypes(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserType userType = userTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(userType);
    }

    @Transactional
    @Test
    public void testPatchUserTypes() {
        UserType userType = createUserTypes();

        UserTypePatchDTO patch = new UserTypePatchDTO();
        patch.setUserGroup(UserGroupType.USER);
        UserTypeReadDTO read = userTypeService.patchUserTypes(userType.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        userType = userTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(userType).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchUserTypesEmptyPatch() {
        UserType userType = createUserTypes();

        UserTypePatchDTO patch = new UserTypePatchDTO();
        UserTypeReadDTO read = userTypeService.patchUserTypes(userType.getId(), patch);

        Assert.assertNotNull(read.getUserGroup());

        UserType userTypeAfterUpdate = userTypeRepository.findById(read.getId()).get();

        Assert.assertNotNull(userTypeAfterUpdate.getUserGroup());

        Assertions.assertThat(userType).isEqualToComparingFieldByField(userTypeAfterUpdate);
    }

    @Test
    public void testDeleteUserTypes() {
        UserType userType = createUserTypes();

        userTypeService.deleteUserTypes(userType.getId());
        Assert.assertFalse(userTypeRepository.existsById(userType.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUserTypesNotFound() {
        userTypeService.deleteUserTypes(UUID.randomUUID());
    }
}
