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

import java.time.Instant;
import java.time.LocalDate;
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
        "delete from master"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VisitRepositoryTest {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Transactional
    @Test
    public void testGetMasterVisits() {
        PortalUser p = createPortalUser();
        Master m1 = createMaster();
        Master m2 = createMaster();
        Visit v1 = createVisit(p, m1, VisitStatus.SCHEDULED);
        createVisit(p, m1, VisitStatus.CANCELLED);
        Visit v2 = createVisit(p, m1, VisitStatus.SCHEDULED);
        createVisit(p, m2, VisitStatus.SCHEDULED);

        List<Visit> res = visitRepository.findByMasterIdAndStatusOrderByStartAtAsc(m1,VisitStatus.SCHEDULED);
        Assertions.assertThat(res).extracting(Visit::getId).isEqualTo(Arrays.asList(v1.getId(), v2.getId()));
    }

    @Test
    public void testfindVisitForMasterInGivenInterval() {
        PortalUser p = createPortalUser();
        Master m1 = createMaster();
        Master m2 = createMaster();
        ZoneOffset utc = ZoneOffset.UTC;
        Visit v1 = createVisit(p, m1,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        createVisit(p, m1,
                VisitStatus.CANCELLED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));
        Visit v2 = createVisit(p, m1,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 30, 0).toInstant(utc));
        createVisit(p, m1,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        createVisit(p, m2,
                VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc));

        List<Visit> res = visitRepository.findVisitsForMasterInGivenInterval(m1.getId(),VisitStatus.SCHEDULED,
                LocalDateTime.of(2019, 12, 4, 15, 0, 0).toInstant(utc),
                LocalDateTime.of(2019, 12, 4, 17, 30, 0).toInstant(utc));
        Assertions.assertThat(res).extracting(Visit::getId).isEqualTo(Arrays.asList(v1.getId(), v2.getId()));
    }

    @Test
    public void testGetVisitsWithEmptyFilter() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        Visit v1 = createVisit(p1, m1, VisitStatus.SCHEDULED);
        Visit v2 = createVisit(p1, m2, VisitStatus.SCHEDULED);
        Visit v3 = createVisit(p2, m2, VisitStatus.SCHEDULED);

        VisitFilter filter = new VisitFilter();
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v2.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByPortalUser() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        Visit v1 = createVisit(p1, m1, VisitStatus.SCHEDULED);
        Visit v2 = createVisit(p1, m2, VisitStatus.SCHEDULED);
        createVisit(p2, m2, VisitStatus.SCHEDULED);

        VisitFilter filter = new VisitFilter();
        filter.setUserId(p1.getId());
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v2.getId());
    }

    @Test
    public void testGetVisitsByMaster() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        createVisit(p1, m1, VisitStatus.SCHEDULED);
        Visit v2 = createVisit(p1, m2, VisitStatus.SCHEDULED);
        Visit v3 = createVisit(p2, m2, VisitStatus.SCHEDULED);

        VisitFilter filter = new VisitFilter();
        filter.setMasterId(m2.getId());
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v2.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByStatuses() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        createVisit(p1, m1, VisitStatus.SCHEDULED, createInstant(12));
        Visit v2 = createVisit(p1, m2, VisitStatus.CANCELLED, createInstant(13));
        Visit v3 = createVisit(p2, m2, VisitStatus.FINISHED, createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setStatuses(Set.of(VisitStatus.CANCELLED, VisitStatus.FINISHED));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v2.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByStartAtInterval() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        Visit v1 = createVisit(p1, m1, VisitStatus.SCHEDULED, createInstant(12));
        createVisit(p1, m2, VisitStatus.CANCELLED, createInstant(13));
        Visit v3 = createVisit(p2, m2, VisitStatus.FINISHED, createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setStartAtFrom(createInstant(9));
        filter.setStartAtTo(createInstant(13));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId(),v3.getId());
    }

    @Test
    public void testGetVisitsByAllFilters() {
        PortalUser p1 = createPortalUser();
        Master m1 = createMaster();
        PortalUser p2 = createPortalUser();
        Master m2 = createMaster();

        Visit v1 = createVisit(p1, m1, VisitStatus.SCHEDULED, createInstant(12));
        createVisit(p1, m2, VisitStatus.CANCELLED, createInstant(13));
        createVisit(p2, m2, VisitStatus.FINISHED, createInstant(9));

        VisitFilter filter = new VisitFilter();
        filter.setUserId(p1.getId());
        filter.setMasterId(m1.getId());
        filter.setStartAtFrom(createInstant(9));
        filter.setStartAtTo(createInstant(13));
        filter.setStatuses(Set.of(VisitStatus.SCHEDULED));
        Assertions.assertThat(visitService.getVisits(filter)).extracting("Id")
                .containsExactlyInAnyOrder(v1.getId());
    }

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
        master.setId(UUID.randomUUID());
        master.setName("MasterName");
        master.setPhone("645768767");
        master.setAbout("What about");
        master = masterRepository.save(master);

        return master;
    }

    private Visit createVisit(PortalUser portalUser, Master master, VisitStatus visitStatus) {
        Visit visit = new Visit();
        visit.setId(UUID.randomUUID());
        visit.setUserId(portalUser);
        visit.setMasterId(master);
        visit.setStartAt(Instant.now());
        visit.setFinishAt(Instant.now());
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    private Visit createVisit(PortalUser portalUser, Master master, VisitStatus visitStatus, Instant startAt) {
        Visit visit = new Visit();
        visit.setId(UUID.randomUUID());
        visit.setUserId(portalUser);
        visit.setMasterId(master);
        visit.setStartAt(startAt);
        visit.setFinishAt(Instant.now());
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    private Instant createInstant(int hour) {
        return LocalDateTime.of(2019, 12, 23, hour, 0).toInstant(ZoneOffset.UTC);
    }
}
