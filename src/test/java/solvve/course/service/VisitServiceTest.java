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

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from visit",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitServiceTest {

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void testGetVisitExtended() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        VisitReadExtendedDTO readDTO = visitService.getVisit(visit.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(visit,
                "userId");
        Assertions.assertThat(readDTO.getUserId()).isEqualToIgnoringGivenFields(visit.getUserId(),
                "userTypeId");
        Assertions.assertThat(readDTO.getUserId().getUserTypeId()).isEqualTo(visit.getUserId().getUserTypeId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetVisitWrongId() {
        visitService.getVisit(UUID.randomUUID());
    }

    @Test
    public void testCreateVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();

        VisitCreateDTO create = testObjectsFactory.createVisitCreateDTO(portalUser);
        VisitReadDTO read = visitService.createVisit(create);
        Assertions.assertThat(read).isEqualToComparingFieldByField(read);

        Visit visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(create).isEqualToIgnoringGivenFields(visit,
                "userId");
        Assertions.assertThat(create.getUserId()).isEqualToIgnoringGivenFields(visit.getUserId().getId());
    }

    @Test
    public void testPatchVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        VisitPatchDTO patch = testObjectsFactory.createVisitPatchDTO(portalUser);
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(read,
                "userId","startAt", "finishAt");
        Assertions.assertThat(visit.getUserId().getId()).isEqualToIgnoringGivenFields(read.getUserId());
    }

    @Test
    public void testPatchVisitEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        VisitPatchDTO patch = new VisitPatchDTO();
        VisitReadDTO read = visitService.patchVisit(visit.getId(), patch);

        Assert.assertNotNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNotNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(visitAfterUpdate, "userId");
        Assertions.assertThat(visit.getUserId().getId())
                .isEqualToIgnoringGivenFields(visitAfterUpdate.getUserId().getId());
    }

    @Test
    public void testDeleteVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        visitService.deleteVisit(visit.getId());
        Assert.assertFalse(visitRepository.existsById(visit.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteVisitNotFound() {
        visitService.deleteVisit(UUID.randomUUID());
    }

    @Test
    public void testPutVisit() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        VisitPutDTO put = testObjectsFactory.createVisitPutDTO(portalUser);
        VisitReadDTO read = visitService.updateVisit(visit.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        visit = visitRepository.findById(read.getId()).get();
        Assertions.assertThat(visit).isEqualToIgnoringGivenFields(read,
                "userId");
        Assertions.assertThat(visit.getUserId().getId()).isEqualToIgnoringGivenFields(read.getUserId());
    }

    @Transactional
    @Test
    public void testPutVisitEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Visit visit = testObjectsFactory.createVisit(portalUser);

        VisitPutDTO put = new VisitPutDTO();
        VisitReadDTO read = visitService.updateVisit(visit.getId(), put);

        Assert.assertNull(read.getStartAt());

        Visit visitAfterUpdate = visitRepository.findById(read.getId()).get();

        Assert.assertNull(visitAfterUpdate.getStartAt());

        Assertions.assertThat(visit).isEqualToComparingFieldByField(visitAfterUpdate);
    }
}
