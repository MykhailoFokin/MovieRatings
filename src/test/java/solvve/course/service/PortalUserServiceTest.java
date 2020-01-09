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
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from portal_user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PortalUserServiceTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PortalUserService portalUserService;

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
        PortalUser portalUser = new PortalUser();
        portalUser.setId(UUID.randomUUID());
        portalUser.setUserType(userTypesReadDTO.getId());
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        PortalUserReadDTO readDTO = portalUserService.getPortalUser(portalUser.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(portalUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPortalUsersWrongId() {
        portalUserService.getPortalUser(UUID.randomUUID());
    }

    @Test
    public void testCreatePortalUsers() {
        PortalUserCreateDTO create = new PortalUserCreateDTO();
        create.setUserType(userTypesReadDTO.getId());
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
}
