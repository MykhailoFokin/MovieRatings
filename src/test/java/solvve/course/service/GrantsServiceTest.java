package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.GrantsCreateDTO;
import solvve.course.dto.GrantsReadDTO;
import solvve.course.dto.PortalUsersReadDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GrantsRepository;
import solvve.course.repository.PortalUsersRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from grants", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GrantsServiceTest {

    @Autowired
    private GrantsRepository grantsRepository;

    @Autowired
    private GrantsService grantsService;

    @Autowired
    private PortalUsersService portalUsersService;

    @Autowired
    private PortalUsersRepository portalUsersRepository;

    private PortalUsersReadDTO portalUsersReadDTO;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired UserTypesService userTypesService;

    private UserTypesReadDTO userTypesReadDTO;

    UserTypes userTypes;

    @Before
    public void setup() {
        if (portalUsersReadDTO==null) {
            userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            userTypesReadDTO = userTypesService.getUserTypes(userTypes.getId());

            PortalUsers portalUsers = new PortalUsers();
            portalUsers.setUserType(userTypesReadDTO.getId());
            portalUsers.setSurname("Surname");
            portalUsers.setName("Name");
            portalUsers.setMiddleName("MiddleName");
            portalUsers.setLogin("Login");
            portalUsers.setUserConfidence(UserConfidenceType.NORMAL);
            portalUsers = portalUsersRepository.save(portalUsers);

            portalUsersReadDTO = portalUsersService.getPortalUsers(portalUsers.getId());
        }
    }

    @Test
    public void testGetGrants() {

        Grants grants = new Grants();
        grants.setId(UUID.randomUUID());
        grants.setUserTypeId(userTypes);
        grants.setObjectName("Movie");
        grants.setUserPermission(UserPermType.READ);
        grants.setGrantedBy(portalUsersReadDTO.getId());
        grants = grantsRepository.save(grants);

        GrantsReadDTO readDTO = grantsService.getGrants(grants.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(grants);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetGrantsWrongId() {
        grantsService.getGrants(UUID.randomUUID());
    }

    @Test
    public void testCreateGrants() {
        GrantsCreateDTO create = new GrantsCreateDTO();
        create.setUserTypeId(userTypes);
        create.setObjectName("Movie");
        create.setUserPermission(UserPermType.READ);
        create.setGrantedBy(portalUsersReadDTO.getId());
        GrantsReadDTO read = grantsService.createGrants(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        //Grants grants = grantsRepository.findById(read.getId()).get();
        //Assertions.assertThat(read).isEqualToComparingFieldByField(grants);
    }
}
