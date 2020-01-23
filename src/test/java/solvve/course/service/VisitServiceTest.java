package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.dto.VisitCreateDTO;
import solvve.course.dto.VisitPatchDTO;
import solvve.course.dto.VisitReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MasterRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypesRepository;
import solvve.course.repository.VisitRepository;

import java.time.Instant;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from visit; delete from portal_user; delete from user_types; delete from master;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitServiceTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserTypesService userTypesService;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private MasterRepository masterRepository;

    private PortalUser portalUser;

    private Master master;

    @Before
    public void setup() {
        if (portalUser == null) {
            UserTypes userTypes = new UserTypes();
            userTypes.setUserGroup(UserGroupType.USER);
            userTypes = userTypesRepository.save(userTypes);

            portalUser = new PortalUser();
            portalUser.setUserType(userTypes);
            portalUser.setSurname("Surname");
            portalUser.setName("Name");
            portalUser.setMiddleName("MiddleName");
            portalUser.setLogin("Login");
            portalUser.setUserConfidence(UserConfidenceType.NORMAL);
            portalUser = portalUserRepository.save(portalUser);
        }

        if (master == null) {
            master = new Master();
            master. setId(UUID.randomUUID());
            master.setName("MasterName");
            master.setPhone("645768767");
            master.setAbout("What about");
            master = masterRepository.save(master);
        }
    }

    private Visit createVisit() {
        Visit visit = new Visit();
        visit.setId(UUID.randomUUID());
        visit.setUserId(portalUser);
        visit.setMasterId(master);
        visit.setStartAt(Instant.now());
        visit.setFinishAt(Instant.now());
        visit.setStatus(VisitStatus.FINISHED);
        return visitRepository.save(visit);
    }

    @Transactional
    @Test
    public void testGetVisit() {
        Visit visit = createVisit();

        VisitReadDTO readDTO = visitService.getVisit(visit.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(visit);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetVisitWrongId() {
        visitService.getVisit(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateVisit() {
        VisitCreateDTO create = new VisitCreateDTO();
        create.setUserId(portalUser);
        create.setMasterId(master);
        create.setStartAt(Instant.now());
        create.setFinishAt(Instant.now());
        create.setStatus(VisitStatus.FINISHED);
        VisitReadDTO read = visitService.createVisit(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Visit visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(visit);
    }

    @Transactional
    @Test
    public void testPatchVisit() {
        Visit visit = createVisit();

        VisitPatchDTO patch = new VisitPatchDTO();
        patch.setUserId(portalUser);
        patch.setMasterId(master);
        patch.setStartAt(Instant.now());
        patch.setFinishAt(Instant.now());
        patch.setStatus(VisitStatus.FINISHED);
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(visit).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchVisitEmptyPatch() {
        Visit visit = createVisit();

        VisitPatchDTO patch = new VisitPatchDTO();
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assert.assertNotNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNotNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToComparingFieldByField(visitAfterUpdate);
    }

    @Test
    public void testDeleteVisit() {
        Visit visit = createVisit();

        visitService.deleteVisit(visit.getId());
        Assert.assertFalse(visitRepository.existsById(visit.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVisitNotFound() {
        visitService.deleteVisit(UUID.randomUUID());
    }
}
