package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;
import solvve.course.dto.UserTypesCreateDTO;
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

    @Test
    public void testGetUserTypes() {
        UserTypes userTypes = new UserTypes();
        userTypes.setId(UUID.randomUUID());
        userTypes.setUserGroup(UserGroupType.USER);
        userTypes = userTypesRepository.save(userTypes);

        UserTypesReadDTO readDTO = userTypesService.getUserTypes(userTypes.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(userTypes);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserTypesWrongId() {
        userTypesService.getUserTypes(UUID.randomUUID());
    }

    @Test
    public void testCreateUserTypes() {
        UserTypesCreateDTO create = new UserTypesCreateDTO();
        create.setUserGroup(UserGroupType.USER);
        UserTypesReadDTO read = userTypesService.createUserTypes(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        UserTypes userTypes = userTypesRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(userTypes);
    }
}
