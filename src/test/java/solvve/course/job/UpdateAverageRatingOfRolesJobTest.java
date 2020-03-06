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
import solvve.course.repository.RoleRepository;
import solvve.course.service.RoleService;
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
public class UpdateAverageRatingOfRolesJobTest {

    @Autowired
    private UpdateAverageRatingOfRolesJob updateAverageRatingOfRolesJob;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private RoleRepository roleRepository;

    @SpyBean
    private RoleService roleService;

    @Test
    public void testUpdateAverageRatingOfRoles() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        testObjectsFactory.createRoleVote(portalUser1, role, UserVoteRatingType.R5);

        PortalUser portalUser2 = testObjectsFactory.createPortalUser();
        testObjectsFactory.createRoleVote(portalUser2, role, UserVoteRatingType.R7);

        updateAverageRatingOfRolesJob.updateAverageRatingOfRoles();

        role = roleRepository.findById(role.getId()).get();
        Assert.assertEquals(5.0, role.getAverageRating(), Double.MIN_NORMAL);
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
        }).when(roleService).updateAverageRatingOfRole(Mockito.any());

        updateAverageRatingOfRolesJob.updateAverageRatingOfRoles();

        for (Role m : roleRepository.findAll()) {
            if (m.getId().equals(failedId[0])) {
                Assert.assertNull(m.getAverageRating());
            } else {
                Assert.assertNotNull(m.getAverageRating());
            }
        }
    }
}
