package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from role_vote; delete from role; delete from persons; delete from portal_user; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleVoteRepositoryTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testSave() {
        Persons person = new Persons();
        person.setName("Name");
        person = personsRepository.save(person);

        Role role = new Role();
        role.setPersonId(person);
        role = roleRepository.save(role);

        UserTypes userTypes = new UserTypes();
        userTypes = userTypesRepository.save(userTypes);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userTypes);
        portalUser = portalUserRepository.save(portalUser);

        RoleVote r = new RoleVote();
        r.setRoleId(role);
        r.setUserId(portalUser);
        r = roleVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleVoteRepository.findById(r.getId()).isPresent());
    }
}
