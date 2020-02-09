package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.VisitRepository;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from visit",
        "delete from portal_user",
        "delete from user_type",
        "delete from master"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitServiceTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;

    @Transactional
    @Test
    public void testGetVisitExtended() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        VisitReadExtendedDTO readDTO = visitService.getVisit(visit.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(visit,
                "userId", "masterId");
        Assertions.assertThat(readDTO.getUserId()).isEqualToIgnoringGivenFields(visit.getUserId(),
                "userType");
        Assertions.assertThat(readDTO.getUserId().getUserType()).isEqualTo(visit.getUserId().getUserTypeId().getId());
        Assertions.assertThat(readDTO.getMasterId()).isEqualToComparingFieldByField(visit.getMasterId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetVisitWrongId() {
        visitService.getVisit(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();

        VisitCreateDTO create = testObjectsFactory.createVisitCreateDTO(portalUser, master);
        VisitReadDTO read = visitService.createVisit(create);
        Assertions.assertThat(read).isEqualToComparingFieldByField(read);

        Visit visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(visit,
                "userId", "masterId");
        Assertions.assertThat(create.getUserId()).isEqualToIgnoringGivenFields(visit.getUserId().getId());
        Assertions.assertThat(create.getMasterId()).isEqualToIgnoringGivenFields(visit.getMasterId().getId());
    }

    @Transactional
    @Test
    public void testPatchVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        VisitPatchDTO patch = testObjectsFactory.createVisitPatchDTO(portalUser, master);
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(read,
                "userId", "masterId");
        Assertions.assertThat(visit.getUserId().getId()).isEqualToIgnoringGivenFields(read.getUserId());
        Assertions.assertThat(visit.getMasterId().getId()).isEqualToIgnoringGivenFields(read.getMasterId());
    }

    @Transactional
    @Test
    public void testPatchVisitEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        VisitPatchDTO patch = new VisitPatchDTO();
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assert.assertNotNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNotNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToComparingFieldByField(visitAfterUpdate);
    }

    @Test
    public void testDeleteVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        visitService.deleteVisit(visit.getId());
        Assert.assertFalse(visitRepository.existsById(visit.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVisitNotFound() {
        visitService.deleteVisit(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        VisitPutDTO put = testObjectsFactory.createVisitPutDTO(portalUser, master);
        VisitReadDTO read = visitService.putVisit(visit.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(read,
                "userId", "masterId");
        Assertions.assertThat(visit.getUserId().getId()).isEqualToIgnoringGivenFields(read.getUserId());
        Assertions.assertThat(visit.getMasterId().getId()).isEqualToIgnoringGivenFields(read.getMasterId());
    }

    @Transactional
    @Test
    public void testPutVisitEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Master master = testObjectsFactory.createMaster();
        Visit visit = testObjectsFactory.createVisit(portalUser, master);

        VisitPutDTO put = new VisitPutDTO();
        VisitReadDTO read = visitService.putVisit(visit.getId(), put);

        Assert.assertNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToComparingFieldByField(visitAfterUpdate);
    }
}
