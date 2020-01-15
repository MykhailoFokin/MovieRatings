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
import solvve.course.domain.UserTypes;
import solvve.course.dto.UserTypesCreateDTO;
import solvve.course.dto.UserTypesPatchDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from user_types", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserTypesServiceTest {

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private UserTypesService userTypesService;

    private UserTypes createUserTypes() {
        UserTypes userTypes = new UserTypes();
        userTypes.setUserGroup(UserGroupType.USER);
        return userTypesRepository.save(userTypes);
    }

    @Transactional
    @Test
    public void testGetUserTypes() {
        UserTypes userTypes = createUserTypes();

        UserTypesReadDTO readDTO = userTypesService.getUserTypes(userTypes.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(userTypes);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserTypesWrongId() {
        userTypesService.getUserTypes(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateUserTypes() {
        UserTypesCreateDTO create = new UserTypesCreateDTO();
        create.setUserGroup(UserGroupType.USER);
        UserTypesReadDTO read = userTypesService.createUserTypes(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserTypes userTypes = userTypesRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(userTypes);
    }

    @Transactional
    @Test
    public void testPatchUserTypes() {
        UserTypes userTypes = createUserTypes();

        UserTypesPatchDTO patch = new UserTypesPatchDTO();
        patch.setUserGroup(UserGroupType.USER);
        UserTypesReadDTO read = userTypesService.patchUserTypes(userTypes.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        userTypes = userTypesRepository.findById(read.getId()).get();
        Assertions.assertThat(userTypes).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchUserTypesEmptyPatch() {
        UserTypes userTypes = createUserTypes();

        UserTypesPatchDTO patch = new UserTypesPatchDTO();
        UserTypesReadDTO read = userTypesService.patchUserTypes(userTypes.getId(), patch);

        Assert.assertNotNull(read.getUserGroup());

        UserTypes userTypesAfterUpdate = userTypesRepository.findById(read.getId()).get();

        Assert.assertNotNull(userTypesAfterUpdate.getUserGroup());

        Assertions.assertThat(userTypes).isEqualToComparingFieldByField(userTypesAfterUpdate);
    }

    @Test
    public void testDeleteUserTypes() {
        UserTypes userTypes = createUserTypes();

        userTypesService.deleteUserTypes(userTypes.getId());
        Assert.assertFalse(userTypesRepository.existsById(userTypes.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUserTypesNotFound() {
        userTypesService.deleteUserTypes(UUID.randomUUID());
    }
}
