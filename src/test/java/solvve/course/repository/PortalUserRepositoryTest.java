package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class PortalUserRepositoryTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Test
    public void testSave() {
        UserTypes userTypes = new UserTypes();
        userTypes.setUserGroup(UserGroupType.USER);
        userTypes = userTypesRepository.save(userTypes);

        PortalUser r = new PortalUser();
        r.setUserType(userTypes);
        r = portalUserRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(portalUserRepository.findById(r.getId()).isPresent());
    }
}
