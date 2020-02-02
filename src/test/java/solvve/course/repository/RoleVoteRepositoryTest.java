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
@Sql(statements = {"delete from role_vote",
        " delete from role",
        " delete from person",
        " delete from portal_user",
        " delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleVoteRepositoryTest {

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Test
    public void testSave() {
        Person person = new Person();
        person.setName("Name");
        person = personRepository.save(person);

        Role role = new Role();
        role.setPersonId(person);
        role = roleRepository.save(role);

        UserType userType = new UserType();
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userType);
        portalUser = portalUserRepository.save(portalUser);

        RoleVote r = new RoleVote();
        r.setRoleId(role);
        r.setUserId(portalUser);
        r = roleVoteRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleVoteRepository.findById(r.getId()).isPresent());
    }
}
