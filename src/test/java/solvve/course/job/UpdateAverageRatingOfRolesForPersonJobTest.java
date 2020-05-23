package solvve.course.job;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.repository.PersonRepository;
import solvve.course.service.PersonService;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_vote",
        "delete from portal_user",
        "delete from role",
        "delete from movie",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UpdateAverageRatingOfRolesForPersonJobTest {

    @Autowired
    private UpdateAverageRatingOfRolesForPersonJob updateAverageRatingOfRoles;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private PersonRepository personRepository;

    @SpyBean
    private PersonService personService;

    @Test
    public void testUpdateAverageRatingOfRolesForPerson() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        testObjectsFactory.createRoleVote(portalUser1, role, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createRoleVote(portalUser2, role, UserVoteRatingType.R7);

        updateAverageRatingOfRoles.updateAverageRatingOfRolesForPersons();

        person = personRepository.findById(person.getId()).get();
        Assert.assertEquals(5.0, person.getAverageRoleRating(), Double.MIN_NORMAL);
    }

    @Test
    public void testRolesUpdatedIndependently() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        testObjectsFactory.createRoleVote(portalUser1, role, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createRoleVote(portalUser2, role, UserVoteRatingType.R7);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocationOnMock -> {
            if (failedId[0] == null) {
                failedId[0] = invocationOnMock.getArgument(0);
                throw new RuntimeException();
            }
            return invocationOnMock.callRealMethod();
        }).when(personService).updateAverageRatingOfRoleForPerson(Mockito.any());

        updateAverageRatingOfRoles.updateAverageRatingOfRolesForPersons();

        for (Person m : personRepository.findAll()) {
            if (m.getId().equals(failedId[0])) {
                Assert.assertEquals(0.0, m.getAverageRoleRating(), Double.MIN_NORMAL);
            } else {
                Assert.assertNotNull(m.getAverageRoleRating());
            }
        }
    }
}
