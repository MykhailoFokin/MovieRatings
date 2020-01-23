package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.VisitCreateDTO;
import solvve.course.dto.VisitPatchDTO;
import solvve.course.dto.VisitReadDTO;
import solvve.course.dto.VisitReadExtendedDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MasterRepository;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserTypeRepository;
import solvve.course.repository.VisitRepository;

import java.time.Instant;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from visit; delete from portal_user; delete from user_type; delete from master;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitServiceTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private MasterRepository masterRepository;

    private PortalUser createPortalUser() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserType(userType);
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    private Master createMaster() {
        Master master = new Master();
        master. setId(UUID.randomUUID());
        master.setName("MasterName");
        master.setPhone("645768767");
        master.setAbout("What about");
        master = masterRepository.save(master);

        return master;
    }

    private Visit createVisit(PortalUser portalUser, Master master) {
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
    public void testGetVisitExtended() {
        PortalUser portalUser = createPortalUser();
        Master master = createMaster();
        Visit visit = createVisit(portalUser, master);

        VisitReadExtendedDTO readDTO = visitService.getVisit(visit.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(visit, "userId", "masterId");
        Assertions.assertThat(readDTO.getUserId()).isEqualToIgnoringGivenFields(portalUser);
        Assertions.assertThat(readDTO.getMasterId()).isEqualToIgnoringGivenFields(master);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetVisitWrongId() {
        visitService.getVisit(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateVisit() {
        PortalUser portalUser = createPortalUser();
        Master master = createMaster();

        VisitCreateDTO create = new VisitCreateDTO();
        create.setUserId(portalUser);
        create.setMasterId(master);
        create.setStartAt(Instant.now());
        create.setFinishAt(Instant.now());
        create.setStatus(VisitStatus.FINISHED);
        VisitReadExtendedDTO read = visitService.createVisit(create);
        //Assertions.assertThat(create).isEqualToComparingFieldByField(read);
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(read, "userId", "masterId");
        Assertions.assertThat(create.getUserId()).isEqualToIgnoringGivenFields(portalUser);
        Assertions.assertThat(create.getMasterId()).isEqualToIgnoringGivenFields(master);

        Visit visit = visitRepository.findById(read.getId()).get();
        //Assertions.assertThat(read).isEqualToComparingFieldByField(visit);
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(visit, "userId", "masterId");
        Assertions.assertThat(create.getUserId()).isEqualToIgnoringGivenFields(portalUser);
        Assertions.assertThat(create.getMasterId()).isEqualToIgnoringGivenFields(master);
    }

    @Transactional
    @Test
    public void testPatchVisit() {
        PortalUser portalUser = createPortalUser();
        Master master = createMaster();
        Visit visit = createVisit(portalUser, master);

        VisitPatchDTO patch = new VisitPatchDTO();
        patch.setUserId(portalUser);
        patch.setMasterId(master);
        patch.setStartAt(Instant.now());
        patch.setFinishAt(Instant.now());
        patch.setStatus(VisitStatus.FINISHED);
        VisitReadExtendedDTO read = visitService.patchVisit(visit.getId(), patch);

        //Assertions.assertThat(patch).isEqualToComparingFieldByField(read);
        Assertions.assertThat(patch).isEqualToIgnoringGivenFields(read, "userId", "masterId");
        Assertions.assertThat(patch.getUserId()).isEqualToIgnoringGivenFields(portalUser);
        Assertions.assertThat(patch.getMasterId()).isEqualToIgnoringGivenFields(master);

        visit = visitRepository.findById(read.getId()).get();
        //Assertions.assertThat(visit).isEqualToComparingFieldByField(read);
        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(read, "userId", "masterId");
        Assertions.assertThat(visit.getUserId()).isEqualToIgnoringGivenFields(portalUser);
        Assertions.assertThat(visit.getMasterId()).isEqualToIgnoringGivenFields(master);
    }

    @Transactional
    @Test
    public void testPatchVisitEmptyPatch() {
        PortalUser portalUser = createPortalUser();
        Master master = createMaster();
        Visit visit = createVisit(portalUser, master);

        VisitPatchDTO patch = new VisitPatchDTO();
        VisitReadExtendedDTO read = visitService.patchVisit(visit.getId(), patch);

        Assert.assertNotNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNotNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToComparingFieldByField(visitAfterUpdate);
    }

    @Test
    public void testDeleteVisit() {
        PortalUser portalUser = createPortalUser();
        Master master = createMaster();
        Visit visit = createVisit(portalUser, master);

        visitService.deleteVisit(visit.getId());
        Assert.assertFalse(visitRepository.existsById(visit.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVisitNotFound() {
        visitService.deleteVisit(UUID.randomUUID());
    }
}
