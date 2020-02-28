package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.VisitFilter;
import solvve.course.service.VisitService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from visit",
        "delete from portal_user",
        "delete from user_type"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitRepositoryTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetMasterVisits() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();
        Visit v1 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        testObjectsFactory.createVisit(p2, VisitStatus.CANCELLED);
        Visit v2 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        testObjectsFactory.createVisit(p2, VisitStatus.SCHEDULED);

        List<Visit> res = visitRepository.findByPortalUserIdAndStatusOrderByStartAtAsc(p1.getId(),
                VisitStatus.SCHEDULED);
        Assertions.assertThat(res).extracting(Visit::getId).isEqualTo(Arrays.asList(v1.getId(), v2.getId()));
    }

    @Test
    public void testfindVisitForMasterInGivenInterval() {
        PortalUser p = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();
        ZoneOffset utc = ZoneOffset.UTC;
        Visit v1 = testObjectsFactory.createVisit(p, 
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        testObjectsFactory.createVisit(p, 
                VisitStatus.CANCELLED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        Visit v2 = testObjectsFactory.createVisit(p,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 30, 0).toInstant(utc));
        testObjectsFactory.createVisit(p2,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        testObjectsFactory.createVisit(p2,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));

        List<Visit> res = visitRepository.findVisitsForUserInGivenInterval(p.getId(),VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc),
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        Assertions.assertThat(res).extracting(Visit::getId).isEqualTo(Arrays.asList(v1.getId(), v2.getId()));
    }

    @Test
    public void testGetVisitsWithEmptyFilter() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();

        Visit v1 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        Visit v2 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        Visit v3 = testObjectsFactory.createVisit(p2, VisitStatus.SCHEDULED);

        VisitFilter filter = new VisitFilter();
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v2.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByPortalUser() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();

        Visit v1 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        Visit v2 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED);
        testObjectsFactory.createVisit(p2, VisitStatus.SCHEDULED);

        VisitFilter filter = new VisitFilter();
        filter.setPortalUserId(p1.getId());
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v2.getId());
    }

    @Test
    public void testGetVisitsByStatuses() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();

        testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED, testObjectsFactory.createInstant(12));
        Visit v2 = testObjectsFactory.createVisit(p1, VisitStatus.CANCELLED, testObjectsFactory.createInstant(13));
        Visit v3 = testObjectsFactory.createVisit(p2, VisitStatus.FINISHED, testObjectsFactory.createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setStatuses(Set.of(VisitStatus.CANCELLED, VisitStatus.FINISHED));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v2.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByStartAtInterval() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();

        Visit v1 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED, testObjectsFactory.createInstant(12));
        testObjectsFactory.createVisit(p1, VisitStatus.CANCELLED, testObjectsFactory.createInstant(13));
        Visit v3 = testObjectsFactory.createVisit(p2, VisitStatus.FINISHED, testObjectsFactory.createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setStartAtFrom(testObjectsFactory.createInstant(9));
        filter.setStartAtTo(testObjectsFactory.createInstant(13));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByAllFilters() {
        PortalUser p1 = testObjectsFactory.createPortalUser();
        PortalUser p2 = testObjectsFactory.createPortalUser();

        Visit v1 = testObjectsFactory.createVisit(p1, VisitStatus.SCHEDULED, testObjectsFactory.createInstant(12));
        testObjectsFactory.createVisit(p1, VisitStatus.CANCELLED, testObjectsFactory.createInstant(13));
        testObjectsFactory.createVisit(p2, VisitStatus.FINISHED, testObjectsFactory.createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setPortalUserId(p1.getId());
        filter.setStartAtFrom(testObjectsFactory.createInstant(9));
        filter.setStartAtTo(testObjectsFactory.createInstant(13));
        filter.setStatuses(Set.of(VisitStatus.SCHEDULED));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId());
    }
}
