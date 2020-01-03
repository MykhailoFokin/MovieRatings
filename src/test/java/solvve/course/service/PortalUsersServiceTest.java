package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.PortalUsers;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;
import solvve.course.dto.PortalUsersCreateDTO;
import solvve.course.dto.PortalUsersReadDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUsersRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from portal_users", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUsersServiceTest {

    @Autowired
    private PortalUsersRepository portalUsersRepository;

    @Autowired
    private PortalUsersService portalUsersService;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private UserTypesService userTypesService;

    private UserTypesReadDTO userTypesReadDTO;

    @Before
    public void setup() throws Exception {
        if (userTypesReadDTO==null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            userTypesReadDTO = userTypesService.getUserTypes(userTypes.getId());
        }
    }

    @Test
    public void testGetPortalUsers() {
        PortalUsers portalUsers = new PortalUsers();
        portalUsers.setId(UUID.randomUUID());
        portalUsers.setUserType(userTypesReadDTO.getId());
        portalUsers.setSurname("Surname");
        portalUsers.setName("Name");
        portalUsers.setMiddleName("MiddleName");
        portalUsers.setLogin("Login");
        portalUsers.setUserConfidence(UserConfidenceType.NORMAL);
        portalUsers = portalUsersRepository.save(portalUsers);

        PortalUsersReadDTO readDTO = portalUsersService.getPortalUsers(portalUsers.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(portalUsers);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUsersWrongId() {
        portalUsersService.getPortalUsers(UUID.randomUUID());
    }

    @Test
    public void testCreatePortalUsers() {
        PortalUsersCreateDTO create = new PortalUsersCreateDTO();
        create.setUserType(userTypesReadDTO.getId());
        create.setSurname("Surname");
        create.setName("Name");
        create.setMiddleName("MiddleName");
        create.setLogin("Login");
        create.setUserConfidence(UserConfidenceType.NORMAL);
        PortalUsersReadDTO read = portalUsersService.createPortalUsers(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        PortalUsers portalUsers = portalUsersRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(portalUsers);
    }
}
